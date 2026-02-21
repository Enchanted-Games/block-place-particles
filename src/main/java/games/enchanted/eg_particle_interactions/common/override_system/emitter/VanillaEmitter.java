package games.enchanted.eg_particle_interactions.common.override_system.emitter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

public class VanillaEmitter extends Emitter {
    public static final MapCodec<VanillaEmitter> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Codec.DOUBLE.optionalFieldOf(Emitter.VELOCITY_MULTIPLIER_NAME, Emitter.VELOCITY_MULTIPLIER_DEFAULT).forGetter(Emitter::getVelocityMultiplier),
            ParticleTypes.CODEC.fieldOf("particle").forGetter(VanillaEmitter::getParticleOptions)
        ).apply(
            instance,
            (velocityMultiplier, particleOptions) -> new VanillaEmitter(particleOptions, velocityMultiplier)
        )
    );

    protected final ParticleOptions particleOptions;

    public VanillaEmitter(ParticleOptions particleOptions, double velocityMultiplier) {
        super(velocityMultiplier);
        this.particleOptions = particleOptions;
    }

    @Override
    public void spawnParticle(ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        if(this.particleOptions == null) return;
        context.level().addParticle(
            this.particleOptions,
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

    protected ParticleOptions getParticleOptions() {
        return this.particleOptions;
    }
}
