package games.enchanted.blockplaceparticles.particle.option;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class ParticleEmitterOptions implements ParticleOptions {
    private final ParticleType<ParticleEmitterOptions> type;
    private final int tickIterations;
    private final int tickInterval;
    private final int particlesPerEmission;
    private final boolean emitOnFirstTick;
    private final Vector3f dimensions;
    private final Vector3f velocityVariance;

    public ParticleEmitterOptions(ParticleType<ParticleEmitterOptions> type, int tickIterations, int tickInvertal, int particlesPerEmission, boolean emitOnFirstTick, Vector3f dimensions, Vector3f velocityVariance) {
        this.type = type;
        this.tickIterations = tickIterations;
        this.tickInterval = tickInvertal;
        this.particlesPerEmission = particlesPerEmission;
        this.emitOnFirstTick = emitOnFirstTick;
        this.dimensions = dimensions;
        this.velocityVariance = velocityVariance;
    }

    public ParticleEmitterOptions(ParticleType<ParticleEmitterOptions> type, int tickIterations, int tickInvertal, int particlesPerEmission) {
        this(type, tickIterations, tickInvertal, particlesPerEmission, true, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
    }

    public ParticleEmitterOptions(ParticleType<ParticleEmitterOptions> type, int tickIterations, int tickInvertal, int particlesPerEmission, Vector3f dimensions) {
        this(type, tickIterations, tickInvertal, particlesPerEmission, true, dimensions, new Vector3f(0, 0, 0));
    }

    public ParticleEmitterOptions(ParticleType<ParticleEmitterOptions> type, int tickIterations, int tickInvertal, int particlesPerEmission, Vector3f dimensions, Vector3f velocityVariance) {
        this(type, tickIterations, tickInvertal, particlesPerEmission, true, dimensions, velocityVariance);
    }

    private static Codec<ParticleEmitterOptions> createCodec(ParticleType<ParticleEmitterOptions> type) {
        return RecordCodecBuilder.create((RecordCodecBuilder.Instance<ParticleEmitterOptions> instance) ->
            instance.group(
                ExtraCodecs.POSITIVE_INT.fieldOf("repeat").forGetter(ParticleEmitterOptions::getTickIterations),
                ExtraCodecs.POSITIVE_INT.fieldOf("interval").forGetter(ParticleEmitterOptions::getTickInterval),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("amount_per_emission", 1).forGetter(ParticleEmitterOptions::getParticlesPerEmission),
                Codec.BOOL.optionalFieldOf("emit_on_first_tick", true).forGetter(ParticleEmitterOptions::getEmitOnFirstTick),
                ExtraCodecs.VECTOR3F.optionalFieldOf("dimensions", new Vector3f(0f, 0f, 0f)).forGetter(ParticleEmitterOptions::getDimensions),
                ExtraCodecs.VECTOR3F.optionalFieldOf("velocity_variance", new Vector3f(0f, 0f, 0f)).forGetter(ParticleEmitterOptions::getVelocityVariance)
            ).apply(
                instance,
                (Integer tickIterations, Integer tickInterval, Integer particlesPerEmission, Boolean emitOnFirstTick, Vector3f dimensions, Vector3f velocityVariance) -> new ParticleEmitterOptions(type, tickIterations, tickInterval, particlesPerEmission, emitOnFirstTick, dimensions, velocityVariance)
            )
        );
    }

    public static MapCodec<ParticleEmitterOptions> codec(ParticleType<ParticleEmitterOptions> type) {
        return createCodec(type).fieldOf("emitter_options");
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, ParticleEmitterOptions> streamCodec(ParticleType<ParticleEmitterOptions> type) {
        return ByteBufCodecs.fromCodec(createCodec(type));
    }

    @Override
    public @NotNull ParticleType<ParticleEmitterOptions> getType() {
        return this.type;
    }

    public int getTickIterations() {
        return tickIterations;
    }

    public int getTickInterval() {
        return tickInterval;
    }

    public int getParticlesPerEmission() {
        return particlesPerEmission;
    }

    public boolean getEmitOnFirstTick() {
        return emitOnFirstTick;
    }

    public Vector3f getDimensions() {
        return dimensions;
    }

    public Vector3f getVelocityVariance() {
        return velocityVariance;
    }
}
