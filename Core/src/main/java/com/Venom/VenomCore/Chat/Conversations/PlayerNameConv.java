package com.Venom.VenomCore.Chat.Conversations;

import com.Venom.VenomCore.Chat.Color;
import com.Venom.VenomCore.Plugin.VenomPlugin;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.PlayerNamePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 *  * A class for conversations that accept a player value.
 */
public class PlayerNameConv extends PlayerNamePrompt {
    private final String promptText;
    private final Prompt nextPrompt;
    private final Consumer<Player> action;
    private final Function<String, String> invalid;

    /**
     * Creates a PlayerNamePrompt
     * @param promptText The message to send the player at the start of the conversation.
     * @param invalid The message to send the player if an invalid value is written.
     *                The String parameter is the invalid value.
     * @param nextPrompt The next prompt.
     * @param action The action to run.
     */
    public PlayerNameConv(@NotNull VenomPlugin plugin, @Nullable String promptText, @Nullable Function<String, String> invalid, @Nullable Prompt nextPrompt, @Nullable Consumer<Player> action) {
        super(plugin);
        this.promptText = promptText;
        this.nextPrompt = nextPrompt;
        this.action = action;
        this.invalid = invalid;
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull Player input) {
        if (action != null) {
            action.accept(input);
        }
        return nextPrompt;
    }

    @Override
    protected @Nullable String getFailedValidationText(@NotNull ConversationContext context, @NotNull String invalidInput) {
        if (invalid != null) {
            return Color.translate(invalid.apply(invalidInput));
        } else {
            return null;
        }
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext context) {
        if (promptText == null) {
            return " ";
        } else {
            return Color.translate(promptText);
        }
    }
}
