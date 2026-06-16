package games.enchanted.eg_particle_interactions.common.particle;

import games.enchanted.eg_particle_interactions.common.particle.types.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleTypesRegistry;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearanceManager;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponents;
import games.enchanted.eg_particle_interactions.common.particle.component.type.AppearanceComponent;
import games.enchanted.eg_particle_interactions.common.particle.definition.ParticleDefinition;
import games.enchanted.eg_particle_interactions.common.particle.definition.ParticleDefinitionManager;
import games.enchanted.eg_particle_interactions.common.particle.types.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.types.PIParticleProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class ParticleSpawner {
    public static void spawnWithAppearance(
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
        ParticleComponentMap components = ParticleComponentMap.EMPTY;
        if(appearance != null) {
            components = ParticleComponentMap.Builder.create()
                .set(ParticleComponents.APPEARANCE, new AppearanceComponent(new ParticleAppearance.InlineRef(appearance)))
                .build();
        }
        spawn(options, components, context, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    public static void spawn(
        PIParticleOptions options,
        ParticleComponentMap components,
        ParticleContext context,
        double x,
        double y,
        double z,
        double xSpeed,
        double ySpeed,
        double zSpeed
    ) {
        spawnParticle(options, components, context, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    public static void spawnWithDefaultComponents(
        ParticleDefinition definition,
        ParticleContext context,
        double x,
        double y,
        double z,
        double xSpeed,
        double ySpeed,
        double zSpeed
    ) {
        spawn(
            definition,
            ParticleComponentMap.EMPTY,
            context,
            x,
            y,
            z,
            xSpeed,
            ySpeed,
            zSpeed
        );
    }

    public static void spawn(
        ParticleDefinition definition,
        ParticleComponentMap customComponents,
        ParticleContext context,
        double x,
        double y,
        double z,
        double xSpeed,
        double ySpeed,
        double zSpeed
    ) {
        spawnParticle(definition, customComponents, context, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @SuppressWarnings("unchecked")
    private static <T extends PIParticleOptions> void spawnParticle(
        final T options,
        ParticleComponentMap components,
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
        AppearanceComponent appearance = combinedComponentMap.get(ParticleComponents.APPEARANCE);

        Particle particle = provider.createParticle(
            options,
            combinedComponentMap,
            appearance != null ? appearance.value().get() : ParticleAppearanceManager.get(particleId),
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

    private static void spawnParticle(
        ParticleDefinition definition,
        ParticleComponentMap customComponents,
        ParticleContext context,
        double x,
        double y,
        double z,
        double xSpeed,
        double ySpeed,
        double zSpeed
    ) {
        Identifier particleId = ParticleDefinitionManager.INSTANCE.getIdOrNull(definition);
        ParticleComponentMap combinedComponentMap = ParticleComponentMap.Builder.combine(definition.defaultComponents(), customComponents);
        AppearanceComponent appearanceComponent = combinedComponentMap.get(ParticleComponents.APPEARANCE);
        ParticleAppearance appearance;
        if(appearanceComponent != null) {
            appearance = appearanceComponent.value().get();
        } else if (particleId != null) {
            appearance = ParticleAppearanceManager.get(particleId);
        } else {
            appearance = ParticleAppearance.MISSING_APPEARANCE.get();
        }

        Particle particle = definition.behaviourProvider().createParticle(
            combinedComponentMap,
            appearance,
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
