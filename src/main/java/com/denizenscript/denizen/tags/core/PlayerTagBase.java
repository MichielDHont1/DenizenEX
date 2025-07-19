package com.denizenscript.denizen.tags.core;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.Settings;
import com.denizenscript.denizen.tags.BukkitTagContext;
import com.denizenscript.denizencore.tags.TagManager;


import java.util.*;

public class PlayerTagBase {

    public PlayerTagBase() {

        // <--[tag]
        // @attribute <player[(<player>)]>
        // @returns PlayerTag
        // @description
        // Returns a player object constructed from the input value.
        // Refer to <@link objecttype PlayerTag>.
        // If no input value is specified, returns the linked player.
        // -->
//        Bukkit.getServer().getPluginManager().registerEvents(this, Denizen.getInstance());
//        TagManager.registerTagHandler(PlayerTag.class, "player", (attribute) -> {
//            if (!attribute.hasParam()) {
//                PlayerTag player = ((BukkitTagContext) attribute.context).player;
//                if (player != null) {
//                    return player;
//                }
//                else {
//                    attribute.echoError("Missing player for player tag.");
//                    return null;
//                }
//            }
//            return PlayerTag.valueOf(attribute.getParam(), attribute.context);
//        });
    }

    ///////////
    // Player Chat History
    /////////

    public static Map<UUID, List<String>> playerChatHistory = new HashMap<>();
//
//    @EventHandler(priority = EventPriority.MONITOR)
//    public void addMessage(final AsyncPlayerChatEvent event) {
//        final int maxSize = Settings.chatHistoryMaxMessages();
//        if (maxSize > 0) {
//            Bukkit.getScheduler().runTaskLater(Denizen.getInstance(), () -> {
//                List<String> history = playerChatHistory.get(event.getPlayer().getUniqueId());
//                // If history hasn't been started for this player, initialize a new ArrayList
//                if (history == null) {
//                    history = new ArrayList<>();
//                }
//                // Maximum history size is specified by config.yml
//                if (history.size() > maxSize) {
//                    history.remove(maxSize - 1);
//                }
//                // Add message to history
//                history.add(0, event.getMessage());
//                // Store the new history
//                playerChatHistory.put(event.getPlayer().getUniqueId(), history);
//            }, 1);
//        }
//    }
}

