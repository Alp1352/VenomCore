package com.venom.nms.v1_9_R1;

import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class NMSUtils {
    public static void sendPacket(EntityPlayer player, Packet<?> packet) {
        CompletableFuture.runAsync(() -> sendPacketSync(player, packet));
    }

    public static void sendPacketSync(EntityPlayer player, Packet<?> packet) {
        if (player.playerConnection != null) {
            player.playerConnection.sendPacket(packet);
        }
    }

    public static void sendPacket(Packet<?> packet) {
        Bukkit.getOnlinePlayers().forEach(player -> sendPacket(toNMS(player), packet));
    }

    public static void sendPacketSync(Packet<?> packet) {
        Bukkit.getOnlinePlayers().forEach(player -> sendPacketSync(toNMS(player), packet));
    }

    public static EntityPlayer toNMS(Player p) {
        return ((CraftPlayer) p).getHandle();
    }
}
