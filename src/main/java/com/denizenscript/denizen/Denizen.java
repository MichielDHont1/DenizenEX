package com.denizenscript.denizen;

import com.denizenscript.denizen.events.ScriptEventRegistry;
//import com.denizenscript.denizen.events.bukkit.SavesReloadEvent;
//import com.denizenscript.denizen.events.server.ServerPrestartScriptEvent;
//import com.denizenscript.denizen.events.server.ServerStartScriptEvent;
//import com.denizenscript.denizen.nms.NMSHandler;
//import com.denizenscript.denizen.nms.interfaces.FakeArrow;
//import com.denizenscript.denizen.nms.interfaces.FakePlayer;
//import com.denizenscript.denizen.nms.interfaces.ItemProjectile;
//import com.denizenscript.denizen.npc.DenizenNPCHelper;
//import com.denizenscript.denizen.npc.TraitRegistry;
//import com.denizenscript.denizen.objects.InventoryTag;
//import com.denizenscript.denizen.objects.NPCTag;
//import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.events.player.PlayerBreaksBlockScriptEvent;
import com.denizenscript.denizen.events.player.PlayerClicksBlockScriptEvent;
import com.denizenscript.denizen.objects.properties.PropertyRegistry;
import com.denizenscript.denizen.scripts.commands.BukkitCommandRegistry;
//import com.denizenscript.denizen.scripts.commands.player.ClickableCommand;
import com.denizenscript.denizen.scripts.containers.ContainerRegistry;
import com.denizenscript.denizen.scripts.containers.core.*;
import com.denizenscript.denizen.scripts.triggers.TriggerRegistry;
import com.denizenscript.denizen.scripts.triggers.core.ChatTrigger;
import com.denizenscript.denizen.tags.BukkitTagContext;
//import com.denizenscript.denizen.tags.core.NPCTagBase;
import com.denizenscript.denizen.utilities.*;
//import com.denizenscript.denizen.utilities.blocks.FullBlockData;
import com.denizenscript.denizen.utilities.command.*;
import com.denizenscript.denizen.utilities.command.manager.CommandManager;
import com.denizenscript.denizen.utilities.command.manager.Injector;
import com.denizenscript.denizen.utilities.command.manager.messaging.Messaging;
//import com.denizenscript.denizen.utilities.debugging.BStatsMetricsLite;
//import com.denizenscript.denizen.utilities.debugging.DebugSubmit;
//import com.denizenscript.denizen.utilities.debugging.StatsRecord;
//import com.denizenscript.denizen.utilities.entity.DenizenEntityType;
import com.denizenscript.denizen.utilities.flags.PlayerFlagHandler;
import com.denizenscript.denizen.utilities.flags.WorldFlagHandler;
import com.denizenscript.denizen.utilities.implementation.DenizenCoreImplementation;
//import com.denizenscript.denizen.utilities.maps.DenizenMapManager;
import com.denizenscript.denizen.utilities.packets.NetworkInterceptHelper;
//import com.denizenscript.denizen.utilities.world.VoidGenerator;
//import com.denizenscript.denizen.utilities.world.WorldListChangeTracker;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.core.SecretTag;
import com.denizenscript.denizencore.objects.core.TimeTag;
import com.denizenscript.denizencore.objects.notable.NoteManager;
import com.denizenscript.denizencore.scripts.ScriptHelper;
import com.denizenscript.denizencore.scripts.commands.queue.RunLaterCommand;
import com.denizenscript.denizencore.utilities.CoreConfiguration;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.utilities.debugging.DebugInternals;
import com.denizenscript.denizencore.utilities.debugging.StrongWarning;
//import com.denizenscript.denizencore.utilities.text.ConfigUpdater;
import net.minecraft.server.players.GameProfileCache;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
//import com.denizenscript.denizen.nms.abstracts.OfflinePlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.io.*;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("denizenex")
public class Denizen {
    private static final Logger LOGGER = LogManager.getLogger();
    public static Denizen instance;
    public static final IEventBus DENIZEN_EVENTBUS = BusBuilder.builder().startShutdown().build();

