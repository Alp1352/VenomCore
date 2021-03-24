package com.venom.nms.v1_8_R1.Particles;

import com.venom.nms.core.Particle.ParticleCore;
import com.venom.nms.v1_8_R1.NMSUtils;
import net.minecraft.server.v1_8_R1.EnumParticle;
import net.minecraft.server.v1_8_R1.PacketPlayOutWorldParticles;
import org.bukkit.World;

public class Particle implements ParticleCore {
    private static final EnumParticle[] VALUES = EnumParticle.values();

    @Override
    public void spawnParticle(String particle, boolean force, float x, float y, float z, World world, float offSetX, float offSetY, float offSetZ, int data, int count, int... length) {
        EnumParticle nmsParticle = null;

        for (EnumParticle value : VALUES) {
            if (value.b().equals(particle)) {
                nmsParticle = value;
                break;
            }
        }
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(nmsParticle,force, x, y, z, offSetX, offSetY, offSetZ, data, count, length);
        NMSUtils.sendPacket(packet);
    }
}
