package com.venom.venomcore.Chat.Conversations;

import com.venom.venomcore.Chat.Color;
import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A class for conversations that accept a boolean value.
 */
public class BooleanConv extends BooleanPrompt {

    private final Prompt nextPrompt;
    private final String promptText;
    private final Consumer<Boolean> action;
    private final Function<String, String> invalid;

    /**
     * Creates a BooleanPrompt.
     * @param promptText The message to send the player at the start of the conversation.
     * @param invalid The message to send the player if an invalid value is written.
     *                The string parameter is the invalid value.
     * @param nextPrompt The next prompt.
     * @param action The action to run.
     */
    public BooleanConv(@Nullable String promptText, @Nullable Function<String, String> invalid, @Nullable Prompt nextPrompt, @Nullable Consumer<Boolean> action) {
        this.nextPrompt = nextPrompt;
        this.promptText = promptText;
        this.action = action;
        this.invalid = invalid;
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, boolean input) {
        if (action != null) {
            action.accept(input);
        }
        return nextPrompt;
    }

    @Override
    protected @Nullable String getFailedValidationText(@NotNull ConversationContext context, @NotNull String invalidInput) {
        return invalid != null ? Color.translate(invalid.apply(invalidInput)) : null;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext context) {
        return promptText == null ? " " : Color.translate(promptText);
    }
}
