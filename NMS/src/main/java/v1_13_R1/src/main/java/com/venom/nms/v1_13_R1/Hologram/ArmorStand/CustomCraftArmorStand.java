package com.venom.nms.v1_13_R1.Hologram.ArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R1.CraftServer;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class CustomCraftArmorStand extends CraftArmorStand {

    public CustomCraftArmorStand(CraftServer server, CustomArmorStand entity) {
        super(server, entity);
    }

    // CraftArmorStand.java
    @Override
    public void setArms(boolean arms) {
        // Disabled.
    }

    @Override
    public void setBasePlate(boolean basePlate) {
        // Disabled.
    }

    @Override
    public void setBodyPose(EulerAngle pose) {
        // Disabled.
    }

    @Override
    public void setBoots(ItemStack item) {
        // Disabled.
    }

    @Override
    public void setChestplate(ItemStack item) {
        // Disabled.
    }

    @Override
    public void setGravity(boolean gravity) {
        // Disabled.
    }

    @Override
    public void setHeadPose(EulerAngle pose) {
        // Disabled.
    }

    @Override
    public void setHelmet(ItemStack item) {
        // Disabled.
    }

    @Override
    public void setItemInHand(ItemStack item) {
        // Disabled.
    }

    @Override
    public void setLeftArmPose(EulerAngle pose) {
        // Disabled.
    }

    @Override
    public void setLeftLegPose(EulerAngle pose) {
        // Disabled.
    }

    @Override
    public void setLeggings(ItemStack item) {
        // Disabled.
    }

    @Override
    public void setRightArmPose(EulerAngle pose) {
        // Disabled.
    }

    @Override
    public void setRightLegPose(EulerAngle pose) {
        // Disabled.
    }

    @Override
    public void setSmall(boolean small) {
        // Disabled.
    }

    @Override
    public void setVisible(boolean visible) {
        // Disabled.
    }

    // CraftLivingEntity.java
    @Override
    public void setRemoveWhenFarAway(boolean remove) {
        // Disabled.
    }

    // CraftEntity.java
    // make the entity invulnerable
    @Override
    public void remove() {
        // Disabled.
    }

    // lock location
    @Override
    public boolean teleport(Location loc) {
        // Disabled.
        return false;
    }
    @Override
    public boolean teleport(Entity entity) {
        // Disabled.
        return false;
    }
    @Override
    public boolean teleport(Location loc, PlayerTeleportEvent.TeleportCause cause) {
        // Disabled.
        return false;
    }
    @Override
    public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause cause) {
        // Disabled.
        return false;
    }

    // passengers
    @Override
    public boolean setPassenger(Entity entity) {
        // Disabled.
        return false;
    }
    @Override
    public boolean eject() {
        // Disabled.
        return false;
    }
    @Override
    public boolean leaveVehicle() {
        // Disabled.
        return false;
    }

    // lock the custom name
    @Override
    public void setCustomName(String name) {
        // Disabled.
    }
    @Override
    public void setCustomNameVisible(boolean flag) {
        // Disabled.
    }
}
