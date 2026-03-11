package games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface ParticleAccessor {
    @Accessor("stoppedByCollision")
    boolean eg_particle_interactions$getStoppedByCollision();
}
