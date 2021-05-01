package com.venom.venomcore.plugin.chat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Alp Beji
 * A class for clickable messages.
 * Supports 1.8.8 - 1.16.5
 */
public class CustomMessage {
    private final TextComponent text;
    public CustomMessage(String text) {
        this.text = new TextComponent(text);
    }

    /**
     * Sets the click action for the message.
     * @param action The action.
     * @param value The value. i.e. The url.
     */
    public void setClickAction(ClickEvent.Action action, String value) {
        text.setClickEvent(new ClickEvent(action, value));
    }

    /**
     * Sets the hover action for the message.
     * @param action The action.
     * @param values The value. i.e. The hover text.
     */
    public void setHoverAction(HoverEvent.Action action, String... values) {
        Text[] array = new Text[values.length];

        Arrays.stream(values)
                .map(Text::new)
                .collect(Collectors.toList())
                .toArray(array);

        text.setHoverEvent(new HoverEvent(action, array));
    }

    /**
     * Appends a custom message to this component.
     * @param message The custom message to append.
     */
    public CustomMessage append(CustomMessage message) {
        text.addExtra(message.text);
        return this;
    }

    /**
     * Appends a component to this component.
     * @param component The component to append.
     */
    public CustomMessage append(BaseComponent component) {
        text.addExtra(component);
        return this;
    }

    /**
     * Appends a string to this component.
     * @param string The string to append.
     */
    public CustomMessage append(String string) {
        text.addExtra(string);
        return this;
    }

    /**
     * Gets the created text component.
     * @return The bukkit text component.
     */
    public TextComponent get() {
        return text;
    }

    /**
     * Sends the message to a player.
     * @param p The player to send the message to.
     */
    public void send(Player p) {
        p.spigot().sendMessage(text);
    }
}
