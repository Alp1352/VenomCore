package com.venom.venomcore.nms.v1_8_R1.bossbar;

import com.venom.venomcore.nms.v1_8_R1.NMSUtils;
import net.minecraft.server.v1_8_R1.EntityWither;
import net.minecraft.server.v1_8_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R1.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Alp Beji
 * @apiNote Because withers start showing particles below 50 percent health,
 * @apiNote percentage below 50 is not supported.
 * A class for sending BossBars in 1.8.8.
 */
public class BossBar implements com.venom.venomcore.nms.core.bossbar.BossBar {
    private double percentage;
    private String text;
    private final ConcurrentHashMap<UUID, EntityWither> withers = new ConcurrentHashMap<>();
    private final Plugin plugin;

    public BossBar(Plugin plugin, String text, double percentage) {
        this.percentage = percentage;
        this.text = text;
        this.plugin = plugin;
        startTimer();
    }

    public void show() {
        Bukkit.getOnlinePlayers().forEach(this::show);
    }

    public void show(Player p) {
        EntityWither wither = new EntityWither(((CraftWorld) p.getWorld()).getHandle());
        wither.setInvisible(true);
        wither.setCustomNameVisible(true);
        wither.setCustomName(ChatColor.translateAlternateColorCodes('&', text));
        wither.setHealth((float) percentage * 3);

        Location location = getWitherLocation(p);
        wither.setPosition(location.getX(), location.getY(), location.getZ());

        withers.put(p.getUniqueId(), wither);

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(wither);
        NMSUtils.sendPacket(NMSUtils.toNMS(p), packet);
    }

    @Override
    public void update(String text) {
        this.text = text;
        synchronize();
    }

    @Override
    public void update(double percentage) {
        this.percentage = percentage;
        synchronize();
    }

    private void synchronize() {
        withers.keySet().forEach(uuid -> {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) {
                net.minecraft.server.v1_8_R1.EntityWither wither = withers.get(uuid);
                wither.setCustomName(ChatColor.translateAlternateColorCodes('&', text));
                wither.setHealth((float) percentage * 3);
                net.minecraft.server.v1_8_R1.PacketPlayOutEntityMetadata packet = new net.minecraft.server.v1_8_R1.PacketPlayOutEntityMetadata(withers.get(uuid).getId(), wither.getDataWatcher(), false);
                NMSUtils.sendPacket(NMSUtils.toNMS(p), packet);
            }
        });
    }

    public void hide() {
        withers.keySet().forEach(uuid -> {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) {
                hide(p);
            }
        });
    }

    public void hide(Player p) {
        EntityWither wither = withers.get(p.getUniqueId());
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(wither.getId());
        NMSUtils.sendPacket(NMSUtils.toNMS(p), packet);
        withers.remove(p.getUniqueId());
    }

    @Override
    public double getPercentage() {
        return percentage;
    }

    @Override
    public String getText() {
        return text;
    }

    private Location getWitherLocation(Player p) {
        Location l = p.getLocation().clone();
        return l.add(l.getDirection().multiply(20));
    }

    private void startTimer() {
        new BukkitRunnable(){
            @Override
            public void run() {
                if (withers.isEmpty()) {
                    cancel();
                    return;
                }

                for (UUID uuid : withers.keySet()) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null) {
                        teleportToPlayer(player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 10);
    }

    private void teleportToPlayer(Player p) {
        EntityWither wither = withers.get(p.getUniqueId());
        Location location = getWitherLocation(p);
        wither.setPosition(location.getX(), location.getY(), location.getZ());
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(wither);
        NMSUtils.sendPacket(NMSUtils.toNMS(p), packet);
    }
}
