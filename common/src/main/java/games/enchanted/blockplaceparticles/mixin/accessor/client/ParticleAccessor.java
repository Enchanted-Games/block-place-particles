package games.enchanted.blockplaceparticles.mixin.accessor.client;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface ParticleAccessor {
    @Accessor("stoppedByCollision")
    boolean block_place_particle$getStoppedByCollision();
}
