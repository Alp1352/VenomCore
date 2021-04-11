package com.venom.venomcore.plugin.server;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;

public enum ServerVersion {
    v1_7_R4("1.7.10"),
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

    private static final ServerVersion VERSION;

    private final String nmsVersion;
    private final String readable;

    static {
        ServerVersion version;
        try {
            version = ServerVersion.valueOf(Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3]);
        } catch (IllegalArgumentException e) {
            version = UNKNOWN;
        }
        VERSION = version;
    }

    ServerVersion(String readable) {
        this.nmsVersion = this.name();
        this.readable = readable;
    }

    /**
     * Get the version in a readable format.
     * @return The version.
     */
    public String getReadableVersion() {
        return readable;
    }

    /**
     * Get the version in NMS format.
     * i.e. v1_8_R3.
     * @return The version.
     */
    public String getNMSVersion() {
        return nmsVersion;
    }

    /**
     * Check whether the server is legacy or not.
     * @return True if the server is legacy(Version is lower than 1.13)
     */
    public boolean isLegacy() {
        return isServerVersionLowerThan(v1_13_R1);
    }

    /**
     * Get the server version.
     * @return The server version.
     */
    public static ServerVersion getServerVersion() {
        return VERSION;
    }

    /**
     * Check whether the server version is the given parameter.
     * @param version The server version to check.
     * @return True if the server version matches with the given version.
     */
    public static boolean isServerVersion(ServerVersion version) {
        return getServerVersion() == version;
    }

    /**
     * Check whether the server version is one of the given parameters.
     * @param versions The versions.
     * @return True if given versions contains the server version.
     */
    public static boolean isServerVersion(ServerVersion... versions) {
        return ArrayUtils.contains(versions, VERSION);
    }

    /**
     * Check whether the server version is lower than the given version.
     * @param version The version to check.
     * @return True if the server version is lower than given version.
     */
    public static boolean isServerVersionLowerThan(ServerVersion version) {
        return getServerVersion().ordinal() < version.ordinal();
    }

    /**
     * Check whether the server version is higher than the given version.
     * @param version The version to check.
     * @return True if the server version is higher than given version.
     */
    public static boolean isServerVersionHigherThan(ServerVersion version) {
        return getServerVersion().ordinal() > version.ordinal();
    }

    /**
     * Check whether the server version is lower or equal to the given version.
     * @param version The version to check.
     * @return True if the server version is lower or equal to given version.
     */
    public static boolean isServerVersionLowerOrEqual(ServerVersion version) {
        return getServerVersion().ordinal() <= version.ordinal();
    }

    /**
     * Check whether the server version is higher or equal to the given version.
     * @param version The version to check.
     * @return True if the server version is higher or equal to given version.
     */
    public static boolean isServerVersionHigherOrEqual(ServerVersion version) {
        return getServerVersion().ordinal() >= version.ordinal();
    }

    /**
     * Check if the server version is supported.
     * @return True if the server version is not the following:
     * {@link ServerVersion#UNKNOWN}, {@link ServerVersion#v1_17_R1}, {@link ServerVersion#v1_7_R4}.
     */
    public static boolean isSupported() {
        return !isServerVersion(ServerVersion.UNKNOWN, ServerVersion.v1_7_R4, ServerVersion.v1_17_R1);
    }

    /**
     * Check if the server is legacy.
     * @return True if the server is legacy.
     */
    public static boolean isServerLegacy() {
        return getServerVersion().isLegacy();
    }

    /**
     * Check if the server is OneEight.
     * @return True if the server version is one of the following:
     * {@link ServerVersion#v1_8_R1}, {@link ServerVersion#v1_8_R2}, {@link ServerVersion#v1_8_R3}.
     */
    public static boolean isOneEight() {
        return isServerVersion(ServerVersion.v1_8_R1, ServerVersion.v1_8_R2, ServerVersion.v1_8_R3);
    }
}
