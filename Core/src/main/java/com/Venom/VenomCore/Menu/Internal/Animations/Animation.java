package com.Venom.VenomCore.Menu.Internal.Animations;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Animation {
    private final ChatColor[] colors;
    private final AnimationType type;
    private boolean bold;
    private final List<String> animations = new ArrayList<>();

    public Animation(AnimationType type, ChatColor... colors) {
        this.colors = colors;
        this.type = type;
    }

    public AnimationType getType() {
        return type;
    }

    public ChatColor[] getColors() {
        return colors;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isBold() {
        return bold;
    }

    public List<String> getAnimations() {
        return animations;
    }

    public void createAnimation(String displayName) {
        if (type == AnimationType.WAVE) {
            createWave(displayName);
        } else {
            createFade(displayName);
        }
    }

    private void createFade(String displayName) {
        int loop = 0;

        while (animations.size() < getColors().length) {
            ChatColor color = getColors()[loop];

            StringBuilder builder = new StringBuilder();
            builder.append(color);
            if (isBold()) {
                builder.append(ChatColor.BOLD);
            }
            builder.append(ChatColor.stripColor(displayName));

            animations.add(builder.toString());

            if (getColors().length <= loop + 1) {
                loop = 0;
            } else {
                loop = loop + 1;
            }
        }
    }

    private void createWave(String displayName) {
        int loop = 0;
        int colorCode = 0;
        while (animations.size() < (displayName.length() * getColors().length) + 1) {
            String name = ChatColor.stripColor(displayName);
            ChatColor color = getColors()[colorCode];

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < loop; i++) {
                builder.append(color);
                if (isBold()) {
                    builder.append(ChatColor.BOLD);
                }
                builder.append(name.charAt(i));
            }

            ChatColor oldColor;

            if (getColors().length == colorCode + 1) {
                oldColor = getColors()[colorCode - 1];
            } else {
                oldColor = colorCode != 0 ? getColors()[colorCode -1] : getColors()[getColors().length - 1];
            }

            for (int i = loop; i < name.length(); i++) {
                builder.append(oldColor);
                if (isBold()) {
                    builder.append(ChatColor.BOLD);
                }
                builder.append(name.charAt(i));
            }

            if (name.length() == loop) {
                if (getColors().length == colorCode + 1) {
                    colorCode = 0;
                } else {
                    colorCode = colorCode + 1;
                }
                loop = 0;
            }

            animations.add(builder.toString());
            loop = loop + 1;
        }
    }

    public enum AnimationType {
        WAVE(0,1),
        FADE(10,10);

        private final int delay;
        private final int between;

        AnimationType(int delay, int between) {
            this.delay = delay;
            this.between = between;
        }

        public int getDelay() {
            return delay;
        }

        public int getBetween() {
            return between;
        }
    }

}
