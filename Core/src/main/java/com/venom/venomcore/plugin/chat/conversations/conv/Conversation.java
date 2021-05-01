package com.venom.venomcore.plugin.chat.conversations.conv;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A class used for conversations to get a players
 * input for us to use later.
 * @author Alp Beji
 * @param <V> The value we will be receiving.
 */
public class Conversation<V> implements Listener {

    public static final Conversation<?> END_OF_CONVERSATION = null;
    private static final Set<UUID> ALL_CONVERSING_PLAYERS = new CopyOnWriteArraySet<>();
    private static final BukkitScheduler SCHEDULER = Bukkit.getScheduler();

    private final Plugin plugin;
    private final Player player;

    private final ConversationGetter<V> getter;

    private final ConversationAction<V> validAction;
    private final ConversationAction<String> invalidAction;
    private final ConversationAction<Reason> cancelAction;

    private final int expire;

    private final Collection<String> cancel;
    private final Collection<ConversationView> messages;

    private final Conversation<?> next;

    private BukkitTask expireTask;

    private final AtomicReference<State> state = new AtomicReference<>(State.NOT_STARTED);
    private final LocalEcho echo;

    /**
     * Create a new conversation to get
     * values from a specific player
     * for us to use later.
     *
     * @param plugin The plugin creating the new conversation.
     *
     * @param player The player we will be getting values from.
     *
     * @param messages The messages the player will see on the start
     *                 of the conversation. See {@link ConversationView}.
     *
     * @param getter The getter for the input string. You can use
     *               any pre-made getters via {@link ConversationGetter}
     *               or you can create your own. If the input is invalid,
     *               the getter should return null.
     *
     * @param onValid Function to run once a valid input is received.
     *                If you only want one input, you can use {@link Conversation#next()}
     *                method to start the next conversation or you can just
     *                end it via {@link Conversation#stop()} or {@link Conversation#stopDirect()}.
     *
     * @param onInvalid Function to run once an invalid input is received.
     *                  As with valid function, you can also use {@link Conversation#next()}
     *                  method to start the next conversation or you can just
     *                  end it via {@link Conversation#stop()} or {@link Conversation#stopDirect()}.
     *
     * @param onCancel Function run when the conversation is cancelled.
     *                 The reason will also be specified with the
     *                 {@link Reason} enum.
     *
     * @param expireAfter The amount of ticks until this conversation
     *                    expires. Whenever a player makes an input,
     *                    this timer will be reset.
     *
     * @param echo If enabled, the player will receive the echo
     *             of its input.
     *
     * @param cancel A collection of strings that the player
     *               can write to abandon the conversation.
     *
     * @param next The next conversation. If this is the end,
     *             you should use {@link Conversation#END_OF_CONVERSATION}.
     *             If you want this conversation to pass on to a new one,
     *             you can use {@link Conversation#next()} method.
     */
    public Conversation(
            Plugin plugin,
            Player player,
            Collection<ConversationView> messages,
            ConversationGetter<V> getter,
            ConversationAction<V> onValid,
            ConversationAction<String> onInvalid,
            ConversationAction<Reason> onCancel,
            int expireAfter,
            LocalEcho echo,
            @Nullable Collection<String> cancel,
            @Nullable Conversation<?> next
            ) {

        this.plugin = plugin;
        this.player = player;
        this.messages = messages;
        this.getter = getter;
        this.validAction = onValid;
        this.invalidAction = onInvalid;
        this.cancelAction = onCancel;
        this.expire = expireAfter;
        this.echo = echo;
        this.cancel = cancel;
        this.next = next;

        Bukkit.getPluginManager()
                .registerEvents(this, plugin);
    }

    @EventHandler
    protected void onQuit(PlayerQuitEvent e) {
        if (e.getPlayer().equals(player) && state.get() == State.ACTIVE) {
            stopDirect();
            handle(cancelAction, Reason.DISCONNECT);
        }
    }

    @EventHandler
    protected void onChat(AsyncPlayerChatEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();

        if (!uuid.equals(this.player.getUniqueId()) || state.get() != State.ACTIVE) return;

        String prompt = e.getMessage();

        e.setCancelled(true);

        if (cancel != null && !cancel.isEmpty() && cancel.contains(prompt)) {
            stopDirect();
            handle(cancelAction, Reason.EXIT);
            return;
        }

        V value = getter.get(prompt);

        if (value == null) {
            handle(invalidAction, prompt);
            if (echo == LocalEcho.ALWAYS || echo == LocalEcho.ONLY_ON_INVALID) {
                player.sendMessage(prompt);
            }
            return;
        }

        handle(validAction, value);
        if (echo == LocalEcho.ALWAYS || echo == LocalEcho.ONLY_ON_VALID) {
            player.sendMessage(prompt);
        }

        startExpireTask();
    }


