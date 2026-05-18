package games.enchanted.eg_particle_interactions.common.particle.behaviour;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import net.minecraft.client.particle.Particle;
import org.jspecify.annotations.Nullable;

public interface ParticleBehaviourProvider {
    @Nullable
    Particle createParticle(ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, double x, double z, double y, double xSpeed, double ySpeed, double zSpeed);
}
