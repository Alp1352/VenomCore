package com.venom.venomcore.Chat;
import org.bukkit.ChatColor;

import java.util.Random;

/**
 * A class for faster color codes.
 */
public class Color {

    public static String translate(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
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
    public static String RANDOM(String string){
        return ChatColor.getByChar(Integer.toHexString(new Random().nextInt(16))) + string;
    }

}