    /**
     * Start the conversation.
     */
    public void start() {
        ALL_CONVERSING_PLAYERS.add(player.getUniqueId());
        startExpireTask();

        for (ConversationView message : messages) {
            message.handle(player, plugin);
        }

        state.set(State.ACTIVE);
    }

    /**
     * End the current conversation,
     * and get to the next one.
     */
    public void next() {
        stopDirect();

        if (next != END_OF_CONVERSATION) {
            next.start();
        }
    }

    /**
     * Stop the conversation.
     * Do note that this will also run
     * your cancel function with {@link Reason#PLUGIN} reason.
     */
    public void stop() {
        stopDirect();
        handle(cancelAction, Reason.PLUGIN);
    }

    /**
     * Stops the conversation WITHOUT
     * running your cancel function.
     */
    public void stopDirect() {
        HandlerList.unregisterAll(this);
        ALL_CONVERSING_PLAYERS.remove(player.getUniqueId());

        if (expireTask != null) {
            expireTask.cancel();
        }

        expireTask = null;

        state.set(State.OVER);
    }

    private <K> void handle(ConversationAction<K> action, K value) {
        SCHEDULER.runTask(plugin, () -> action.handle(player, value, this));
    }

    private void startExpireTask() {
        if (expire == -1) return;

        if (expireTask != null) {
            expireTask.cancel();
            expireTask = null;
        }


        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                stopDirect();
                handle(cancelAction, Reason.EXPIRE);
            }
        };

        this.expireTask = task.runTaskLaterAsynchronously(plugin, expire);
    }

    /**
     * Get the next conversation.
     * @return The next conversation if exists, otherwise null.
     */
    public Conversation<?> getNext() {
        return next;
    }

    /**
     * Get the current state of the conversation.
     * @return The current state of the conversation.
     */
    public State getCurrentState() {
        return state.get();
    }

    /**
     * Check if a given player is currently in a conversation.
     * This basically calls {@link Conversation#isConversing(UUID)}
     * method with the players unique id.
     * @param player The player to check.
     * @return True if the player is in conversation.
     */
    public static boolean isConversing(Player player) {
        return isConversing(player.getUniqueId());
    }

    /** Check if a given player is currently in a conversation.
     * @param uuid The uuid to check.
     * @return True if the player is in a conversation.
     */
    public static boolean isConversing(UUID uuid) {
        return ALL_CONVERSING_PLAYERS.contains(uuid);
    }

    /**
     * Get the conversation builder.
     * @param plugin The plugin requesting the builder.
     * @param player The player the server will communicate with.
     * @param <V> The type of the conversation i.e. Integer.
     * @return The builder.
     */
    public static <V> Builder<V> builder(Plugin plugin, Player player) {
        return new Builder<>(plugin, player);
    }

    public static class Builder<V> {
        private final Plugin plugin;
        private final Player player;
        private final List<ConversationView> views = new ArrayList<>();
        private final List<String> messages = new ArrayList<>();

        private ConversationGetter<V> getter;

        private ConversationAction<V> valid;
        private ConversationAction<String> invalid;
        private ConversationAction<Reason> cancel;



        private LocalEcho echo = LocalEcho.NEVER;
        private Conversation<?> next = Conversation.END_OF_CONVERSATION;
        private int expire = -1;

        private Builder(Plugin plugin, Player player) {
            this.plugin = plugin;
            this.player = player;
        }

        public Builder<V> withView(ConversationView... views) {
            this.views.addAll(Arrays.asList(views));
            return this;
        }

        public Builder<V> withView(ConversationView view) {
            this.views.add(view);
            return this;
        }

        public Builder<V> withGetter(ConversationGetter<V> getter) {
            this.getter = getter;
            return this;
        }

        public Builder<V> withEcho(LocalEcho echo) {
            this.echo = echo;
            return this;
        }

        public Builder<V> withCancelMessages(Collection<String> messages) {
            this.messages.addAll(messages);
            return this;
        }

        public Builder<V> withCancelMessages(String... messages) {
            this.messages.addAll(Arrays.asList(messages));
            return this;
        }

        public Builder<V> onValid(ConversationAction<V> valid) {
            this.valid = valid;
            return this;
        }

        public Builder<V> onInvalid(ConversationAction<String> invalid) {
            this.invalid = invalid;
            return this;
        }

        public Builder<V> onCancel(ConversationAction<Reason> cancel) {
            this.cancel = cancel;
            return this;
        }

        public Builder<V> expireAfter(int expire) {
            this.expire = expire;
            return this;
        }

        public Builder<V> andThen(Conversation<?> next) {
            this.next = next;
            return this;
        }

        public Conversation<V> build() {
            return new Conversation<>(plugin, player, views, getter, valid, invalid, cancel, expire, echo, messages, next);
        }
    }
}
