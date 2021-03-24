package com.Venom.VenomCore.Server;

import org.bukkit.Bukkit;

import java.util.Arrays;

public enum ServerVersion {
    v1_7_R3("1.7.10"),
    v1_8_R1("1.8.0"),
    v1_8_R2("1.8.3"),
    v1_8_R3("1.8.8"),
    v1_9_R1("1.9.2"),
    v1_9_R2("1.9.4"),
    v1_10_R1("1.10.2"),
    v1_11_R1("1.11.2"),
    v1_12_R1("1.12.2"),
    v1_13_R1("1.13.0"),
    v1_13_R2("1.13.2"),
    v1_14_R1("1.14.4"),
    v1_15_R1("1.15.2"),
    v1_16_R1("1.16.1"),
    v1_16_R2("1.16.3"),
    v1_16_R3("1.16.5"),
    v1_17_R1("1.17.0"),
    UNKNOWN("-1");

    private static ServerVersion version;

    private final String nmsVersion;
    private final String readable;

    static {
        try {
            version = ServerVersion.valueOf(Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3]);
            if (version == ServerVersion.v1_17_R1 || version == ServerVersion.v1_7_R3) {
                System.out.println("Kullandiginiz surum, suanda desteklenmemektedir.");
            }
        } catch (IllegalArgumentException e) {
            version = ServerVersion.UNKNOWN;
        }
    }

    ServerVersion(String readable) {
        this.nmsVersion = this.name();
        this.readable = readable;
    }

    public String getReadableVersion() {
        return readable;
    }

    public String getNMSVersion() {
        return nmsVersion;
    }

    public boolean isLegacy() {
        return isServerVersionLowerThan(v1_13_R1);
    }

    public static ServerVersion getServerVersion() {
        return version;
    }

    public static boolean isServerVersion(ServerVersion version) {
        return getServerVersion() == version;
    }

    public static boolean isServerVersion(ServerVersion... versions) {
        return Arrays.asList(versions).contains(version);
    }

    public static boolean isServerVersionLowerThan(ServerVersion version) {
        return getServerVersion().ordinal() < version.ordinal();
    }

    public static boolean isServerVersionHigherThan(ServerVersion version) {
        return getServerVersion().ordinal() > version.ordinal();
    }

    public static boolean isServerVersionLowerOrEqual(ServerVersion version) {
        return getServerVersion().ordinal() <= version.ordinal();
    }

    public static boolean isServerVersionHigherOrEqual(ServerVersion version) {
        return getServerVersion().ordinal() >= version.ordinal();
    }

    public static boolean isSupported() {
        return !(getServerVersion() == ServerVersion.UNKNOWN) && !(getServerVersion() == ServerVersion.v1_7_R3);
    }

    public static boolean isServerLegacy() {
        return getServerVersion().isLegacy();
    }


}
