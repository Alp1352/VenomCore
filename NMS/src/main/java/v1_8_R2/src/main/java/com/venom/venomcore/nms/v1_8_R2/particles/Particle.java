package com.venom.venomcore.nms.v1_8_R2.particles;

import com.venom.venomcore.nms.core.particle.ParticleCore;
import com.venom.venomcore.nms.v1_8_R2.NMSUtils;
import net.minecraft.server.v1_8_R2.EnumParticle;
import net.minecraft.server.v1_8_R2.PacketPlayOutWorldParticles;
import org.bukkit.World;

public class Particle implements ParticleCore {
    @Override
    public void spawnParticle(String particle, boolean force, float x, float y, float z, World world, float offSetX, float offSetY, float offSetZ, int data, int count) {
        EnumParticle nmsParticle = EnumParticle.valueOf(particle);

        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(nmsParticle,force, x, y, z, offSetX, offSetY, offSetZ, data, count);
        NMSUtils.sendPacket(packet);
    }
}
