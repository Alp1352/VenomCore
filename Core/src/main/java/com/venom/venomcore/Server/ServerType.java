package com.venom.venomcore.Server;

import org.bukkit.Bukkit;

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
        if (types.length == 0)
            return false;

        boolean serverType = false;

        for (ServerType type : types) {
            if (type == getServerType()) {
                serverType = true;
                break;
            }
        }

        return serverType;
    }

    public static boolean isSupported() {
        return !(getServerType() == UNKNOWN);
    }

    public static boolean isAsync() {
        return getServerType() == PAPER || getServerType() == IMANITY;
    }
}
