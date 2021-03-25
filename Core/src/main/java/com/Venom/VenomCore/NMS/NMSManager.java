package com.Venom.VenomCore.NMS;

import com.Venom.VenomCore.Server.ServerVersion;
import com.venom.nms.core.Anvil.AnvilCore;
import com.venom.nms.core.BossBar.BossBarCore;
import com.venom.nms.core.Chat.ActionBarCore;
import com.venom.nms.core.Chat.TitleCore;
import com.venom.nms.core.Hologram.HologramCore;
import com.venom.nms.core.Inventory.TitleUpdaterCore;
import com.venom.nms.core.NBT.NBTCore;
import com.venom.nms.core.Particle.ParticleCore;
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

    static {
        try {
            ServerVersion version = ServerVersion.getServerVersion();
            String NMS_VERSION = version.getNMSVersion();

            PACKET_CLASS = getNMSClass("Packet");
            CRAFT_PLAYER_CLASS = getCraftBukkitClass("entity.CraftPlayer");
            Class<?> ENTITY_PLAYER_CLASS = getNMSClass("EntityPlayer");

            HANDLE_METHOD = CRAFT_PLAYER_CLASS.getDeclaredMethod("getHandle");
            SEND_PACKET_METHOD = ENTITY_PLAYER_CLASS.getDeclaredMethod("sendPacket", PACKET_CLASS);

            CONNECTION_FIELD = ENTITY_PLAYER_CLASS.getDeclaredField("playerConnection");

            HANDLE_METHOD.setAccessible(true);
            CONNECTION_FIELD.setAccessible(true);

            anvilCore = (AnvilCore) Class.forName("com.venom.nms." + NMS_VERSION + ".Anvil.AnvilCore").newInstance();

            titleCore = ServerVersion.isServerVersionLowerThan(ServerVersion.v1_12_R1) ?
                    (TitleCore) Class.forName("com.venom.nms." + NMS_VERSION + ".Chat.Title").newInstance() :
                    (TitleCore) Class.forName("com.venom.nms.common.Chat.Title").newInstance();

            actionBarCore = ServerVersion.isServerVersionLowerThan(ServerVersion.v1_9_R1) ?
                    (ActionBarCore) Class.forName("com.venom.nms." + NMS_VERSION + ".Chat.ActionBar").newInstance() :
                    (ActionBarCore) Class.forName("com.venom.nms.common.Chat.ActionBar").newInstance();

            nbtCore = (NBTCore) Class.forName("com.venom.nms." + NMS_VERSION + ".NBT.NBTCore").newInstance();

            hologramCore = (HologramCore) Class.forName("com.venom.nms." + NMS_VERSION + ".Hologram.HologramCore").newInstance();

            bossBarCore = ServerVersion.isServerVersionLowerThan(ServerVersion.v1_9_R1) ?
                    (BossBarCore) Class.forName("com.venom.nms." + NMS_VERSION + ".BossBar.BossBarCore").newInstance() :
                    (BossBarCore) Class.forName("com.venom.nms.common.BossBar.BossBarCore").newInstance();

            particleCore = ServerVersion.isServerVersionLowerThan(ServerVersion.v1_9_R1) ?
                    (ParticleCore) Class.forName("com.venom.nms." + NMS_VERSION + ".Particles.Particle").newInstance() :
                    (ParticleCore) Class.forName("com.venom.nms.common.Particles.Particle").newInstance();

            updaterCore = (TitleUpdaterCore) Class.forName("com.venom.nms." + NMS_VERSION + ".Inventory.TitleUpdater").newInstance();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

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
}
