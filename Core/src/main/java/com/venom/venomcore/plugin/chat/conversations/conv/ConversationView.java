package com.venom.venomcore.plugin.chat.conversations.conv;

import com.venom.venomcore.nms.core.chat.ActionBarCore;
import com.venom.venomcore.nms.core.chat.TitleCore;
import com.venom.venomcore.plugin.chat.Color;
import com.venom.venomcore.plugin.chat.CustomMessage;
import com.venom.venomcore.plugin.chat.conversations.image.ImageChar;
import com.venom.venomcore.plugin.chat.conversations.image.ImageMessage;
import com.venom.venomcore.plugin.nms.NMSManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ConversationView {
    private boolean persist = false;

    protected abstract void handle(Player player, Plugin plugin);

    /**
     * Get a title view for conversations.
     * @param title The title. Can be null.
     * @param subtitle The subtitle. Can be null.
     * @param fadeIn Fade in ticks.
     * @param stay Stay ticks.
     * @param fadeOut Fadeout ticks.
     * @return The view to use.
     */
    public static ConversationView title(@Nullable String title, @Nullable String subtitle, int fadeIn, int stay, int fadeOut) {
        return new Title(Color.translate(title), Color.translate(subtitle), fadeIn, stay, fadeOut);
    }

    /**
     * Get an actionbar view for conversations.
     * @param message The message to send.
     * @param stay The stay ticks.
     * @return The view to use.
     */
    public static ConversationView actionbar(String message, int stay) {
        return new ActionBar(Color.translate(message), stay);
    }

    /**
     * Get a multi-lined clickable message for conversations.
     * @param messages Messages to send. If you don't want
     *                 clickable messages, use the {@link ConversationView#chat(String... message)}
     *                 method.
     * @return The view to use.
     */
    public static ConversationView chat(CustomMessage... messages) {
        return new Chat(Arrays.asList(messages));
    }

    /**
     * Get a multi-lined message for conversations.
     * @param messages The messages to send.
     * @return The view to use.
     */
    public static ConversationView chat(String... messages) {
        return new Chat(Arrays.stream(messages)
                .map(Color::translate)
                .map(CustomMessage::new)
                .collect(Collectors.toList()));
    }


    /**
     * Get an image message for conversations.
     * @param url The url to read the image from.
     * @param imageChar The image char. {@link ImageChar}
     * @param texts Messages to send. {@link ImageMessage.ImageText}
     * @return The message to use.
     */
    public static ConversationView image(String url, ImageChar imageChar, ImageMessage.ImageText... texts) {
        return new Image(url, imageChar, texts);
    }

    /**
     * If run, the title/actionbar will stay there
     * until the end of conversation.
     * @return The persistent view.
     */
    public ConversationView persist() {
        persist = true;
        return this;
    }

    /**
     * Check whether or not this view is persistent.
     * @return True if the {@link ConversationView#persist()} method has ran before.
     */
    public boolean isPersistent() {
        return persist;
    }

    protected static class Title extends ConversationView {
         private final String title;
         private final String subtitle;
         private final int fadeIn;
         private final int stay;
         private final int fadeOut;

         private Title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
             this.title = title;
             this.subtitle = subtitle;
             this.fadeIn = fadeIn;
             this.stay = stay;
             this.fadeOut = fadeOut;
         }

         @Override
         public void handle(Player player, Plugin plugin) {
             final TitleCore TITLE = NMSManager.getTitle();

             TITLE.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);

             if (!isPersistent()) return;

             BukkitRunnable runnable = new BukkitRunnable() {
                 @Override
                 public void run() {
                     if (!Conversation.isConversing(player)) {
                         TITLE.clearTitle(player);
                         cancel();
                         return;
                     }

                     TITLE.sendTitle(player, title, subtitle, 1, stay, fadeOut);
                 }
             };

             runnable.runTaskTimerAsynchronously(plugin, stay, stay);
         }
     }

    protected static class ActionBar extends ConversationView {
        private final String message;
        private final int stay;

        private ActionBar(String message, int stay) {
            this.message = message;
            this.stay = stay;
        }


        @Override
        public void handle(Player player, Plugin plugin) {
            final ActionBarCore ACTION_BAR = NMSManager.getActionBar();

            ACTION_BAR.sendActionBar(player, message);

            BukkitRunnable runnable;

            if (isPersistent()) {
                runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!Conversation.isConversing(player)) {
                            ACTION_BAR.clearActionBar(player);
                            cancel();
                            return;
                        }

                        ACTION_BAR.sendActionBar(player, message);
                    }
                };
            } else if (stay != -1) {
                runnable = new BukkitRunnable() {
                    int tick = 0;
                    @Override
                    public void run() {
                        if (!Conversation.isConversing(player) || stay == tick) {
                            ACTION_BAR.clearActionBar(player);
                            cancel();
                            return;
                        }

                        ACTION_BAR.sendActionBar(player, message);
                        tick = tick + 1;
                    }
                };
            } else {
                return;
            }


            runnable.runTaskTimerAsynchronously(plugin, 1, 1);
        }
    }

    protected static class Chat extends ConversationView {
        private final Iterable<CustomMessage> messages;
        private Chat(Iterable<CustomMessage> messages) {
            this.messages = messages;
        }


        @Override
        protected void handle(Player player, Plugin plugin) {
            messages.forEach(customMessage -> customMessage.send(player));
        }
    }

    protected static class Image extends ConversationView {
        private final String url;
        private final ImageChar imageChar;
        private final List<ImageMessage.ImageText> texts;

        private Image(String url, ImageChar imageChar, ImageMessage.ImageText... texts) {
            this.url = url;
            this.imageChar = imageChar;
            this.texts = Arrays.asList(texts);
        }

        @Override
        protected void handle(Player player, Plugin plugin) {
            try {
                URL link = new URL(url);

                BufferedImage image = ImageIO.read(link);

                new ImageMessage(image, 8, imageChar.getChar())
                        .appendText(texts)
                        .send(player);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
