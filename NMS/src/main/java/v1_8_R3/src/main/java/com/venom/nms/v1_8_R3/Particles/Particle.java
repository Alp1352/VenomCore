package com.venom.nms.v1_8_R3.Particles;


import com.venom.nms.core.Particle.ParticleCore;
import com.venom.nms.v1_8_R3.NMSUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.World;

/**
 * @apiNote List of ids : https://wiki.vg/index.php?title=Protocol&oldid=7368#Particle_2
 */
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

        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(nmsParticle, force, x, y, z, offSetX, offSetY, offSetZ, data, count, length);
        NMSUtils.sendPacket(packet);
    }
}
