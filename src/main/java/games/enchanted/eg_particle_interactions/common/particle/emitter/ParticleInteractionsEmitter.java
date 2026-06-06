package games.enchanted.eg_particle_interactions.common.particle.emitter;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.ParticleTypesRegistry;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearanceManager;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.util.ParticleSpawner;
import org.joml.Vector3d;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class ParticleInteractionsEmitter extends Emitter {
    public static final MapCodec<ParticleInteractionsEmitter> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            ModCodecs.COMPACT_VECTOR3D.optionalFieldOf(Emitter.VELOCITY_MULTIPLIER_NAME, Emitter.VELOCITY_MULTIPLIER_DEFAULT).forGetter(Emitter::getVelocityMultiplier),
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

    public ParticleInteractionsEmitter(Vector3d velocityMultiplier, PIParticleOptions particleOptions, ParticleAppearance.@Nullable Reference appearance) {
        super(velocityMultiplier);
        this.particleOptions = particleOptions;
        this.appearance = appearance;
    }

    public static Emitter defaultAppearance(double velocityMultiplierScalar, PIParticleOptions options) {
        return new ParticleInteractionsEmitter(new Vector3d(velocityMultiplierScalar), options, null);
    }

    public static Emitter defaultAppearance(Vector3d velocityMultiplier, PIParticleOptions options) {
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
            xSpeed * this.getVelocityMultiplier().x(),
            ySpeed * this.getVelocityMultiplier().y(),
            zSpeed * this.getVelocityMultiplier().z()
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
