package games.enchanted.eg_particle_interactions.common.override_system;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class Emitter {
    public static final Codec<Emitter> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.DOUBLE.optionalFieldOf("velocity_multiplier", 1.0).forGetter(Emitter::getVelocityMultiplier),
            ParticleTypes.CODEC.optionalFieldOf("particle").forGetter(Emitter::getParticleOptions)
        ).apply(
            instance,
            (velocityMultiplier, particleOptions) -> new Emitter(particleOptions.orElse(null), velocityMultiplier)
        )
    );

    private final @Nullable ParticleOptions particleOptions;
    private final double velocityMultiplier;

    public Emitter(@Nullable ParticleOptions options, double velocityMultiplier) {
        this.particleOptions = options;
        this.velocityMultiplier = velocityMultiplier;
    }

    public void spawnParticle(ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        if(this.particleOptions == null) return;
        context.level().addParticle(this.particleOptions, x, y, z, xSpeed * this.velocityMultiplier, ySpeed * this.velocityMultiplier, zSpeed * this.velocityMultiplier);
    }

    protected double getVelocityMultiplier() {
        return this.velocityMultiplier;
    }

    protected Optional<ParticleOptions> getParticleOptions() {
        return Optional.<ParticleOptions>ofNullable(this.particleOptions);
    }
}
