package com.venom.nms.common.Particles;

import com.venom.nms.core.Particle.ParticleCore;
import org.bukkit.World;

public class Particle implements ParticleCore {
    @Override
    public void spawnParticle(String particle, boolean force, float x, float y, float z, World world, float offSetX, float offSetY, float offSetZ, int data, int count, int... length) {
        org.bukkit.Particle bukkitParticle = org.bukkit.Particle.valueOf(particle);
        world.spawnParticle(bukkitParticle, x, y, z, count, offSetX, offSetY, offSetZ, length[0], data);
    }
}
