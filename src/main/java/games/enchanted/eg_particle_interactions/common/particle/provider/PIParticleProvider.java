package games.enchanted.eg_particle_interactions.common.particle.provider;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import net.minecraft.client.particle.Particle;
import org.jspecify.annotations.Nullable;

public interface PIParticleProvider<T extends PIParticleOptions> {
    @Nullable
    Particle createParticle(T options, ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed);
}
