package games.enchanted.eg_particle_interactions.common.particle.emitter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.ParticleTypesRegistry;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearanceManager;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.util.ParticleSpawner;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class ParticleInteractionsEmitter extends Emitter {
    public static final MapCodec<ParticleInteractionsEmitter> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Codec.DOUBLE.optionalFieldOf(Emitter.VELOCITY_MULTIPLIER_NAME, Emitter.VELOCITY_MULTIPLIER_DEFAULT).forGetter(Emitter::getVelocityMultiplier),
            ParticleTypesRegistry.CODEC.fieldOf("particle").forGetter(ParticleInteractionsEmitter::getParticleOptions),
            ParticleAppearanceManager.referenceCodec().optionalFieldOf("appearance").forGetter(particleInteractionsEmitter -> Optional.ofNullable(particleInteractionsEmitter.getAppearance()))
        ).apply(
            instance,
            (
                velocityMultiplier,
                particleOptions,
                appearance
            ) -> new ParticleInteractionsEmitter(
                velocityMultiplier,
                particleOptions,
                appearance.orElse(null)
            )
        )
    );

    final PIParticleOptions particleOptions;
    final ParticleAppearance.@Nullable Reference appearance;

    public ParticleInteractionsEmitter(double velocityMultiplier, PIParticleOptions particleOptions, ParticleAppearance.@Nullable Reference appearance) {
        super(velocityMultiplier);
        this.particleOptions = particleOptions;
        this.appearance = appearance;
    }

    public static Emitter defaultAppearance(double velocityMultiplier, PIParticleOptions options) {
        return new ParticleInteractionsEmitter(velocityMultiplier, options, null);
    }

    @Override
    public void spawnParticle(ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        ParticleSpawner.spawnWithAppearance(
            this.particleOptions,
            this.appearance == null ? null : this.appearance.get(),
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

    protected PIParticleOptions getParticleOptions() {
        return this.particleOptions;
    }

    protected ParticleAppearance.@Nullable Reference getAppearance() {
        return this.appearance;
    }
}
