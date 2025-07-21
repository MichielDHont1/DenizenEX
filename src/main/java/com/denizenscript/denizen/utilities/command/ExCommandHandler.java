package com.denizenscript.denizen.utilities.command;

//import com.denizenscript.denizen.objects.NPCTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.tags.BukkitTagContext;
import com.denizenscript.denizen.utilities.ChatColor;
import com.denizenscript.denizen.utilities.FormattedTextHelper;
import com.denizenscript.denizen.utilities.Settings;
//import com.denizenscript.denizen.utilities.depends.Depends;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.scripts.ScriptBuilder;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.core.FlagCommand;
import com.denizenscript.denizencore.scripts.queues.core.InstantQueue;
import com.denizenscript.denizencore.utilities.CoreConfiguration;
import com.denizenscript.denizencore.utilities.ExCommandHelper;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mongodb.lang.Nullable;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class ExCommandHandler {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("execute").requires((SourceStack) -> {
            return SourceStack.hasPermission(3);
        }).then(Commands.argument("script", StringArgumentType.string()).executes((SourceStack) -> {
            return execute(SourceStack.getSource(), StringArgumentType.getString(SourceStack, "script"));
        })));
    }

        private static int execute(CommandSourceStack command, String arguments){

            // <--[language]
            // @name /ex command
            // @group Console Commands
            // @description
            // The '/ex' command is an easy way to run a single denizen script command in-game.
            // 'Ex' is short for 'Execute'.
            // Its syntax, aside from '/ex' is exactly the same as any other Denizen script command.
            // When running a command, some context is also supplied, such as '<player>' if being run by a player (versus the console),
            // as well as '<npc>' if a NPC is selected by using the '/npc sel' command.
            //
            // By default, ex command debug output is sent to the player that ran the ex command (if the command was ran by a player).
            // To avoid this, use '-q' at the start of the ex command.
            // Like: /ex -q narrate "wow no output"
            //
            // The '/ex' command creates a new queue each time it's run,
            // meaning for example '/ex define' would do nothing, as the definition will be lost immediately.
            //
            // If you need to sustain a queue between multiple executions, use '/exs' ("Execute Sustained").
            // A sustained queue will use the same queue on every execution until the queue stops (normally due to '/exs stop').
            // Be warned that waits will block the sustained queue - eg '/exs wait 10m' will make '/exs' effectively unusable for 10 minutes.
            //
            // Examples:
            // /ex flag <player> test_flag:!
            // /ex run npc_walk_script
            //
            // Need to '/ex' a command as a different player or NPC? Use <@link language The Player and NPC Arguments>.
            //
            // Examples:
            // /ex narrate player:<[aplayer]> 'Your health is <player.health.formatted>.'
            // /ex walk npc:<[some_npc]> <player.cursor_on>
            //
            // -->
            String[] flags;
            Player sender;
            String type = null;
            String filter = null;
            int PageNumber = -1;
            boolean quiet = !Settings.showExDebug();
            if(command.getEntity() instanceof Player){
                sender = (Player) command.getEntity();
            } else {
                sender = null;
            }
            if (arguments != null) {
                flags = arguments.split(" ");
                for (String flag : flags)
                {
                    if (flag.contains("-q"))
                    {
                        quiet = !quiet;
                        org.apache.commons.lang3.ArrayUtils.removeElement(flags, PageNumber);
                        arguments.replace(" -q", "");
                    }
                }
            }
            if (Settings.showExHelp()) {
                if (CoreConfiguration.shouldShowDebug) {
                    if (quiet) {
                        sender.sendMessage(new TextComponent(ChatColor.YELLOW + "Executing Denizen script command... check the console for full debug output!"), Util.NIL_UUID);
                    }
                }
                else {
                    sender.sendMessage(new TextComponent(ChatColor.YELLOW + "Executing Denizen script command... to see debug, use /denizen debug"), Util.NIL_UUID);
                }
            }
            List<Object> entries = new ArrayList<>();
            entries.add(arguments);
            InstantQueue queue = new InstantQueue("EXCOMMAND");
//            NPCTag npc = null;
//            if (Depends.citizens != null && Depends.citizens.getNPCSelector().getSelected(sender) != null) {
//                npc = new NPCTag(Depends.citizens.getNPCSelector().getSelected(sender));
//            }
            List<ScriptEntry> scriptEntries = ScriptBuilder.buildScriptEntries(entries, null, new BukkitScriptEntryData(sender != null ? new PlayerTag(sender) : null/*, npc*/));
            queue.addEntries(scriptEntries);
            if (!quiet && sender instanceof Player) {
                queue.debugOutput = s -> sender.sendMessage(FormattedTextHelper.parse(s.replace("<FORCE_ALIGN>", ""), com.denizenscript.denizen.utilities.ChatColor.WHITE), Util.NIL_UUID);
            }
            queue.start();
            return Command.SINGLE_SUCCESS;
        }


    static {
        FlagCommand.flagTabCompleters.add(ExCommandHandler::onFlagTabComplete);
    }

    public static void onFlagTabComplete(AbstractCommand.TabCompletionsBuilder builder) {
        BukkitTagContext context = (BukkitTagContext) builder.context;
        if (context.player != null) {
            //todo implement player
//            for (String flagName : context.player.getFlagTracker().listAllFlags()) {
//                if (!flagName.startsWith("__")) {
//                    builder.add(flagName);
//                }
//            }
        }
//        if (context.npc != null) {
//            for (String flagName : context.npc.getFlagTracker().listAllFlags()) {
//                if (!flagName.startsWith("__")) {
//                    builder.add(flagName);
//                }
//            }
//        }
    }
//
//    @Override
//    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] rawArgs) {
//        BukkitTagContext context = new BukkitTagContext(sender instanceof Player player ? new PlayerTag(player) : null, null, null);
//        if (Depends.citizens != null && Depends.citizens.getNPCSelector().getSelected(sender) != null) {
//            context.npc = new NPCTag(Depends.citizens.getNPCSelector().getSelected(sender));
//        }
//        return ExCommandHelper.buildTabCompletions(rawArgs, context);
//    }



}
