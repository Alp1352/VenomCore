package com.venom.venomcore.plugin.chat.conversations;

import org.bukkit.entity.Player;

public interface ConversationAction<V> {
    void handle(Player player, V value, Conversation<?> conversation);

    default ConversationAction<V> andThen(ConversationAction<? super V> after) {
        return (player, value, conversation) -> {
            handle(player, value, conversation);
            after.handle(player, value, conversation);
        };
    }

    static <V> ConversationAction<V> nothing() {
        return (player, value, conversation) -> {};
    }
}
