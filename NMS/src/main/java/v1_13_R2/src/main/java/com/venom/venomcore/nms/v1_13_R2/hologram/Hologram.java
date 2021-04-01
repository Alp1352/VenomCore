package com.venom.venomcore.nms.v1_13_R2.hologram;

import com.venom.venomcore.nms.core.hologram.Status;
import com.venom.venomcore.nms.core.hologram.Time;
import com.venom.venomcore.nms.v1_13_R2.NMSUtils;
import com.venom.venomcore.nms.v1_13_R2.hologram.armorstand.CustomArmorStand;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author Alp Beji
 * A class that uses NMS to create holograms.
 * @apiNote You shouldn't really need the update method unless you are editing the entity manually.
 * TODO Add item support.
 * TODO remove later fix 1.8.3, and 1.8.
 */
public class Hologram implements com.venom.venomcore.nms.core.hologram.Hologram {
    private Location location;
    private final Map<Integer, CustomArmorStand> entities = new HashMap<>();
    private List<String> lines;
    private boolean created = false;

    private HologramTimer timer;
    private final JavaPlugin plugin;

    private static Method validate;

    static {
        try {
            validate = World.class.getDeclaredMethod("b", Entity.class);
            validate.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Hologram(JavaPlugin plugin, Location location, List<String> text) {
        this.lines = new ArrayList<>(text);
        Location loc = location.clone();
        loc.setX(Math.round(location.getX()));
        loc.setY(Math.round(location.getY()));
        loc.setZ(Math.round(location.getZ()));
        this.location = loc.clone();
        this.plugin = plugin;
    }

    /**
     * Creates the hologram.
     * @return The created hologram instance.
     */
    @Override
    public Hologram create() {
        for (int i = 0; i < lines.size(); i++) {
            create(i);
        }
        created = true;
        return this;
    }

    private void create(int i) {
        String line = lines.get(i);
        WorldServer world = ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle();
        CustomArmorStand hologram = new CustomArmorStand(world);

        hologram.setPos(location.getX(), location.getY() - (i * 0.23), location.getZ());
        hologram.setHologramStandName(line);

        final int chunkX = MathHelper.floor(hologram.locX / 16.0);
        final int chunkZ = MathHelper.floor(hologram.locZ / 16.0);

        if (validate == null) {
            world.addEntity(hologram, CreatureSpawnEvent.SpawnReason.CUSTOM);
        } else {
            world.getChunkAt(chunkX, chunkZ).a(hologram);
            world.entityList.add(hologram);
            try {
                validate.invoke(world, hologram);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        entities.put(i, hologram);
    }

    /**
     * Shows all lines of the hologram to all players.
     */
    @Override
    public void show() {
        if (!isCreated()) return;

        for (int i = 0; i < entities.size(); i++) {
            show(i);
        }
    }

    /**
     * Shows a line of the hologram to all players.
     *
     * @param line The line.
     */
    @Override
    public void show(int line) {
        if (!isCreated()) return;

        Bukkit.getOnlinePlayers().forEach(player -> show(player, line));
    }

    /**
     * Shows a hologram to a player.
     * @param player The player who will see the hologram.
     */
    @Override
    public void show(Player player) {
        if (!isCreated()) return;

        for (int i = 0; i < entities.size(); i++) {
            show(player, i);
        }
    }

    /**
     * Shows a line of the hologram to a player.
     * @param player The player who will see the hologram.
     * @param line The line to show.
     */
    @Override
    public void show(Player player, int line) {
        if (!isCreated()) return;

        CustomArmorStand entity = entities.get(line);
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entity);
        NMSUtils.sendPacketSync(NMSUtils.toNMS(player), packet);

        DataWatcher dataWatcher = entity.getDataWatcher();

        dataWatcher.set(DataWatcherRegistry.a.a(0), (byte) 0x20);

        IChatBaseComponent customName = entity.getCustomName();
        if (customName != null) {
            dataWatcher.set(DataWatcherRegistry.f.a(2), Optional.of(customName));
            dataWatcher.set(DataWatcherRegistry.i.a(3), true);
        }

        dataWatcher.set(DataWatcherRegistry.i.a(5), true);
        dataWatcher.set(DataWatcherRegistry.a.a(11), (byte) (0x01 | 0x08 | 0x10));

        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entity.getId(), dataWatcher, true);
        NMSUtils.sendPacket(NMSUtils.toNMS(player), metadata);
    }

    /**
     * Hides all lines of the hologram from all players.
     */
    @Override
    public void hide() {
        if (!isCreated()) return;

        for (int i = 0; i < entities.size(); i++) {
            hide(i);
        }
    }

    /**
     * Hides a line of the hologram from all players.
     *
     * @param line The line.
     */
    @Override
    public void hide(int line) {
        if (!isCreated()) return;

        Bukkit.getOnlinePlayers().forEach(player -> hide(player, line));
    }

    /**
     * Hides the hologram from a player.
     * @param player The player who no longer can see the hologram.
     */
    @Override
    public void hide(Player player) {
        if (!isCreated()) return;

        for (int i = 0; i < entities.size(); i++) {
            hide(player, i);
        }
    }

    /**
     * Hides a line of the hologram from a player.
     * @param player The player who no longer can see the line of the hologram.
     * @param line The line to hide.
     */
    @Override
    public void hide(Player player, int line) {
        if (!isCreated()) return;

        EntityArmorStand entity = entities.get(line);
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entity.getId());
        NMSUtils.sendPacket(NMSUtils.toNMS(player), packet);
    }

    /**
     * Destroys the hologram.
     */
    @Override
    public void destroy() {
        for (int i = 0; i < lines.size(); i++) {
            destroy(i);
        }
    }

    /**
     * Destroys a line of the hologram.
     * @param line The line.
     */
    @Override
    public void destroy(int line) {
        if (!isCreated()) return;

        hide(line);

        CustomArmorStand entity = entities.get(line);
        entity.world.removeEntity(entity);

        entity.dead = true;

        entities.remove(line);
        if (entities.isEmpty()) {
            created = false;
        }
    }

    /**
     * Updates all the lines of the hologram.
     * @apiNote Teleport and setLine methods already call this method.
     */
    @Override
    public void update() {
        for (Integer integer : entities.keySet()) {
            update(integer);
        }
    }

    /**
     * Updates a line of the hologram.
     * @param line The line.
     */
    @Override
    public void update(int line) {
        if (!isCreated()) return;

        CustomArmorStand entity = entities.get(line);
        if (!ChatColor.stripColor(Objects.requireNonNull(entity.getCustomName()).getText()).equalsIgnoreCase(ChatColor.stripColor(lines.get(line)))) {
            entity.setHologramStandName(ChatColor.translateAlternateColorCodes('&', lines.get(line)));
        }
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), false);
        NMSUtils.sendPacket(packet);
    }

    /**
     * Creates a new timer for the hologram.
     * @param seconds The time of the timer.
     * @return The created timer.
     */
    @Override
    public HologramTimer createTimer(int seconds) {
        this.timer = new HologramTimer(seconds);
        return timer;
    }

    /**
     * Teleports the hologram.
     * @param location The location to teleport the hologram to.
     */
    @Override
    public void teleport(Location location) {
        if (!isCreated()) return;

        this.location = location.clone();
        for (int i = 0; i < lines.size(); i++) {
            CustomArmorStand entity = entities.get(i);
            entity.setPos(location.getX(), location.getY() - (i * 0.23), location.getZ());
            PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(entity);
            NMSUtils.sendPacket(packet);
        }
    }

    /**
     * @return True if the hologram is created.
     */
    @Override
    public boolean isCreated() {
        return created;
    }

    /**
     * @return The location of the hologram.
     */
    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLines(List<String> lines) {
        this.lines = lines;
        if (timer != null) {
            timer.setLines(lines);
        }
        update();
    }

    /**
     * Sets all the lines of the hologram.
     * @param text The new lines.
     */
    @Override
    public void setLines(String... text) {
        setLines(Arrays.asList(text));
    }

    /**
     * Sets a line of the hologram.
     * @param line The line number.
     * @param text The text.
     */
    @Override
    public void setLine(int line, String text) {
        lines.set(line, text);
        update(line);
    }

    /**
     * Adds a line to the hologram.
     * @param text The text to add.
     */
    public void addLine(String text) {
        lines.add(text);
        update(lines.size() - 1);
    }

    /**
     * @return All the lines.
     */
    @Override
    public List<String> getLines() {
        return lines;
    }

    @Override
    public List<LivingEntity> toBukkitEntities() {
        List<LivingEntity> bukkitEntities = new ArrayList<>();
        entities.values().forEach(entity -> bukkitEntities.add((LivingEntity) entity.getBukkitEntity()));
        return bukkitEntities;
    }

    public class HologramTimer implements com.venom.venomcore.nms.core.hologram.HologramTimer {
        private boolean freeze = false;
        private boolean repeating = false;
        private boolean stop = false;

        private int seconds;
        private Status status;
        private BukkitTask task;

        private List<String> lines;
        private final HashMap<Status, String> messages = new HashMap<>();
        private Consumer<Status> onStatusChange;

        private int day;
        private int hours;
        private int minutes;
        private int sec;

        public HologramTimer(int seconds) {
            this.seconds = seconds;
            messages.put(Status.FREEZE, "&bDonduruldu!");
            messages.put(Status.COUNTDOWN, "&eDevam ediyor!");
            messages.put(Status.STOPPED, "&cDurduruldu!");
            messages.put(Status.OVER, "&cBitti!");
        }

        public void start() {
            this.lines = getLines();
            setStatus(Status.COUNTDOWN);
            stop = false;
            task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                // Check if freezed. If so, return.
                if (isFreezed()) {
                    setStatus(Status.FREEZE);
                    return;
                }

                // Check if the timer ended.
                if (seconds == 0) {
                    sec = sec - 1;
                    setStatus(Status.OVER);
                    task.cancel();
                    task = null;
                    if (repeating) {
                        Bukkit.getScheduler().runTaskLater(plugin, this::start, 3);
                    }
                    return;
                }

                // The timer is not ended and is not freezed. We can proceed.
                setStatus(Status.COUNTDOWN);

                // Calculate remaining days, hours, minutes and seconds.
                day = seconds / 86400;
                hours = (seconds / 3600) % 24;
                minutes = (seconds / 60) % 60;
                sec = seconds % 60;

                // Update the hologram.
                updateLines();

                // Reduce the seconds by one.
                setSeconds(this.seconds - 1);
            }, 0L, 20L);
        }

        public void stop() {
            setStatus(Status.STOPPED);
            task.cancel();
            stop = true;
        }

        private void setLines(List<String> lines) {
            this.lines = lines;
        }

        private void setStatus(Status status) {
            this.status = status;
            updateLines();
            if (onStatusChange != null) {
                onStatusChange.accept(status);
            }
        }

        private void updateLines() {
            List<String> newLines = new ArrayList<>();
            lines.forEach(line -> {
                String replacedLine = line;
                replacedLine = ChatColor.translateAlternateColorCodes('&', replacedLine.replaceAll(Time.DAY.getPlaceholder(), Integer.toString(day)));
                replacedLine = ChatColor.translateAlternateColorCodes('&', replacedLine.replaceAll(Time.HOUR.getPlaceholder(), Integer.toString(hours)));
                replacedLine = ChatColor.translateAlternateColorCodes('&', replacedLine.replaceAll(Time.MINUTE.getPlaceholder(), Integer.toString(minutes)));
                replacedLine = ChatColor.translateAlternateColorCodes('&', replacedLine.replaceAll(Time.SECOND.getPlaceholder(), Integer.toString(sec)));
                replacedLine = ChatColor.translateAlternateColorCodes('&', replacedLine.replaceAll("%status%",ChatColor.translateAlternateColorCodes('&', messages.get(status))));
                newLines.add(replacedLine);
            });
            Hologram.this.lines = newLines;
            update();
        }

        public int getTime(Time time) {
            switch (time) {
                case DAY:
                    return seconds / 86400;
                case HOUR:
                    return (seconds / 3600) % 24;
                case MINUTE:
                    return (seconds / 60) % 60;
                case SECOND:
                    return seconds % 60;
            }
            return 0;
        }

        public Status getStatus() {
            return status;
        }

        @Override
        public void setOnStatusChange(Consumer<Status> action) {
            this.onStatusChange = action;
        }

        public void setStatusText(Status status, String message) {
            messages.put(status, message);
        }

        public int getSeconds() {
            return seconds;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds;
        }

        public void setRepeating(boolean repeating) {
            this.repeating = repeating;
        }

        public boolean isRepeating() {
            return repeating;
        }

        public void setFreezed(boolean freeze) {
            this.freeze = freeze;
        }

        public boolean isFreezed() {
            return freeze;
        }

        public boolean isStarted() {
            return task != null;
        }

        public boolean isStopped() {
            return stop;
        }
    }
}
