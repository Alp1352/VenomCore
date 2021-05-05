package com.venom.venomcore.plugin.chat.conversations;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class ConversationFactory {
    private static final int EXPIRE = 1200;
    private static final List<String> CANCEL_MESSAGES = Arrays.asList("iptal","iptal et", "çıkış", "çık", "quit", "leave", "exit");
    private static final LocalEcho LOCAL_ECHO = LocalEcho.NEVER;

    private final Plugin plugin;


    public ConversationFactory(Plugin plugin) {
        this.plugin = plugin;
    }

    public Conversation<Boolean> getBooleanConv(ConversationAction<Boolean> valid,
                                                ConversationAction<String> invalid,
                                                ConversationAction<Reason> cancel,
                                                ConversationView... view) {
        return Conversation.<Boolean>builder(plugin)
                .withEcho(LOCAL_ECHO)
                .withGetter(ConversationGetter.bool())
                .withCancelMessages(CANCEL_MESSAGES)
                .withView(view)
                .onValid(valid)
                .onInvalid(invalid)
                .onCancel(cancel)
                .expireAfter(EXPIRE)
                .andThen(Conversation.END_OF_CONVERSATION)
                .build();
    }

    public Conversation<Player> getPlayerConv(ConversationAction<Player> valid,
                                              ConversationAction<String> invalid,
                                              ConversationAction<Reason> cancel,
                                              ConversationView... view) {

        return Conversation.<Player>builder(plugin)
                .withEcho(LOCAL_ECHO)
                .withGetter(ConversationGetter.player())
                .withCancelMessages(CANCEL_MESSAGES)
                .withView(view)
                .onValid(valid)
                .onInvalid(invalid)
                .onCancel(cancel)
                .expireAfter(EXPIRE)
                .andThen(Conversation.END_OF_CONVERSATION)
                .build();
    }

    public Conversation<String> getStringConv(ConversationAction<String> valid,
                                              ConversationAction<Reason> cancel,
                                              ConversationView... view) {

        return Conversation.<String>builder(plugin)
                .withEcho(LOCAL_ECHO)
                .withGetter(ConversationGetter.string())
                .withCancelMessages(CANCEL_MESSAGES)
                .withView(view)
                .onValid(valid)
                .onInvalid(ConversationAction.nothing())
                .onCancel(cancel)
                .expireAfter(EXPIRE)
                .andThen(Conversation.END_OF_CONVERSATION)
                .build();
    }

    public <V extends Enum<V>> Conversation<V> getEnumConv(Class<V> clazz,
                                                           ConversationAction<V> valid,
                                                           ConversationAction<String> invalid,
                                                           ConversationAction<Reason> cancel,
                                                           ConversationView... view) {

        return Conversation.<V>builder(plugin)
                .withEcho(LOCAL_ECHO)
                .withGetter(ConversationGetter.constant(clazz))
                .withCancelMessages(CANCEL_MESSAGES)
                .withView(view)
                .onValid(valid)
                .onInvalid(invalid)
                .onCancel(cancel)
                .expireAfter(EXPIRE)
                .andThen(Conversation.END_OF_CONVERSATION)
                .build();
    }

    public Conversation<Number> getNumberConv(ConversationAction<Number> valid,
                                              ConversationAction<String> invalid,
                                              ConversationAction<Reason> cancel,
                                              ConversationView... view
                                              ) {

        return Conversation.<Number>builder(plugin)
                .withEcho(LOCAL_ECHO)
                .withGetter(ConversationGetter.number())
                .withCancelMessages(CANCEL_MESSAGES)
                .withView(view)
                .onValid(valid)
                .onInvalid(invalid)
                .onCancel(cancel)
                .expireAfter(EXPIRE)
                .andThen(Conversation.END_OF_CONVERSATION)
                .build();
    }
}
