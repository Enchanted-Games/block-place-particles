package games.enchanted.eg_particle_interactions.common.particle.util;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.ParticleTypesRegistry;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearanceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class ParticleSpawner {
    public static void spawn(PIParticleOptions options, ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        spawn(options, context, null, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    public static void spawn(PIParticleOptions options, ParticleContext context, @Nullable ParticleAppearance appearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        spawnParticle(options, context, appearance, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @SuppressWarnings("unchecked")
    private static <T extends PIParticleOptions> void spawnParticle(
        final T options,
        ParticleContext context,
        @Nullable ParticleAppearance appearance,
        double x,
        double y,
        double z,
        double xSpeed,
        double ySpeed,
        double zSpeed
    ) {
        PIParticleType<T> type = (PIParticleType<T>) options.type();
        if(type == null) {
            throw new IllegalStateException("Particle type was null, options: " + options);
        }
        PIParticleProvider<T> provider = ParticleTypesRegistry.getProviderOrThrow(type);
        Identifier particleId = ParticleTypesRegistry.getIdOrThrow(type);

        Particle particle = provider.createParticle(
            options,
            type.components(),
            appearance != null ? appearance : ParticleAppearanceManager.get(particleId),
            context,
            x,
            z,
            y,
            xSpeed,
            ySpeed,
            zSpeed
        );
        if(particle != null) {
            Minecraft.getInstance().particleEngine.add(particle);
        }
    }
}
