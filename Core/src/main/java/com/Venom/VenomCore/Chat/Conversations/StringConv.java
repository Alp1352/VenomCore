package com.Venom.VenomCore.Chat.Conversations;

import com.Venom.VenomCore.Chat.Color;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * A class for conversations that accept a string value.
 */
public class StringConv extends StringPrompt {
    private final Prompt nextPrompt;
    private final String promptText;
    private final Consumer<String> action;

    /**
     * Creates a StringPrompt
     * @param promptText The message to send the player at the start of the conversation.
     * @param nextPrompt The next prompt.
     * @param action The action to run.
     */
    public StringConv(@Nullable String promptText, @Nullable Prompt nextPrompt, @Nullable Consumer<String> action) {
        this.nextPrompt = nextPrompt;
        this.promptText = promptText;
        this.action = action;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext context) {
        if (promptText == null) {
            return " ";
        } else {
            return Color.translate(promptText);
        }
    }

    @Override
    public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
        if (action != null) {
            action.accept(input);
        }
        return nextPrompt;
    }
}
