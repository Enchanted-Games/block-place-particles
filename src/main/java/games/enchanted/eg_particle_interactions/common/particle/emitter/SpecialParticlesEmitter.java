package games.enchanted.eg_particle_interactions.common.particle.emitter;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleTypesRegistry;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearanceManager;
import games.enchanted.eg_particle_interactions.common.particle.types.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.ParticleSpawner;
import org.joml.Vector3d;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class SpecialParticlesEmitter extends Emitter {
    public static final MapCodec<SpecialParticlesEmitter> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            ModCodecs.COMPACT_VECTOR3D.optionalFieldOf(Emitter.VELOCITY_MULTIPLIER_NAME, Emitter.VELOCITY_MULTIPLIER_DEFAULT).forGetter(Emitter::getVelocityMultiplier),
            ParticleTypesRegistry.CODEC.fieldOf("particle").forGetter(SpecialParticlesEmitter::getParticleOptions)
        ).apply(
            instance,
            SpecialParticlesEmitter::new
        )
    );

    final PIParticleOptions particleOptions;

    public SpecialParticlesEmitter(Vector3d velocityMultiplier, PIParticleOptions particleOptions) {
        super(velocityMultiplier);
        this.particleOptions = particleOptions;
    }

    @Override
    public void spawnParticle(ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        ParticleSpawner.spawnWithAppearance(
            this.particleOptions,
            ParticleAppearance.MISSING_APPEARANCE.get(),
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
}
