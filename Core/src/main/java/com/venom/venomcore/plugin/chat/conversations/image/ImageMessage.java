package com.venom.venomcore.plugin.chat.conversations.image;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.ChatPaginator;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImageMessage {
    private final static char TRANSPARENT_CHAR = ' ';

    private static final Color[] COLORS = {
            new Color(0, 0, 0),
            new Color(0, 0, 170),
            new Color(0, 170, 0),
            new Color(0, 170, 170),
            new Color(170, 0, 0),
            new Color(170, 0, 170),
            new Color(255, 170, 0),
            new Color(170, 170, 170),
            new Color(85, 85, 85),
            new Color(85, 85, 255),
            new Color(85, 255, 85),
            new Color(85, 255, 255),
            new Color(255, 85, 85),
            new Color(255, 85, 255),
            new Color(255, 255, 85),
            new Color(255, 255, 255),
    };

    private final String[] lines;

    public ImageMessage(BufferedImage image, int height, char imgChar) {
        ChatColor[][] chatColors = toChatColorArray(image, height);
        lines = toImgMessage(chatColors, imgChar);
    }

    public ImageMessage appendText(List<ImageText> text) {
        for (int y = 0; y < lines.length; y++) {
            if (text.size() > y) {
                ImageText imageText = text.get(y);
                if (imageText.isCentered()) {
                    int len = ChatPaginator.AVERAGE_CHAT_PAGE_WIDTH - lines[y].length();
                    lines[y] = lines[y] + center(imageText.getText(), len);
                } else {
                    lines[y] += " " + imageText.getText();
                }
            }
        }
        return this;
    }


    private ChatColor[][] toChatColorArray(BufferedImage image, int height) {
        double ratio = (double) image.getHeight() / image.getWidth();
        BufferedImage resized = resizeImage(image, (int) (height / ratio), height);

        ChatColor[][] chatImg = new ChatColor[resized.getWidth()][resized.getHeight()];
        for (int x = 0; x < resized.getWidth(); x++) {
            for (int y = 0; y < resized.getHeight(); y++) {
                int rgb = resized.getRGB(x, y);
                ChatColor closest = getClosestChatColor(new Color(rgb, true));
                chatImg[x][y] = closest;
            }
        }
        return chatImg;
    }

    private String[] toImgMessage(ChatColor[][] colors, char imgchar) {
        String[] lines = new String[colors[0].length];
        for (int i = 0; i < colors[0].length; i++) {
            StringBuilder line = new StringBuilder();
            for (ChatColor[] chatColors : colors) {
                ChatColor color = chatColors[i];
                line.append((color != null) ? chatColors[i].toString() + imgchar : TRANSPARENT_CHAR);
            }
            lines[i] = line.toString() + ChatColor.RESET;
        }
        return lines;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        AffineTransform af = new AffineTransform();
        af.scale(
                width / (double) originalImage.getWidth(),
                height / (double) originalImage.getHeight());

        AffineTransformOp operation = new AffineTransformOp(af, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return operation.filter(originalImage, null);
    }

    private double getDistance(Color c1, Color c2) {
        double rmean = (c1.getRed() + c2.getRed()) / 2.0;
        double r = c1.getRed() - c2.getRed();
        double g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();
        double weightR = 2 + rmean / 256.0;
        double weightG = 4.0;
        double weightB = 2 + (255 - rmean) / 256.0;
        return weightR * r * r + weightG * g * g + weightB * b * b;
    }

    private boolean areIdentical(Color c1, Color c2) {
        return Math.abs(c1.getRed() - c2.getRed()) <= 5 &&
                Math.abs(c1.getGreen() - c2.getGreen()) <= 5 &&
                Math.abs(c1.getBlue() - c2.getBlue()) <= 5;
    }

    private ChatColor getClosestChatColor(Color color) {
        if (color.getAlpha() < 128) return null;

        int index = 0;
        double best = -1;

        for (int i = 0; i < COLORS.length; i++) {
            if (areIdentical(COLORS[i], color)) {
                return ChatColor.values()[i];
            }
        }

        for (int i = 0; i < COLORS.length; i++) {
            double distance = getDistance(color, COLORS[i]);
            if (distance < best || best == -1) {
                best = distance;
                index = i;
            }
        }

        // Minecraft has 15 colors
        return ChatColor.values()[index];
    }

    private String center(String s, int length) {
        if (s.length() > length) {
            return s.substring(0, length);
        } else if (s.length() == length) {
            return s;
        } else {
            int leftPadding = (length - s.length()) / 2;
            StringBuilder leftBuilder = new StringBuilder();
            for (int i = 0; i < leftPadding; i++) {
                leftBuilder.append(" ");
            }
            return leftBuilder + s;
        }
    }

    public String[] getLines() {
        return lines;
    }

    public void send(Player player) {
        for (String line : getLines()) {
            player.sendMessage(line);
        }
    }

    public static class ImageText {
        private final String text;
        private final boolean center;
        public ImageText(String text, boolean center) {
            this.text = text;
            this.center = center;
        }

        public String getText() {
            return text;
        }

        public boolean isCentered() {
            return center;
        }
    }
}
