package com.denizenscript.denizen.utilities;

import com.denizenscript.denizen.Config;
import com.denizenscript.denizen.Denizen;
//import com.denizenscript.denizen.scripts.commands.entity.RemoveCommand;
import com.denizenscript.denizen.tags.core.CustomColorTagBase;
import com.denizenscript.denizen.utilities.flags.PlayerFlagHandler;
import com.denizenscript.denizencore.utilities.CoreConfiguration;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.ReflectionRefuse;
import com.denizenscript.denizencore.utilities.debugging.DebugSubmitter;

import java.nio.charset.Charset;

@ReflectionRefuse
public class Settings {

    public static void refillCache() {

        // Core
        CoreConfiguration.debugRecordingAllowed = true;
        CoreConfiguration.defaultDebugMode = Config.SHOW_DEBUG.get();
        CoreConfiguration.shouldShowDebug = CoreConfiguration.defaultDebugMode;
        CoreConfiguration.debugExtraInfo = Config.EXTRA_DEBUG.get();
        CoreConfiguration.debugVerbose = Config.VERBOSE_DEBUG.get();
        CoreConfiguration.debugLoadingInfo = Config.SHOW_LOADING_INFO.get();
        CoreConfiguration.deprecationWarningRate = Config.WARNING_RATE.get();
        CoreConfiguration.futureWarningsEnabled = Config.SHOW_FUTURE_WARNING.get();
        CoreConfiguration.allowLog = Config.ALLOW_LOGGING.get();
        CoreConfiguration.allowFileCopy = Config.ALLOW_FILECOPY.get();
        CoreConfiguration.allowWebget = Config.ALLOW_WEBGET.get();
        CoreConfiguration.allowSQL = Config.ALLOW_SQL.get();
        CoreConfiguration.allowRedis = Config.ALLOW_REDIS.get();
        CoreConfiguration.allowMongo = Config.ALLOW_MONGO.get();
        CoreConfiguration.whileMaxLoops = Config.MAX_WHILE_LOOPS.get();
        CoreConfiguration.tagTimeout = Config.TAG_TIMEOUT.get();
        CoreConfiguration.tagTimeoutUnsafe = Config.TAG_TIMEOUT_WHEN_UNSAFE.get();
        CoreConfiguration.tagTimeoutWhenSilent = Config.TAG_TIMEOUT_WHEN_SILENT.get();
        CoreConfiguration.scriptQueueSpeed = DurationTag.valueOf(Config.QUEUE_SPEED.get(), CoreUtilities.basicContext).getSeconds();
        CoreConfiguration.allowConsoleRedirection = Config.ALLOW_CONSOLE_REDIRECTION.get();
        CoreConfiguration.allowStrangeFileSaves = Config.ALLOW_SAVING_OUTSIDE.get();
        CoreConfiguration.skipAllFlagCleanings = Config.SKIP_FLAG_CLEANING.get();
        CoreConfiguration.allowRestrictedActions = Config.ALLOW_RESTRICTED_ACTIONS.get();
        CoreConfiguration.allowWebserver = Config.ALLOW_WEBSERVER.get();
        CoreConfiguration.webserverRoot = Config.WEBROOT.get();
        CoreConfiguration.allowFileRead = Config.ALLOW_FILE_READ.get();
        CoreConfiguration.allowFileWrite = Config.ALLOW_FILE_WRITE.get();
        CoreConfiguration.allowFileDeletion = Config.ALLOW_FILE_DELETE.get();
        CoreConfiguration.filePathLimit = Config.RESTRICTED_PATH.get();
        CoreConfiguration.verifyThreadMatches = Config.VERIFY_THREAD.get();
        CoreConfiguration.queueIdPrefix = Config.QUEUE_ID_PREFIX.get();
        CoreConfiguration.queueIdNumeric = Config.QUEU_ID_NUMERIC.get();
        CoreConfiguration.queueIdWords = Config.QUEUE_ID_WORDS.get();
        CoreConfiguration.listFlagsAllowed = Config.ALLOW_LIST_FLAGS.get();
        CoreConfiguration.allowReflectionFieldReads = Config.ALLOW_READING_FIELDS.get();
        CoreConfiguration.allowReflectedCoreMethods = Config.ALLOW_CORE_METHODS.get();
        CoreConfiguration.allowReflectionSet = Config.ALLOW_SET_COMMAND.get();
        CoreConfiguration.allowReflectionSetPrivate = Config.ALLOW_PRIVATE_FIELDS.get();
        CoreConfiguration.allowReflectionSetFinal = Config.ALLOW_FINAL_FIELDS.get();
        CoreConfiguration.debugLimitPerTick = Config.DEBUG_LIMIT.get();
        CoreConfiguration.debugTrimLength = Config.TRIM_LENGTH.get();
        CoreConfiguration.debugPrefix = Config.DEBUG_PREFIX.get();
        CoreConfiguration.debugLineLength = Config.DEBUG_LINE_LENGTH.get();
        DebugSubmitter.pasteURL = Config.PASTE_URL.get();
        if (DebugSubmitter.pasteURL.equals("default")) {
            DebugSubmitter.pasteURL = DebugSubmitter.corePasteURL;
        }
        String scriptEncoding = Config.SCRIPT_ENCODING.get();
        if (scriptEncoding.equalsIgnoreCase("default")) {
            CoreConfiguration.scriptEncoding = null;
        }
        else {
            try {
                CoreConfiguration.scriptEncoding = Charset.forName(scriptEncoding).newDecoder();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        //todo
//        PolygonTag.preferInclusive = config.getBoolean("Tags.Polygon default inclusive", false);
//        allowAsyncPassThrough = config.getBoolean("Scripts.Economy.Pass async to main thread", false);
//        skipChunkFlagCleaning = config.getBoolean("Saves.Skip chunk flag cleaning", false);
//        nullifySkullSkinIds = config.getBoolean("Tags.Nullify skull skin ids", false);
//        worldPlayerDataSaveDelay = (float) DurationTag.valueOf(config.getString("Save world player file delay", "10s"), CoreUtilities.basicContext).getSeconds();
//        worldPlayerDataMaxCacheTicks = DurationTag.valueOf(config.getString("World player data max cache", "1h"), CoreUtilities.basicContext).getTicks();
//        cache_overrideHelp = config.getBoolean("Debug.Override help", true);
//        cache_useDefaultScriptPath = config.getBoolean("Scripts location.Use default script folder", true);
//        cache_showExHelp = config.getBoolean("Debug.Ex command help", true);
//        cache_showExDebug = config.getBoolean("Debug.Ex command debug", true);
//        cache_getAlternateScriptPath = config.getString("Scripts location.Alternative folder path", "plugins/Denizen");
//        cache_canRecordStats = config.getBoolean("Debug.Stats", true);
//        cache_defaultDebugMode = config.getBoolean("Debug.Container default", true);
//        cache_warnOnAsyncPackets = config.getBoolean("Debug.Warn on async packets", false);
//        cache_interactQueueSpeed = config.getString("Scripts.Interact.Queue speed", "0.5s");
//        cache_healthTraitEnabledByDefault = config.getBoolean("Traits.Health.Enabled", false);
//        cache_healthTraitRespawnEnabled = config.getBoolean("Traits.Health.Respawn.Enabled", true);
//        cache_healthTraitAnimatedDeathEnabled = config.getBoolean("Traits.Health.Animated death.Enabled", true);
//        cache_healthTraitRespawnDelay = config.getString("Traits.Health.Respawn.Delay", "10s");
//        cache_healthTraitBlockDrops = config.getBoolean("Traits.Health.Block drops", false);
//        cache_engageTimeoutInSeconds = config.getString("Commands.Engage.Timeout", "150s");
//        cache_createWorldSymbols = config.getBoolean("Commands.CreateWorld.Allow symbols in names", false);
//        cache_createWorldWeirdPaths = config.getBoolean("Commands.CreateWorld.Allow weird paths", false);
//        cache_allowServerStop = config.getBoolean("Commands.Restart.Allow server stop", false);
//        cache_allowServerRestart = config.getBoolean("Commands.Restart.Allow server restart", true);
//        cache_limitPath = config.getString("Commands.Yaml.Limit path", "none");
//        cache_chatMultipleTargetsFormat = config.getString("Commands.Chat.Options.Multiple targets format", "%target%, %target%, %target%, and others");
//        cache_chatBystandersRange = config.getDouble("Commands.Chat.Options.Range for bystanders", 5.0);
//        cache_chatNoTargetFormat = config.getString("Commands.Chat.Formats.No target", "[<[talker].name>]: <[message]>");
//        cache_chatToTargetFormat = config.getString("Commands.Chat.Formats.To target", "[<[talker].name>] -> You: <[message]>");
//        cache_chatWithTargetToBystandersFormat = config.getString("Commands.Chat.Formats.With target to bystanders", "[<[talker].name>] -> <[target].name>: <[message]>");
//        cache_chatWithTargetsToBystandersFormat = config.getString("Commands.Chat.Formats.With targets to bystanders", "[<[talker].name>] -> [<[targets]>]: <[message]>");
//        cache_chatAsynchronous = config.getBoolean("Triggers.Chat.Use asynchronous event", false);
//        cache_chatToNpcFormat = config.getString("Triggers.Chat.Formats.Player to NPC", "You -> <npc.nickname>: <text>");
//        cache_chatToNpcOverheardFormat = config.getString("Triggers.Chat.Formats.Player to NPC overheard", "<player.name> -> <npc.nickname>: <text>");
//        cache_chatToNpcOverhearingRange = config.getDouble("Triggers.Chat.Overhearing range", 4);
//        cache_chatMustSeeNPC = config.getBoolean("Triggers.Chat.Prerequisites.Must be able to see NPC", true);
//        cache_chatMustLookAtNPC = config.getBoolean("Triggers.Chat.Prerequisites.Must be looking in direction of NPC", true);
//        cache_chatGloballyIfFailedChatTriggers = config.getBoolean("Triggers.Chat.Appears globally.If triggers failed", false);
//        cache_chatGloballyIfNoChatTriggers = config.getBoolean("Triggers.Chat.Appears globally.If triggers missing", true);
//        cache_chatGloballyIfUninteractable = config.getBoolean("Triggers.Chat.Appears globally.If NPC uninteractable", true);
//        cache_worldScriptChatEventAsynchronous = config.getBoolean("Scripts.World.Events.On player chats.Use asynchronous event", false);
//        cache_worldScriptTimeEventFrequency = DurationTag.valueOf(config.getString("Scripts.World.Events.On time changes.Frequency of check", "250t"), CoreUtilities.basicContext);
//        cache_blockTagsMaxBlocks = config.getInt("Tags.Block tags.Max blocks", 1000000);
//        cache_chatHistoryMaxMessages = config.getInt("Tags.Chat history.Max messages", 10);
//        cache_packetInterception = config.getBoolean("Packets.Interception", true);
//        cache_packetInterceptAutoInit = config.getBoolean("Packets.Auto init", false);
//        cache_commandScriptAutoInit = config.getBoolean("Scripts.Command.Auto init", false);
//        cache_legacySpigotNamesSupport = config.getBoolean("Scripts.Support legacy Spigot names", true);
//        PlayerFlagHandler.cacheTimeoutSeconds = config.getLong("Saves.Offline player cache timeout", 300);
//        PlayerFlagHandler.asyncPreload = config.getBoolean("Saves.Load async on login", true);
//        PlayerFlagHandler.saveOnlyWhenWorldSaveOn = config.getBoolean("Saves.Only save if world save is on", false);
//        RemoveCommand.alwaysWarnOnMassRemove = config.getBoolean("Commands.Remove.Always warn on mass delete", false);
//        ConfigurationSection colorSection = config.getConfigurationSection("Colors");
//        if (colorSection != null) {
//            CustomColorTagBase.customColors.clear();
//            CustomColorTagBase.defaultColor = null;
//            for (String key : colorSection.getKeys(false)) {
//                CustomColorTagBase.customColorsRaw.put(CoreUtilities.toLowerCase(key), colorSection.getString(key));
//            }
//            CustomColorTagBase.defaultColorRaw = CustomColorTagBase.customColorsRaw.getOrDefault("default", CustomColorTagBase.defaultColorRaw);
//        }
    }

    public static boolean skipChunkFlagCleaning = false;

    public static boolean nullifySkullSkinIds = false;

    public static boolean allowAsyncPassThrough = false;

    public static float worldPlayerDataSaveDelay = 10;

    public static long worldPlayerDataMaxCacheTicks = 20 * 60 * 60;

    public static boolean cache_overrideHelp,
            cache_showExHelp, cache_showExDebug, cache_canRecordStats,
            cache_defaultDebugMode, cache_healthTraitEnabledByDefault, cache_healthTraitAnimatedDeathEnabled,
            cache_healthTraitRespawnEnabled, cache_allowServerStop, cache_allowServerRestart,
            cache_healthTraitBlockDrops, cache_chatAsynchronous, cache_chatMustSeeNPC, cache_chatMustLookAtNPC,
            cache_chatGloballyIfFailedChatTriggers, cache_chatGloballyIfNoChatTriggers,
            cache_chatGloballyIfUninteractable, cache_worldScriptChatEventAsynchronous,
            cache_packetInterception, cache_createWorldSymbols, cache_createWorldWeirdPaths,
            cache_commandScriptAutoInit, cache_packetInterceptAutoInit, cache_warnOnAsyncPackets, cache_legacySpigotNamesSupport;

    public static volatile boolean cache_useDefaultScriptPath;
    public static volatile String cache_getAlternateScriptPath;

    public static String cache_healthTraitRespawnDelay,
            cache_engageTimeoutInSeconds, cache_chatMultipleTargetsFormat, cache_chatNoTargetFormat,
            cache_chatToTargetFormat, cache_chatWithTargetToBystandersFormat, cache_chatWithTargetsToBystandersFormat,
            cache_chatToNpcFormat, cache_chatToNpcOverheardFormat, cache_interactQueueSpeed, cache_limitPath;

    public static int cache_blockTagsMaxBlocks, cache_chatHistoryMaxMessages;

    public static double cache_chatBystandersRange, cache_chatToNpcOverhearingRange;

    public static DurationTag cache_worldScriptTimeEventFrequency;

    public static boolean useDefaultScriptPath() {
        return cache_useDefaultScriptPath;
    }

    public static String getAlternateScriptPath() {
        return cache_getAlternateScriptPath;
    }

    public static boolean overrideHelp() {
        return cache_overrideHelp;
    }

    public static boolean showExHelp() {
        return cache_showExHelp;
    }

    public static boolean showExDebug() {
        return cache_showExDebug;
    }

    public static boolean canRecordStats() {
        return cache_canRecordStats;
    }

    public static String interactQueueSpeed() {
        return cache_interactQueueSpeed;
    }

    public static boolean healthTraitEnabledByDefault() {
        return cache_healthTraitEnabledByDefault;
    }

    public static boolean healthTraitBlockDrops() {
        return cache_healthTraitBlockDrops;
    }

    public static boolean healthTraitRespawnEnabled() {
        return cache_healthTraitRespawnEnabled;
    }

    public static boolean healthTraitAnimatedDeathEnabled() {
        return cache_healthTraitAnimatedDeathEnabled;
    }

    public static String healthTraitRespawnDelay() {
        return cache_healthTraitRespawnDelay;
    }

    /**
     * Whether a certain trigger is enabled by default or not
    */
//    public static boolean triggerEnabled(String triggerName) {
//        return Denizen.getInstance().getConfig()
//                .getBoolean("Triggers." + String.valueOf(triggerName.charAt(0)).toUpperCase()
//                        + CoreUtilities.toLowerCase(triggerName.substring(1)) + ".Enabled", true);
//    }
//
//    /**
//     * Default duration of cooldown set to Denizens for when a trigger is
//     * triggered. Not all triggers may use this, it is optional!
//    */
//    public static double triggerDefaultCooldown(String triggerName) {
//        return DurationTag.valueOf(Denizen.getInstance().getConfig()
//                .getString("Triggers." + String.valueOf(triggerName.charAt(0)).toUpperCase()
//                        + CoreUtilities.toLowerCase(triggerName.substring(1)) + ".Cooldown", "5s"), CoreUtilities.basicContext).getSeconds();
//    }
//
//    /*
//     * This set of nodes defines ranges for different types of
//     * interact-script triggers. Not all triggers use a range,
//     * as it may not be applicable to the trigger.
//    */
//
//    public static double triggerDefaultRange(String triggerName) {
//        return Denizen.getInstance().getConfig()
//                .getDouble("Triggers." + String.valueOf(triggerName.charAt(0)).toUpperCase()
//                        + CoreUtilities.toLowerCase(triggerName.substring(1)) + ".Range", -1);
//    }

    /**
     * Default engage timeout. When NPCs are set to ENGAGE, this is
     * the default timeout that they will auto-DISENGAGE if not otherwise
     * specified. (Default, 150 seconds)
    */
    public static String engageTimeoutInSeconds() {
        return cache_engageTimeoutInSeconds;
    }

    public static boolean allowStupids() {
        return allowStupid1() && allowStupid2() && allowStupid3();
    }

    public static boolean allowStupid1() {
        // Unrestricted file access can cause a lot of problems in itself, and encourage a style of script
        // writing that is extremely poor and can be done in much more effective and clean ways.
        // If you believe you need to make use of this config option... strongly consider any possible alternatives.
        //
        // Generally, be aware that if you are not completely clear on exactly how these settings work internally in Java,
        // and what changing them can do, ... you just should not use them.
        // This is for very highly experienced users only.
        return Config.ALLOW_STUPID1.get();
    }

    public static boolean allowStupid2() {
        return Config.ALLOW_STUPID2.get();
    }

    public static boolean allowStupid3() {
        return Config.ALLOW_STUPID3.get();
    }

    public static boolean allowStupidx() {
        return Config.ALLOW_STUPIDX.get();
    }

    public static boolean allowServerStop() {
        return cache_allowServerStop;
    }

    public static boolean allowServerRestart() {
        return cache_allowServerRestart;
    }

    public static String fileLimitPath() {
        return cache_limitPath;
    }

    public static String chatMultipleTargetsFormat() {
        return cache_chatMultipleTargetsFormat;
    }

    public static double chatBystandersRange() {
        return cache_chatBystandersRange;
    }

    public static String chatNoTargetFormat() {
        return cache_chatNoTargetFormat;
    }

    public static String chatToTargetFormat() {
        return cache_chatToTargetFormat;
    }

    public static String chatWithTargetToBystandersFormat() {
        return cache_chatWithTargetToBystandersFormat;
    }

    public static String chatWithTargetsToBystandersFormat() {
        return cache_chatWithTargetsToBystandersFormat;
    }

    /**
     * Whether the Chat Trigger should use an asynchronous Bukkit
     * event or not
    */
    public static boolean chatAsynchronous() {
        return cache_chatAsynchronous;
    }

    /*
     * The formats in which Chat Trigger input from players appears to
     * themselves and to players who can overhear them
    */

    public static String chatToNpcFormat() {
        return cache_chatToNpcFormat;
    }

    public static String chatToNpcOverheardFormat() {
        return cache_chatToNpcOverheardFormat;
    }

    /**
     * The distance from which a player chatting to an NPC can be overheard
     * by other players
    */
    public static double chatToNpcOverhearingRange() {
        return cache_chatToNpcOverhearingRange;
    }

    /*
     * Prerequisites for triggering a Chat Trigger
    */

    public static boolean chatMustSeeNPC() {
        return cache_chatMustSeeNPC;
    }

    public static boolean chatMustLookAtNPC() {
        return cache_chatMustLookAtNPC;
    }

    /*
     * Circumstances under which a player's Chat Trigger input should
     * appear in the global chat
    */

    public static boolean chatGloballyIfFailedChatTriggers() {
        return cache_chatGloballyIfFailedChatTriggers;
    }

    public static boolean chatGloballyIfNoChatTriggers() {
        return cache_chatGloballyIfNoChatTriggers;
    }

    public static boolean chatGloballyIfUninteractable() {
        return cache_chatGloballyIfUninteractable;
    }

    /**
     * Whether the "on player chats" world event should use an
     * asynchronous Bukkit event or not
    */
    public static boolean worldScriptChatEventAsynchronous() {
        return cache_worldScriptChatEventAsynchronous;
    }

    /**
     * The frequency with which the "on time changes" world script
     * event will be checked
    */
    public static DurationTag worldScriptTimeEventFrequency() {
        return cache_worldScriptTimeEventFrequency;
    }

    public static int blockTagsMaxBlocks() {
        return cache_blockTagsMaxBlocks;
    }

    public static int chatHistoryMaxMessages() {
        return cache_chatHistoryMaxMessages;
    }

    public static boolean packetInterception() {
        return cache_packetInterception;
    }
}
