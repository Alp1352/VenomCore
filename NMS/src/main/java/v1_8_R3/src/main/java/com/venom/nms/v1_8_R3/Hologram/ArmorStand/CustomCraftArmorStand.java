package com.venom.nms.v1_8_R3.Hologram.ArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
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
    public void setArms(boolean arms) { }

    @Override
    public void setBasePlate(boolean basePlate) { }

    @Override
    public void setBodyPose(EulerAngle pose) { }

    @Override
    public void setBoots(ItemStack item) { }

    @Override
    public void setChestplate(ItemStack item) { }

    @Override
    public void setGravity(boolean gravity) { }

    @Override
    public void setHeadPose(EulerAngle pose) { }

    @Override
    public void setHelmet(ItemStack item) { }

    @Override
    public void setItemInHand(ItemStack item) { }

    @Override
    public void setLeftArmPose(EulerAngle pose) { }

    @Override
    public void setLeftLegPose(EulerAngle pose) { }

    @Override
    public void setLeggings(ItemStack item) { }

    @Override
    public void setRightArmPose(EulerAngle pose) { }

    @Override
    public void setRightLegPose(EulerAngle pose) { }

    @Override
    public void setSmall(boolean small) { }

    @Override
    public void setVisible(boolean visible) { }

    // CraftLivingEntity.java
    @Override
    public void setRemoveWhenFarAway(boolean remove) { }

    // CraftEntity.java
    // make the entity invulnerable
    @Override
    public void remove() { }

    // lock location
    @Override
    public boolean teleport(Location loc) { return false; }
    @Override
    public boolean teleport(Entity entity) { return false; }
    @Override
    public boolean teleport(Location loc, PlayerTeleportEvent.TeleportCause cause) { return false; }
    @Override
    public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause cause) { return false; }

    // passengers
    @Override
    public boolean setPassenger(Entity entity) { return false; }
    @Override
    public boolean eject() { return false; }
    @Override
    public boolean leaveVehicle() { return false; }

    // lock the custom name
    @Override
    public void setCustomName(String name) { }
    @Override
    public void setCustomNameVisible(boolean flag) { }
}
