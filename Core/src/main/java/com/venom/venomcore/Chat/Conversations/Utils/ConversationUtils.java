package com.venom.venomcore.Chat.Conversations.Utils;

import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.plugin.java.JavaPlugin;

public class ConversationUtils {
    /**
     * Creates a conversation factory with default settings.
     * @param plugin The plugin getting the factory.
     * @param firstPrompt The first prompt.
     * @return The created factory.
     */
    public static ConversationFactory getQuickFactory(JavaPlugin plugin, Prompt firstPrompt) {
        ConversationFactory factory = new ConversationFactory(plugin);
        return factory.withFirstPrompt(firstPrompt).withLocalEcho(false).withModality(false).withTimeout(100);
    }

    /**
     * Creates a conversation with default settings and optionally, begins.
     * @param plugin The plugin creating the conversation.
     * @param firstPrompt The prompt to begin with.
     * @param conversable The conversable.
     * @param start Whether to start the conversation right away or not.
     * @return The created conversation.
     */
    public static Conversation getQuickConversation(JavaPlugin plugin, Prompt firstPrompt, Conversable conversable, boolean start) {
        Conversation conversation = getQuickFactory(plugin, firstPrompt).buildConversation(conversable);
        if (start) conversation.begin();

        return conversation;
    }
}
