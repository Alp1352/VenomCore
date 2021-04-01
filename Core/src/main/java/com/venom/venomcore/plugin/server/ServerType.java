package com.venom.venomcore.plugin.server;

import org.bukkit.Bukkit;

import java.util.Arrays;

public enum ServerType {
    SPIGOT,
    CRAFT_BUKKIT,
    PAPER,
    TACO,
    IMANITY,
    UNKNOWN;

    private final static ServerType SERVER_TYPE = getType();

    private static ServerType getType() {
        try {
            Class.forName("spg.lgdev.iSpigot");
            return ServerType.IMANITY;
        } catch (ClassNotFoundException ignored) {}

        try {
            Class.forName("net.techcable.tacospigot.TacoSpigotConfig");
            return ServerType.TACO;
        } catch (ClassNotFoundException ignored) {}

        try {
            Class.forName("com.destroystokyo.paperclip.Paperclip");
            return ServerType.PAPER;
        } catch (ClassNotFoundException ignored) {}

        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return ServerType.PAPER;
        } catch (ClassNotFoundException ignored) {}

        try {
            Class.forName("org.spigotmc.SpigotConfig");
            return ServerType.SPIGOT;
        } catch (ClassNotFoundException ignored) {}

        if (Bukkit.getServer().getClass().getName().contains("craftbukkit")) {
            return ServerType.CRAFT_BUKKIT;
        }

        return ServerType.UNKNOWN;
    }

    public static ServerType getServerType() {
        return SERVER_TYPE;
    }

    public static boolean isServerType(ServerType type) {
        return type == getServerType();
    }

    public static boolean isServerType(ServerType... types) {
        return Arrays.stream(types)
                .anyMatch(ServerType::isServerType);
    }

    public static boolean isSupported() {
        return (getServerType() != UNKNOWN);
    }

    public static boolean isAsync() {
        return getServerType() == PAPER || getServerType() == IMANITY;
    }
}
