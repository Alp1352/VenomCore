package com.venom.venomcore.plugin.chat.conversations;

import org.bukkit.entity.Player;

public interface ConversationAction<V> {
    void handle(Player player, V value, Conversation<?> conversation);
}
