package games.enchanted.eg_particle_interactions.common.particle.emitter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearanceManager;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.definition.ParticleDefinition;
import games.enchanted.eg_particle_interactions.common.particle.definition.ParticleDefinitionManager;
import games.enchanted.eg_particle_interactions.common.particle.util.ParticleSpawner;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class ParticleInteractionsNewEmitter extends Emitter {
    public static final MapCodec<ParticleInteractionsNewEmitter> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Codec.DOUBLE.optionalFieldOf(Emitter.VELOCITY_MULTIPLIER_NAME, Emitter.VELOCITY_MULTIPLIER_DEFAULT).forGetter(Emitter::getVelocityMultiplier),
            ParticleDefinitionManager.REFERENCE_CODEC.fieldOf("particle").forGetter(ParticleInteractionsNewEmitter::getParticleDefinition),
            ParticleAppearanceManager.INLINE_OR_ID_CODEC.optionalFieldOf("appearance").forGetter(particleInteractionsEmitter -> Optional.ofNullable(particleInteractionsEmitter.getAppearance()))
        ).apply(
            instance,
            (
                velocityMultiplier,
                particleOptions,
                appearance
            ) -> new ParticleInteractionsNewEmitter(
                velocityMultiplier,
                particleOptions,
                appearance.orElse(null)
            )
        )
    );

    final ParticleDefinition.Reference particleDefinition;
    final @Nullable ParticleAppearance appearance;

    public ParticleInteractionsNewEmitter(double velocityMultiplier, ParticleDefinition.Reference particleDefinition, @Nullable ParticleAppearance appearance) {
        super(velocityMultiplier);
        this.particleDefinition = particleDefinition;
        this.appearance = appearance;
    }

    public static Emitter defaultAppearance(double velocityMultiplier, ParticleDefinition.Reference particleDefinition) {
        return new ParticleInteractionsNewEmitter(velocityMultiplier, particleDefinition, null);
    }

    @Override
    public void spawnParticle(ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        ParticleSpawner.spawn(
            this.particleDefinition.get(),
            ParticleComponentMap.EMPTY,
            this.appearance,
            context,
            x,
            y,
            z,
            xSpeed * this.getVelocityMultiplier(),
            ySpeed * this.getVelocityMultiplier(),
            zSpeed * this.getVelocityMultiplier()
        );
    }

    @Override
    public MapCodec<? extends Emitter> codec() {
        return CODEC;
    }

    protected ParticleDefinition.Reference getParticleDefinition() {
        return this.particleDefinition;
    }

    protected @Nullable ParticleAppearance getAppearance() {
        return this.appearance;
    }
}
