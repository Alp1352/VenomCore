package com.venom.venomcore.nms.core.particle;

import org.bukkit.Location;
import org.bukkit.World;

public interface ParticleCore {
    void spawnParticle(String particle, boolean force, float x, float y, float z, World world, float offSetX, float offSetY, float offSetZ, int data, int count);

    default void spawnParticle(String particle, float x, float y, float z, World world, float offSetX, float offSetY, float offSetZ, int data, int count) {
        spawnParticle(particle, true, x, y, z, world, offSetX, offSetY, offSetZ, data, count);
    }

    default void spawnParticle(String particle, Location location, float offSetX, float offSetY, float offSetZ, int data, int count) {
        spawnParticle(particle, (float) location.getX(), (float) location.getY(), (float) location.getZ(), location.getWorld(), offSetX, offSetY, offSetZ, data, count);
    }

    default void spawnParticle(String particle, Location location, int data, int count) {
        spawnParticle(particle, location, 1,1,1, data, count);
    }

    default void spawnParticle(String particle, Location location, int count) {
        spawnParticle(particle, location, 0, count);
    }

    default void spawnParticle(String particle, Location location) {
        spawnParticle(particle, location, 1);
    }

}
