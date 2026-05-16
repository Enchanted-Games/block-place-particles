package games.enchanted.eg_particle_interactions.common.particle.provider;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import net.minecraft.client.particle.Particle;
import org.jspecify.annotations.Nullable;

public interface PIParticleProvider<T extends PIParticleOptions> {
    @Nullable
    Particle createParticle(T options, ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, double x, double z, double y, double xSpeed, double ySpeed, double zSpeed);
}
