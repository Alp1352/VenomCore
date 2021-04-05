package com.venom.venomcore.plugin.server;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;

public enum ServerType {
    CRAFT_BUKKIT,
    SPIGOT,
    PAPER,
    TACO,
    TUINITY,
    AIRPLANE,
    PURPUR,
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
        return ArrayUtils.contains(types, SERVER_TYPE);
    }

    public static boolean isSupported() {
        return !isServerType(ServerType.UNKNOWN);
    }

    public static boolean isStarlightEngine() {
        return isServerType(ServerType.TUINITY, ServerType.AIRPLANE, ServerType.PURPUR);
    }

    public static boolean isAsyncChunkEngine() {
        return isServerType(ServerType.PAPER, ServerType.TACO, ServerType.IMANITY, ServerType.TUINITY, ServerType.AIRPLANE, ServerType.PURPUR);
    }
}
