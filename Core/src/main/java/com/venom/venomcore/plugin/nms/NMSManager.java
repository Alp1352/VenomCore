package com.venom.venomcore.plugin.nms;

import com.venom.venomcore.nms.core.anvil.AnvilCore;
import com.venom.venomcore.nms.core.bossbar.BossBarCore;
import com.venom.venomcore.nms.core.chat.ActionBarCore;
import com.venom.venomcore.nms.core.chat.TitleCore;
import com.venom.venomcore.nms.core.hologram.HologramCore;
import com.venom.venomcore.nms.core.inventory.TitleUpdaterCore;
import com.venom.venomcore.nms.core.nbt.NBTCore;
import com.venom.venomcore.nms.core.particle.ParticleCore;
import com.venom.venomcore.plugin.server.ServerVersion;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class NMSManager {
    private static AnvilCore anvilCore;
    private static TitleCore titleCore;
    private static ActionBarCore actionBarCore;
    private static NBTCore nbtCore;
    private static HologramCore hologramCore;
    private static BossBarCore bossBarCore;
    private static ParticleCore particleCore;
    private static TitleUpdaterCore updaterCore;

    private static Class<?> PACKET_CLASS;
    private static MethodHandle HANDLE_METHOD;
    private static MethodHandle SEND_PACKET_METHOD;
    private static MethodHandle CONNECTION_FIELD;

    public static HologramCore getHologram() {
        return hologramCore;
    }

    public static BossBarCore getBossBar() {
        return bossBarCore;
    }

    public static NBTCore getNBT() {
        return nbtCore;
    }

    public static TitleCore getTitle() {
        return titleCore;
    }

    public static ActionBarCore getActionBar() {
        return actionBarCore;
    }

    public static AnvilCore getAnvil() {
        return anvilCore;
    }

    public static ParticleCore getParticle() {
        return particleCore;
    }

    public static TitleUpdaterCore getTitleUpdater() {
        return updaterCore;
    }

    /**
     * Get a NMS class.
     * @param name The class name to search for.
     * @return The NMS class.
     */
    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + ServerVersion.getServerVersion().getNMSVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets a CraftBukkit class.
     * @param name The class name to search for.
     * @return The CraftBukkit class.
     */
    public static Class<?> getCraftBukkitClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + ServerVersion.getServerVersion().getNMSVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert a Bukkit player to NMS EntityPlayer.
     * @param player The player to convert.
     * @return The NMS EntityPlayer.
     */
    public static Object getHandle(Player player) {
        Validate.notNull(player, "Player cannot be null.");

        try {
            return HANDLE_METHOD.invoke(player);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * Get the connection of a player.
     * @param player The player to get the connection of.
     * @return The connection.
     */
    public static Object getConnection(Player player) {
        Validate.notNull(player, "Player cannot be null.");
        try {
            Object entityPlayer = getHandle(player);
            return CONNECTION_FIELD.invoke(entityPlayer);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * Send a packet synchronously.
     * @param player The player to send the packet to.ç
     * @param packet The packet to send.
     */
    public static void sendPacketSync(Player player, Object packet) {
        Validate.isTrue(packet.getClass().isAssignableFrom(PACKET_CLASS), "The given object is not a packet.");
        Object connection = getConnection(player);

        try {
            SEND_PACKET_METHOD.invoke(connection, packet);
        } catch (Throwable e){
            e.printStackTrace();
        }
    }

    /**
     * Send a packet asynchronously.
     * Since packets are thread safe, it is safe to call this method.
     * @param player The player to send the packet to.
     * @param packet The packet to send.
     */
    public static void sendPacket(Player player, Object packet) {
        CompletableFuture
                .runAsync(() -> sendPacketSync(player, packet));
    }

    /**
     * Send multiple packets asynchronously with order.
     * If we use {@link #sendPacket(Player, Object)} method for each packet,
     * Some packets may arrive faster than the others. This can especially be an issue
     * if you are sending entity spawn and entity metadata packets at the same time.
     *
     * @param player The player to send the packets to.
     * @param packets The packets.
     */
    public static void sendPacket(Player player, Object... packets) {
        CompletableFuture
                .runAsync(() -> {
                    for (Object packet : packets) {
                        sendPacketSync(player, packet);
                    }
                });
    }

    public static void init() {
        try {
            ServerVersion version = ServerVersion.getServerVersion();
            String NMS_VERSION = version.getNMSVersion();
            PACKET_CLASS = getNMSClass("Packet");

            MethodHandles.Lookup lookup = MethodHandles.lookup();

            Class<?> CRAFT_PLAYER_CLASS = getCraftBukkitClass("entity.CraftPlayer");
            Class<?> ENTITY_PLAYER_CLASS = getNMSClass("EntityPlayer");
            Class<?> CONNECTION_CLASS = getNMSClass("PlayerConnection");

            Validate.notNull(PACKET_CLASS); Validate.notNull(ENTITY_PLAYER_CLASS); Validate.notNull(CONNECTION_CLASS); Validate.notNull(CRAFT_PLAYER_CLASS);

            HANDLE_METHOD = lookup.findVirtual(CRAFT_PLAYER_CLASS, "getHandle", MethodType.methodType(ENTITY_PLAYER_CLASS));
            SEND_PACKET_METHOD = lookup.findVirtual(CONNECTION_CLASS, "sendPacket", MethodType.methodType(void.class, PACKET_CLASS));
            CONNECTION_FIELD = lookup.findGetter(ENTITY_PLAYER_CLASS, "playerConnection", CONNECTION_CLASS);

            anvilCore = (AnvilCore) Class.forName("com.venom.venomcore.nms." + NMS_VERSION + ".anvil.AnvilCore").newInstance();

            titleCore = ServerVersion.isServerVersionLowerThan(ServerVersion.v1_12_R1) ?
                    (TitleCore) Class.forName("com.venom.venomcore.nms." + NMS_VERSION + ".chat.Title").newInstance() :
                    (TitleCore) Class.forName("com.venom.venomcore.nms.common.chat.Title").newInstance();

            actionBarCore = ServerVersion.isServerVersionLowerThan(ServerVersion.v1_9_R1) ?
                    (ActionBarCore) Class.forName("com.venom.venomcore.nms." + NMS_VERSION + ".chat.ActionBar").newInstance() :
                    (ActionBarCore) Class.forName("com.venom.venomcore.nms.common.chat.ActionBar").newInstance();

            nbtCore = (NBTCore) Class.forName("com.venom.venomcore.nms." + NMS_VERSION + ".nbt.NBTCore").newInstance();

            hologramCore = (HologramCore) Class.forName("com.venom.venomcore.nms." + NMS_VERSION + ".hologram.HologramCore").newInstance();

            bossBarCore = ServerVersion.isServerVersionLowerThan(ServerVersion.v1_9_R1) ?
                    (BossBarCore) Class.forName("com.venom.venomcore.nms." + NMS_VERSION + ".bossbar.BossBarCore").newInstance() :
                    (BossBarCore) Class.forName("com.venom.venomcore.nms.common.bossbar.BossBarCore").newInstance();

            particleCore = ServerVersion.isServerVersionLowerThan(ServerVersion.v1_9_R1) ?
                    (ParticleCore) Class.forName("com.venom.venomcore.nms." + NMS_VERSION + ".particles.Particle").newInstance() :
                    (ParticleCore) Class.forName("com.venom.venomcore.nms.common.particles.Particle").newInstance();

            updaterCore = (TitleUpdaterCore) Class.forName("com.venom.venomcore.nms." + NMS_VERSION + ".inventory.TitleUpdater").newInstance();

            // Run the static initializer for AnvilView.
            Class.forName("com.venom.venomcore.nms." + NMS_VERSION + ".anvil.AnvilView");
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
}
