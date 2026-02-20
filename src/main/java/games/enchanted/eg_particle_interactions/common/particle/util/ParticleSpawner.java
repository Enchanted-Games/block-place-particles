package games.enchanted.eg_particle_interactions.common.particle.util;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import games.enchanted.eg_particle_interactions.common.registry.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.registry.particle.ParticleTypes;
import games.enchanted.eg_particle_interactions.common.resource.texture_source.TextureSourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.resources.Identifier;

public class ParticleSpawner {
    public static void spawn(PIParticleOptions options, ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        spawnParticle(options, context, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @SuppressWarnings("unchecked")
    private static  <T extends PIParticleOptions> void spawnParticle(
        final T options, ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed
    ) {
        PIParticleType<T> type = (PIParticleType<T>) options.type();
        PIParticleProvider<T> provider = ParticleTypes.getProvider(type);
        Identifier particleId = ParticleTypes.getId(type);

        Particle particle = provider.createParticle(
            options,
            x,
            y,
            z,
            xSpeed,
            ySpeed,
            zSpeed,
            context,
            TextureSourceManager.get(particleId)
        );
        if(particle != null) {
            Minecraft.getInstance().particleEngine.add(particle);
        }
    }
}
