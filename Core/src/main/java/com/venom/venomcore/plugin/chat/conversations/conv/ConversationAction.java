package com.venom.venomcore.plugin.chat.conversations.conv;

import org.bukkit.entity.Player;

public interface ConversationAction<V> {
    void handle(Player player, V value, Conversation<?> conversation);
}
