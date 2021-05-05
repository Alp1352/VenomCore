package com.venom.venomcore.plugin.language;

import com.venom.venomcore.plugin.chat.Color;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;
@SuppressWarnings("unused")
public class LocaleMessage {
    private String text;
    private final String prefix;
    public LocaleMessage(String text, String prefix) {
        this.text = Color.translate(text);
        this.prefix = prefix != null ? Color.translate(prefix) : null;
    }

    /**
     * Sets all the placeholders in a given map.
     * @param map The placeholders.
     * @return The locale message with placeholders.
     */
    public LocaleMessage setPlaceholders(Map<String, String> map) {
        if (map == null || map.isEmpty())
            return this;

        for (Map.Entry<String, String> entr : map.entrySet()) {
            text = text.replace(entr.getKey(), entr.getValue());
        }

        return this;
    }

    /**
     * Sends this locale message to a player.
     * @param sender The recepient to send the message to.
     */
    public void send(CommandSender sender) {
        String message = text;
        if (this.prefix != null) {
            message = this.prefix + text;
        }


        sender.sendMessage(Color.translate(message));
    }

    /**
     * Gets the string without any modifications.
     * @return The string.
     */
    @Override
    public String toString() {
        return text;
    }

    /**
     * Gets the string with colors.
     * @return The string with colors.
     */
    public String toColorizedText() {
        return prefix != null ? (prefix + text) : text;
    }

    /**
     * Strips all the color in the string.
     * @return The string without colors.
     */
    public String toPlainText() {
        return ChatColor.stripColor(text);
    }
}
