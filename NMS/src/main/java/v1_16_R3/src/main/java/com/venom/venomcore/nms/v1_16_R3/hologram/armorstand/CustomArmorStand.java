package com.venom.venomcore.nms.v1_16_R3.hologram.armorstand;

import com.venom.venomcore.nms.v1_16_R3.NMSUtils;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;

public class CustomArmorStand extends EntityArmorStand implements HologramArmorStand {

    private CraftEntity entity;
    private String customName;

    public CustomArmorStand(World world) {
        super(EntityTypes.ARMOR_STAND, world);

        setInvisible(true);
        setSmall(true);
        setNoGravity(false);
        setBasePlate(true);
        setArms(true);
        setMarker(true);

        super.a(new EmptyBoundingBox()); // Set an empty bounding box.

        this.onGround = true;
    }

    @Override
    public boolean a_(int i, ItemStack itemstack) {
        // Prevent equipping the armor stand.
        return false;
    }

    @Override
    public EnumInteractionResult a(EntityHuman entityhuman, Vec3D vec3d, EnumHand enumHand) {
        // Prevent interaction with this entity.
        return EnumInteractionResult.PASS;
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        // Prevent saving nbt tags.
    }

    @Override
    public boolean a_(NBTTagCompound nbttagcompound) {
        // Prevent saving nbt tags.
        return false;
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        // Prevent saving nbt tags.
        return nbttagcompound;
    }

    @Override
    public boolean d(NBTTagCompound nbttagcompound) {
        // Prevent saving nbt tags.
        return false;
    }

    @Override
    public void load(NBTTagCompound nbttagcompound) {
        // Prevent loading nbt tags.
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        // Prevent loading nbt tags.
    }

    @Override
    public void a(AxisAlignedBB boundingBox) {
        // Lock the bounding box.
    }

    @Override
    public boolean isInvulnerable(DamageSource source) {
        // Make the entity invulnerable.
        return true;
    }

    @Override
    public void setCustomName(IChatBaseComponent s) {
        // Lock the custom name.
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
        // Lock the custom name.
    }

    @Override
    public void die() {
        // Make the entity invulnerable.
    }

    @Override
    public void tick() {
        // Disable entity ticking for this entity.
        // Force EntityTrackerEntry to send a teleport packet immediately after spawning this entity.
        if (this.onGround) {
            this.onGround = false;
        }
    }

    @Override
    public void inactiveTick() {
        // Disable entity ticking for this entity.
        // Force EntityTrackerEntry to send a teleport packet immediately after spawning this entity.
        if (this.onGround) {
            this.onGround = false;
        }
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (this.entity == null) {
            // Set the bukkit entity.
            // We are using a custom implementation to override bukkit methods.
            this.entity = new CustomCraftArmorStand(this.world.getServer(), this);
        }
        return this.entity;
    }

    @Override
    public void playSound(SoundEffect soundeffect, float f, float f1) {
        // Remove sounds from this entity.
    }

    @Override
    public void setHologramStandName(String name) {
        if (name != null && !name.isEmpty()) {
            super.setCustomNameVisible(true);
            super.setCustomName(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', name)));
            this.customName = name;
        }
    }

    @Override
    public int getStandID() {
        return super.getId();
    }

    @Override
    public void setPos(double x, double y, double z) {
        super.setPosition(x, y, z);
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(this);
        NMSUtils.sendPacket(packet);
    }

    @Override
    public void setPos(Location loc) {
        setPos(loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        // Since the entity is invulnerable, we can just return false.
        return false;
    }
}
