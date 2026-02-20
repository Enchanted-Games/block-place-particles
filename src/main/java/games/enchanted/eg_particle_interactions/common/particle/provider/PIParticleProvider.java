package games.enchanted.eg_particle_interactions.common.particle.provider;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.resource.texture_source.TextureSource;
import net.minecraft.client.particle.Particle;
import org.jspecify.annotations.Nullable;

public interface PIParticleProvider<T extends PIParticleOptions> {
    @Nullable
    Particle createParticle(T options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ParticleContext context, @Nullable TextureSource textureSource);
}
