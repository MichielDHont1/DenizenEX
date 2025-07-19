package com.denizenscript.denizen.events.server;

import com.denizenscript.denizen.events.BukkitScriptEvent;


import com.denizenscript.denizen.utilities.ChatColor;
import com.denizenscript.denizen.utilities.Settings;
import com.denizenscript.denizen.utilities.flags.PlayerFlagHandler;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.utilities.debugging.StrongWarning;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import com.denizenscript.denizen.utilities.debugging.StatsRecord;

public class Tickevent extends BukkitScriptEvent {
    private int tickTimer = 0;
    @SubscribeEvent
    public void onPlayerEvent(TickEvent.ServerTickEvent event) {
        DenizenCore.tick(50); // Sadly, minecraft has no delta timing, so a tick is always 50ms.
        if ((tickTimer % (20 * 60 * 60) == 0) && Settings.canRecordStats()) {
            StatsRecord.trigger();
        }

        if (tickTimer % (20 * 60) == 0) {
            PlayerFlagHandler.cleanCache();
        }

        if ((tickTimer % (20 * 60 * 5) == 0) && (!StrongWarning.recentWarnings.isEmpty())) {
            StringBuilder warnText = new StringBuilder();
            warnText.append(ChatColor.YELLOW).append("[Denizen] ").append(ChatColor.RED).append("Recent strong system warnings, scripters need to address ASAP (check earlier console logs for details):");
            for (StrongWarning warning : StrongWarning.recentWarnings.keySet()) {
                warnText.append("\n- ").append(warning.message);
            }
            StrongWarning.recentWarnings.clear();
            Debug.log(warnText.toString());
            for (String playername : ServerLifecycleHooks.getCurrentServer().getPlayerList().getOps().getUserList()) {
                Player player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(playername);
                player.sendMessage(new TextComponent(warnText.toString()), player.getUUID());
            }
        }
//        fire(event);
    }
}
