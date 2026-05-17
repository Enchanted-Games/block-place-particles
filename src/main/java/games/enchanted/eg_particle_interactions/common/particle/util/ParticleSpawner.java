package games.enchanted.eg_particle_interactions.common.particle.util;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponents;
import games.enchanted.eg_particle_interactions.common.particle.component.type.GravityComponent;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.options.value.RandomFloatProvider;
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
    public static void spawnWithDefaultAppearance(
        PIParticleOptions options,
        ParticleContext context,
        double x,
        double y,
        double z,
        double xSpeed,
        double ySpeed,
        double zSpeed
    ) {
        spawn(options, null, context, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    public static void spawn(
        PIParticleOptions options,
        @Nullable ParticleAppearance appearance,
        ParticleContext context,
        double x,
        double y,
        double z,
        double xSpeed,
        double ySpeed,
        double zSpeed
    ) {
        spawn(options, ParticleComponentMap.EMPTY, appearance, context, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    public static void spawn(
        PIParticleOptions options,
        ParticleComponentMap components,
        @Nullable ParticleAppearance appearance,
        ParticleContext context,
        double x,
        double y,
        double z,
        double xSpeed,
        double ySpeed,
        double zSpeed
    ) {
        spawnParticle(options, components, appearance, context, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @SuppressWarnings("unchecked")
    private static <T extends PIParticleOptions> void spawnParticle(
        final T options,
        ParticleComponentMap components,
        @Nullable ParticleAppearance appearance,
        ParticleContext context,
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
        ParticleComponentMap combinedComponentMap = ParticleComponentMap.Builder.combine(type.defaultComponents(), components);

        Particle particle = provider.createParticle(
            options,
            combinedComponentMap,
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
