package com.denizenscript.denizen.utilities.command.manager.messaging;

import com.denizenscript.denizen.utilities.ChatColor;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;

public class Messaging {

    public static void configure(String messageColour, String highlightColour) {
        MESSAGE_COLOUR = messageColour;
        HIGHLIGHT_COLOUR = highlightColour;
    }

    private static String prettify(String message) {
        String trimmed = message.trim();
        String messageColor = Colorizer.parseColors(MESSAGE_COLOUR);
        if (!trimmed.isEmpty()) {
            if (trimmed.charAt(0) == ChatColor.COLOR_CHAR) {
                ChatColor test = ChatColor.getByChar(trimmed.substring(1, 2));
                if (test == null) {
                    message = messageColor + message;
                }
            }
            else {
                message = messageColor + message;
            }
        }
        return message;
    }
    public static void send(Player sender, String msg) {
        sendMessageTo(sender, msg);
    }

    public static void sendInfo(Player sender, String msg) {
        send(sender, ChatColor.YELLOW + msg);
    }

    public static void sendError(Player sender, String msg) {
        send(sender, ChatColor.RED + msg);
    }

    private static void sendMessageTo(Player sender, String rawMessage) {
        if (sender != null) {
            //todo implement player
//            rawMessage = rawMessage.replace("<player>", sender.getName());
//            rawMessage = rawMessage.replace("<world>", sender.getWorld().getName());
        }
        rawMessage = Colorizer.parseColors(rawMessage);
        for (String message : rawMessage.split("<br>|<n>|\\n")) {
            sender.sendMessage(new TextComponent(prettify(message)), Util.NIL_UUID);
        }
    }

    private static String HIGHLIGHT_COLOUR = ChatColor.YELLOW.toString();

    private static String MESSAGE_COLOUR = ChatColor.GREEN.toString();
}