    public Denizen()
    {
        //todo move events to
        Denizen.DENIZEN_EVENTBUS.register(new ItemScriptHelper());
        MinecraftForge.EVENT_BUS.register(new PlayerBreaksBlockScriptEvent());
        MinecraftForge.EVENT_BUS.register(new PlayerClicksBlockScriptEvent());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, "denizen-config.toml");
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static Denizen getInstance() {
        return instance;
    }

    public static boolean hasTickedOnce = false;

    public static String versionTag = null;
    private boolean startedSuccessful = false;

    public static boolean supportsPaper = false;
    public CommandManager commandManager;

    public TriggerRegistry triggerRegistry;
//
//    public BukkitWorldScriptHelper worldScriptHelper;
//
//    public ItemScriptHelper itemScriptHelper;

    public ExCommandHandler exCommand;

    public DenizenCoreImplementation coreImplementation = new DenizenCoreImplementation();

    /*
     * Sets up Denizen on start of the CraftBukkit server.
     */

    private void setup(final FMLCommonSetupEvent event){
        long startTime = System.currentTimeMillis();
        instance = this;
        try {
            versionTag = ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().getQualifier();


            CoreUtilities.noDebugContext = new BukkitTagContext(null, /*null,*/ null, false, null);
            CoreUtilities.noDebugContext.showErrors = () -> false;
            CoreUtilities.basicContext = new BukkitTagContext(null, /*null,*/  null, true, null);
            CoreUtilities.errorButNoDebugContext = new BukkitTagContext(null, /*null,*/  null, false, null);
            // Load Denizen's core
            DenizenCore.init(coreImplementation);
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        PlayerFlagHandler.dataFolder = new File(".", "player_flags");
        if (!PlayerFlagHandler.dataFolder.exists()) {
            PlayerFlagHandler.dataFolder.mkdir();
        }
        DebugInternals.alternateTrimLogic = FormattedTextHelper::bukkitSafeDebugTrimming;
        String javaVersion = System.getProperty("java.version");
        Debug.log("Running on java version: " + javaVersion);
        if (javaVersion.startsWith("8") || javaVersion.startsWith("1.8") || javaVersion.startsWith("9") || javaVersion.startsWith("1.9")
                || javaVersion.startsWith("10") || javaVersion.startsWith("1.10") || javaVersion.startsWith("11")
                || javaVersion.startsWith("12") || javaVersion.startsWith("13") || javaVersion.startsWith("14") || javaVersion.startsWith("15")) {
            Debug.log("Running on outdated Java version somehow. Denizen requires Java 16+ or newer to function.");
        }
        else if (javaVersion.startsWith("16")) {
            Debug.log("Running on fully supported Java 16.");
        }
        else if (javaVersion.startsWith("17")) {
            Debug.log("Running on fully supported Java 17.");
        }
        else if (javaVersion.startsWith("18") || javaVersion.startsWith("19")) {
            LOGGER.debug("Running unreliable Java version. modern Minecraft versions are built for Java 21 or 17. Other Java versions are not guaranteed to function properly.");
        }
        else if (javaVersion.startsWith("21")) {
            Debug.log("Running on fully supported Java 21.");
        }
        else {
            Debug.log("Running on unrecognized (future?) Java version. May or may not work.");
        }
//        if (!NMSHandler.instance.isCorrectMappingsCode()) {
//            LOGGER.warn("-------------------------------------");
//            LOGGER.warn("This build of Denizen was built for a different Spigot revision! This may potentially cause issues."
//                    + " If you are experiencing trouble, update Denizen and Spigot both to latest builds!"
//                    + " If this message appears with both Denizen and Spigot fully up-to-date, contact the Denizen team (via GitHub, Spigot, or Discord) to request an update be built.");
//            LOGGER.warn("-------------------------------------");
//        }
        triggerRegistry = new TriggerRegistry();
        boolean citizensBork = false;
        try {
            // Populate config.yml if it doesn't yet exist.
            reloadConfig();
            // Startup procedure
            Debug.log(ChatColor.LIGHT_PURPLE + "+-------------------------+");
            Debug.log(ChatColor.YELLOW + " Denizen " + ChatColor.GRAY + " scriptable minecraft");
            Debug.log("");
            Debug.log(ChatColor.GRAY + "by:" + ChatColor.WHITE + " The DenizenScript team");
            Debug.log(ChatColor.GRAY + "Chat with us at:" + ChatColor.WHITE + " https://discord.gg/Q6pZGSR");
            Debug.log(ChatColor.GRAY + "Or learn more at:" + ChatColor.WHITE + " https://denizenscript.com");
            Debug.log(ChatColor.GRAY + "version: " + ChatColor.WHITE + versionTag);
            Debug.log(ChatColor.LIGHT_PURPLE + "+-------------------------+");
        }
        catch (Exception e) {
            Debug.echoError(e);
        }
        try {
            if (Class.forName("com.destroystokyo.paper.PaperConfig") != null) {
                supportsPaper = true;
            }
        }
        catch (ClassNotFoundException ex) {
            // Ignore.
        }
        catch (Throwable ex) {
            Debug.echoError(ex);
        }
        try {
            //todo debug
//            DebugSubmit.init();

            // Create our CommandManager to handle '/denizen' commands
            commandManager = new CommandManager();
            commandManager.setInjector(new Injector(this));
            commandManager.register(DenizenCommandHandler.class);

//            DenizenEntityType.registerEntityType("ITEM_PROJECTILE", ItemProjectile.class);
//            DenizenEntityType.registerEntityType("FAKE_ARROW", FakeArrow.class);
//            DenizenEntityType.registerEntityType("FAKE_PLAYER", FakePlayer.class);
            // Track all player names for quick PlayerTag matching
            //todo get list of offline players
//            for (var info : infolist ) {
//                OfflinePlayer player = info.getProfile()
//                PlayerTag.notePlayer(player);
//            }
        }
        catch (Exception e) {
            Debug.echoError(e);
        }
        try {
            BukkitCommandRegistry.registerCommands();
        }
        catch (Exception e) {
            Debug.echoError(e);
        }
        try {
            ContainerRegistry.registerMainContainers();
        }
        catch (Exception e) {
            Debug.echoError(e);
        }
        try {
            // Ensure the Scripts and Midi folder exist
            new File(getDataFolder() + "/scripts").mkdirs();
            new File(getDataFolder() + "/midi").mkdirs();
            new File(getDataFolder() + "/schematics").mkdirs();
            // Ensure the example Denizen.mid sound file is available
            if (!new File(getDataFolder() + "/midi/Denizen.mid").exists()) {
                String sourceFile = URLDecoder.decode(Denizen.class.getProtectionDomain().getCodeSource().getLocation().getFile());
                Debug.log("Denizen.mid not found, extracting from " + sourceFile);
                Utilities.extractFile(new File(sourceFile), "Denizen.mid", getDataFolder() + "/midi/");
            }
        }
        catch (Exception e) {
            Debug.echoError(e);
        }
        try {
            // Automatic config file update
            InputStream properConfig = Denizen.class.getResourceAsStream("/config.yml");
            String properConfigString = ScriptHelper.convertStreamToString(properConfig);
            properConfig.close();
            FileInputStream currentConfig = new FileInputStream(getDataFolder() + "/config.yml");
            String currentConfigString = ScriptHelper.convertStreamToString(currentConfig);
            currentConfig.close();
//            String updated = ConfigUpdater.updateConfig(currentConfigString, properConfigString);
//            if (updated != null) {
//                Debug.log("Your config file is outdated. Automatically updating it...");
//                FileOutputStream configOutput = new FileOutputStream(getDataFolder() + "/config.yml");
//                OutputStreamWriter writer = new OutputStreamWriter(configOutput);
//                writer.write(updated);
//                writer.close();
//                configOutput.close();
//                reloadConfig();
//            }
        }
        catch (Exception e) {
            Debug.echoError(e);
        }
        try {
//            worldScriptHelper = new BukkitWorldScriptHelper();
//            itemScriptHelper = new ItemScriptHelper();
//            new InventoryScriptHelper();
//            new EntityScriptHelper();
            new CommandScriptHelper();
        }
        catch (Exception e) {
            Debug.echoError(e);
        }
//        try {
//            if (Depends.citizens != null) {
//                // Register traits
//                TraitRegistry.registerMainTraits();
//            }
//        }
//        catch (Exception e) {
//            Debug.echoError(e);
//        }
        // Register Core Members in the Denizen Registries
//        try {
//            if (Depends.citizens != null) {
//                triggerRegistry.registerCoreMembers();
//            }
//        }
//        catch (Exception e) {
//            Debug.echoError(e);
//        }
        try {
            ScriptEventRegistry.registerMainEvents();
            CommonRegistries.registerMainObjects();
            CommonRegistries.registerMainTagHandlers();
        }
        catch (Exception e) {
            Debug.echoError(e);
        }
        try {
            // Initialize all properties
            PropertyRegistry.registerMainProperties();
        }
        catch (Exception e) {
            Debug.echoError(e);
        }
//        try {
//            new CommandEvents();
//            if (Settings.cache_packetInterceptAutoInit) {
//                NetworkInterceptHelper.enable();
//            }
//        }
//        catch (Exception e) {
//            Debug.echoError(e);
//        }
        Debug.log("Loaded <A>" + DenizenCore.commandRegistry.instances.size() + "<W> core commands and <A>" + ObjectFetcher.objectsByPrefix.size() + "<W> core object types, at <A>" + (System.currentTimeMillis() - startTime) + "<W>ms from start.");
                exCommand = new ExCommandHandler();
//        exCommand.enableFor(getCommand("ex"));
        ExSustainedCommandHandler exsCommand = new ExSustainedCommandHandler();
//        exsCommand.enableFor(getCommand("exs"));
//        FullBlockData.init();
        // Load script files without processing.
        DenizenCore.preloadScripts(false, null);
        // Load the saves.yml into memory
        reloadSaves();
        Debug.log("Final full init took <A>" + (System.currentTimeMillis() - startTime) + "<W>ms.");
        final boolean hadCitizensBork = citizensBork;
        // Run everything else on the first server tick

        try {
            // Process script files (events, etc).
            NoteManager.reload();
            DenizenCore.postLoadScripts();
            Debug.log(ChatColor.LIGHT_PURPLE + "+-------------------------+");
            if (Settings.allowStupidx()) {
                Debug.echoError("Don't screw with bad config values.");
                throw new RuntimeException("Don't screw with bad config values.");
            }
            //todo inventory
//            InventoryTag.setupInventoryTracker();
//            if (!CoreConfiguration.skipAllFlagCleanings && !Settings.skipChunkFlagCleaning) {
//                BukkitWorldScriptHelper.cleanAllWorldChunkFlags();
//            }
            //todo figure out what this does
//            Bukkit.getPluginManager().registerEvents(new PlayerFlagHandler(), this);
            Debug.log("Denizen fully loaded at: " + TimeTag.now().format());
        }
        catch (Throwable ex) {
            Debug.echoError(ex);
        }
    }

    public boolean hasDisabled = false;

    /*
     * Unloads Denizen on shutdown of the server.
     */
    //todo move to event
//    public void onDisable() {
//        if (!startedSuccessful) {
//            return;
//        }
//        if (hasDisabled) {
//            return;
//        }
//        hasDisabled = true;
//        DenizenCore.shutdown();
//        ScoreboardHelper._saveScoreboards();
//        InventoryScriptHelper.savePlayerInventories();
//        triggerRegistry.disableCoreMembers();
//        LOGGER.log(Level.INFO, " v" + getDescription().getVersion() + " disabled.");
//        Bukkit.getServer().getScheduler().cancelTasks(this);
//        HandlerList.unregisterAll(this);
//        saveSaves(true);
//        worldFlags.shutdown();
//    }
    //todo move to event
    public void reloadConfig() {
        Settings.refillCache();
        SecretTag.load();
        if (!CoreConfiguration.defaultDebugMode) {
            LOGGER.warn("Debug is disabled in the Denizen config. This is almost always a mistake, and should not be done in the majority of cases.");
        }
    }
//todo yaml files
//    private FileConfiguration scoreboardsConfig = null;
    private File scoreboardsConfigFile = null;

    public WorldFlagHandler worldFlags;

    public void reloadSaves() {
//        if (scoreboardsConfigFile == null) {
//            scoreboardsConfigFile = new File(getDataFolder(), "scoreboards.yml");
//        }
//        scoreboardsConfig = YamlConfiguration.loadConfiguration(scoreboardsConfigFile);
//        // Reload scoreboards from scoreboards.yml
//        ScoreboardHelper._recallScoreboards();
//        // Load maps from maps.yml
//        DenizenMapManager.reloadMaps();
//        DenizenCore.reloadSaves();
//        if (worldFlags == null) {
//            worldFlags = new WorldFlagHandler();
//        }
//        worldFlags.shutdown();
//        worldFlags.init();
//        RunLaterCommand.init(new File(getDataFolder(), "run_later.yml").getPath());
//        if (new File(getDataFolder(), "saves.yml").exists()) {
//            LegacySavesUpdater.updateLegacySaves();
//        }
//        Bukkit.getServer().getPluginManager().callEvent(new SavesReloadEvent());
    }

//    public FileConfiguration getScoreboards() {
//        if (scoreboardsConfig == null) {
//            reloadSaves();
//        }
//        return scoreboardsConfig;
//    }

    public String getDataFolder()
    {
        return ".";
    }
    /**
     * Immediately saves all non-core save data.
     * @param lockUntilDone 'true' if the system should sleep and lock the thread until saves are complete. 'false' is saves can happen in the future.
     */
    public void saveSaves(boolean lockUntilDone) {
//        // Save scoreboards to scoreboards.yml
//        //todo
////        ScoreboardHelper._saveScoreboards();
//        // Save maps to maps.yml
//        //todo
////        DenizenMapManager.saveMaps();
//        InventoryScriptHelper.savePlayerInventories();
//        // Save server flags
//        try {
//            scoreboardsConfig.save(scoreboardsConfigFile);
//        }
//        catch (IOException ex) {
//            Debug.log("Could not save to " + scoreboardsConfigFile);
//        }
//        PlayerFlagHandler.saveAllNow(lockUntilDone);
//        worldFlags.saveAll(lockUntilDone);
//        RunLaterCommand.saveToFile(!lockUntilDone);
    }

//    @Override
//    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
//        if (cmd.getName().equals("denizenclickable")) {
//            if (!(sender instanceof Player)) {
//                return false;
//            }
//            if (args.length >= 2 && CoreUtilities.equalsIgnoreCase(args[0], "chat")) {
//                ChatTrigger.instance.chatTriggerInternal(new PlayerChatEvent((Player) sender, Arrays.stream(args).skip(1).collect(Collectors.joining(" "))));
//                return true;
//            }
//            if (args.length != 1) {
//                return false;
//            }
//            UUID id;
//            try {
//                id = UUID.fromString(args[0]);
//            }
//            catch (IllegalArgumentException ex) {
//                return false;
//            }
//            ClickableCommand.runClickable(id, (Player) sender);
//            return true;
//        }
//        String modifier = args.length > 0 ? args[0] : "";
//        if (!commandManager.hasCommand(cmd, modifier) && !modifier.isEmpty()) {
//            return suggestClosestModifier(sender, cmd.getName(), modifier);
//        }
//
//        Object[] methodArgs = {sender};
//        return commandManager.executeSafe(cmd, args, sender, methodArgs);
//    }

//    @Override
//    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] strings) {
//        if (alias.equals("denizen")) {
//            return commandManager.onTabComplete(commandSender, command, alias, strings);
//        }
//        return null;
//    }
//
//    private boolean suggestClosestModifier(CommandSender sender, String command, String modifier) {
//        String closest = commandManager.getClosestCommandModifier(command, modifier);
//        if (!closest.isEmpty()) {
//            Messaging.send(sender, "<7>Unknown command. Did you mean:");
//            Messaging.send(sender, " /" + command + " " + closest);
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
//        return switch (CoreUtilities.toLowerCase(id)) {
//            case "void" -> new VoidGenerator(true);
//            case "void_biomes" -> new VoidGenerator(false);
//            default -> null;
//        };
//    }


}
