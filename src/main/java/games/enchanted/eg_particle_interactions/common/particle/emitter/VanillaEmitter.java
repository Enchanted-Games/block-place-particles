package games.enchanted.eg_particle_interactions.common.particle.emitter;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import org.joml.Vector3d;

public class VanillaEmitter extends Emitter {
    public static final MapCodec<VanillaEmitter> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            ModCodecs.COMPACT_VECTOR3D.optionalFieldOf(Emitter.VELOCITY_MULTIPLIER_NAME, Emitter.VELOCITY_MULTIPLIER_DEFAULT).forGetter(Emitter::getVelocityMultiplier),
            ModCodecs.VECTOR3D.optionalFieldOf(Emitter.POSITION_OFFSET_NAME, Emitter.POSITION_OFFSET_DEFAULT).forGetter(Emitter::getVelocityMultiplier),
            ParticleTypes.CODEC.fieldOf("particle").forGetter(VanillaEmitter::getParticleOptions)
        ).apply(
            instance,
            VanillaEmitter::new
        )
    );

    protected final ParticleOptions particleOptions;

    public VanillaEmitter(Vector3d velocityMultiplier, Vector3d positionOffset, ParticleOptions particleOptions) {
        super(velocityMultiplier, positionOffset);
        this.particleOptions = particleOptions;
    }

    @Override
    protected void emit(ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        context.level().addParticle(
            this.particleOptions,
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

    protected ParticleOptions getParticleOptions() {
        return this.particleOptions;
    }
}
