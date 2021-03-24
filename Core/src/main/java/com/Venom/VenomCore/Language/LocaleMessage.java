package com.Venom.VenomCore.Language;

import com.Venom.VenomCore.Chat.Color;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
@SuppressWarnings("unused")
public class LocaleMessage {
    private String text;
    private String prefix;
    public LocaleMessage(String text, String prefix) {
        this.text = Color.translate(text);
        if (prefix != null) {
            this.prefix = Color.translate(prefix);
        }
    }

    /**
     * Sets all the placeholders in a given map.
     * @param map The placeholders.
     * @return The locale message with placeholders.
     */
    public LocaleMessage setPlaceholders(Map<String, String> map) {
        if (map == null)
            return this;

        for (Map.Entry<String, String> entr : map.entrySet()) {
            text = text.replace(entr.getKey(), entr.getValue());
        }

        return this;
    }

    /**
     * Sends this locale message to a player.
     * @param player The player to send the message to.
     * @param prefix Whether to add a prefix or not.
     */
    public void sendMessage(Player player, boolean prefix) {
        String message;
        if (prefix && this.prefix != null) {
            message = this.prefix + text;
        } else {
            message = text;
        }
        player.sendMessage(message);
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
        return Color.translate(text);
    }

    /**
     * Strips all the color in the string.
     * @return The string without colors.
     */
    public String toPlainText() {
        return ChatColor.stripColor(text);
    }
}
