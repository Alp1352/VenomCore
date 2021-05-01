package com.venom.venomcore.plugin.chat;

import com.venom.venomcore.plugin.server.ServerVersion;
import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class for faster color codes.
 */
public class Color {

    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String translate(String message) {
        String translated = message;

        if (ServerVersion.isServerVersionHigherOrEqual(ServerVersion.v1_16_R1)) {
            Matcher matcher = HEX_PATTERN.matcher(translated);

            while (matcher.find()) {
                String color = translated.substring(matcher.start(), matcher.end());
                translated = translated.replace(color, ChatColor.of(color) + "");
                matcher = HEX_PATTERN.matcher(translated);
            }
        }

        return ChatColor.translateAlternateColorCodes('&', translated);
    }

    public static String RED(String string){
        return ChatColor.RED + string;
    }
    public static String GRAY(String string){
        return ChatColor.GRAY + string;
    }
    public static String YELLOW(String string){
        return ChatColor.YELLOW + string;
    }
    public static String WHITE(String string){
        return ChatColor.WHITE + string;
    }
    public static String DARK_RED(String string){
        return ChatColor.DARK_RED + string;
    }
    public static String GREEN(String string){
        return ChatColor.GREEN + string;
    }
    public static String DARK_GREEN(String string){
        return ChatColor.DARK_GREEN + string;
    }
    public static String DARK_AQUA(String string){
        return ChatColor.DARK_AQUA + string;
    }
    public static String DARK_PURPLE(String string){
        return ChatColor.DARK_PURPLE + string;
    }
    public static String DARK_BLUE(String string){
        return ChatColor.DARK_BLUE + string;
    }
    public static String DARK_GRAY(String string){
        return ChatColor.DARK_GRAY + string;
    }
    public static String AQUA(String string){
        return ChatColor.AQUA + string;
    }
    public static String BLUE(String string){
        return ChatColor.BLUE + string;
    }
    public static String GOLD(String string){
        return ChatColor.GOLD + string;
    }
    public static String LIGHT_PURPLE(String string){
        return ChatColor.LIGHT_PURPLE + string;
    }
    public static String BLACK(String string){
        return ChatColor.BLACK + string;
    }
    public static String BOLD(String string){
        return ChatColor.BOLD + string;
    }
    public static String ITALIC(String string){
        return ChatColor.ITALIC + string;
    }
    public static String STRIKETHROUGH(String string){
        return ChatColor.STRIKETHROUGH + string;
    }
    public static String MAGIC(String string){
        return ChatColor.MAGIC + string;
    }

}

