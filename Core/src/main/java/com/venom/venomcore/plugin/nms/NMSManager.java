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
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
    private static Class<?> CRAFT_PLAYER_CLASS;

    private static Method HANDLE_METHOD;
    private static Method SEND_PACKET_METHOD;

    private static Field CONNECTION_FIELD;

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

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + ServerVersion.getServerVersion().getNMSVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getCraftBukkitClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + ServerVersion.getServerVersion().getNMSVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void sendPacket(Player player, Object packet) {
        try {
            if(!packet.getClass().isAssignableFrom(PACKET_CLASS)){
                throw new IllegalArgumentException("The given object is not a packet!");
            }

            Object craftPlayer = CRAFT_PLAYER_CLASS.cast(player);
            Object entityPlayer = HANDLE_METHOD.invoke(craftPlayer);
            Object connection = CONNECTION_FIELD.get(entityPlayer);
            SEND_PACKET_METHOD.invoke(connection, packet);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void init() {
        try {
            ServerVersion version = ServerVersion.getServerVersion();
            String NMS_VERSION = version.getNMSVersion();

            PACKET_CLASS = getNMSClass("Packet");
            CRAFT_PLAYER_CLASS = getCraftBukkitClass("entity.CraftPlayer");
            Class<?> ENTITY_PLAYER_CLASS = getNMSClass("EntityPlayer");

            HANDLE_METHOD = CRAFT_PLAYER_CLASS.getDeclaredMethod("getHandle");

            CONNECTION_FIELD = ENTITY_PLAYER_CLASS.getDeclaredField("playerConnection");
            SEND_PACKET_METHOD = getNMSClass("PlayerConnection").getDeclaredMethod("sendPacket", PACKET_CLASS);

            SEND_PACKET_METHOD.setAccessible(true);
            HANDLE_METHOD.setAccessible(true);
            CONNECTION_FIELD.setAccessible(true);

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
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
}
