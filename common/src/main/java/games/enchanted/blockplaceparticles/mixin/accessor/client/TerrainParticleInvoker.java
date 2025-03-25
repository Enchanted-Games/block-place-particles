package games.enchanted.blockplaceparticles.mixin.accessor.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.particles.BlockParticleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TerrainParticle.class)
public interface TerrainParticleInvoker {
    @Invoker("createTerrainParticle")
    static TerrainParticle block_place_particle$invokeCreateTerrainParticle(BlockParticleOption particleOption, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        throw new AssertionError();
    }
}
