package games.enchanted.eg_particle_interactions.common.particle.option;

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
import org.joml.Vector3f;

public class RandomDistributionEmitterOptions implements ParticleOptions {
    private final ParticleType<RandomDistributionEmitterOptions> type;
    private final int repeat;
    private final int tickInterval;
    private final int particlesPerEmission;
    private final boolean emitOnFirstTick;
    private final Vector3f dimensions;
    private final Vector3f velocityVariance;

    public RandomDistributionEmitterOptions(ParticleType<RandomDistributionEmitterOptions> type, int tickIterations, int tickInvertal, int particlesPerEmission, boolean emitOnFirstTick, Vector3f dimensions, Vector3f velocityVariance) {
        this.type = type;
        this.repeat = tickIterations;
        this.tickInterval = tickInvertal;
        this.particlesPerEmission = particlesPerEmission;
        this.emitOnFirstTick = emitOnFirstTick;
        this.dimensions = dimensions;
        this.velocityVariance = velocityVariance;
    }

    public RandomDistributionEmitterOptions(ParticleType<RandomDistributionEmitterOptions> type, int repeat, int tickInvertal, int particlesPerEmission) {
        this(type, repeat, tickInvertal, particlesPerEmission, true, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
    }

    public RandomDistributionEmitterOptions(ParticleType<RandomDistributionEmitterOptions> type, int repeat, int tickInvertal, int particlesPerEmission, Vector3f dimensions) {
        this(type, repeat, tickInvertal, particlesPerEmission, true, dimensions, new Vector3f(0, 0, 0));
    }

    public RandomDistributionEmitterOptions(ParticleType<RandomDistributionEmitterOptions> type, int repeat, int tickInvertal, int particlesPerEmission, Vector3f dimensions, Vector3f velocityVariance) {
        this(type, repeat, tickInvertal, particlesPerEmission, true, dimensions, velocityVariance);
    }

    private static Codec<RandomDistributionEmitterOptions> createCodec(ParticleType<RandomDistributionEmitterOptions> type) {
        return RecordCodecBuilder.create((RecordCodecBuilder.Instance<RandomDistributionEmitterOptions> instance) ->
            instance.group(
                ExtraCodecs.POSITIVE_INT.fieldOf("repeat").forGetter(RandomDistributionEmitterOptions::getRepeat),
                ExtraCodecs.POSITIVE_INT.fieldOf("interval").forGetter(RandomDistributionEmitterOptions::getTickInterval),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("amount_per_emission", 1).forGetter(RandomDistributionEmitterOptions::getParticlesPerEmission),
                Codec.BOOL.optionalFieldOf("emit_on_first_tick", true).forGetter(RandomDistributionEmitterOptions::getEmitOnFirstTick),
                ExtraCodecs.VECTOR3F.optionalFieldOf("dimensions", new Vector3f(0f, 0f, 0f)).forGetter(RandomDistributionEmitterOptions::getDimensions),
                ExtraCodecs.VECTOR3F.optionalFieldOf("velocity_variance", new Vector3f(0f, 0f, 0f)).forGetter(RandomDistributionEmitterOptions::getVelocityVariance)
            ).apply(
                instance,
                (Integer tickIterations, Integer tickInterval, Integer particlesPerEmission, Boolean emitOnFirstTick, Vector3f dimensions, Vector3f velocityVariance) -> new RandomDistributionEmitterOptions(type, tickIterations, tickInterval, particlesPerEmission, emitOnFirstTick, dimensions, velocityVariance)
            )
        );
    }

    public static MapCodec<RandomDistributionEmitterOptions> codec(ParticleType<RandomDistributionEmitterOptions> type) {
        return createCodec(type).fieldOf("emitter_options");
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, RandomDistributionEmitterOptions> streamCodec(ParticleType<RandomDistributionEmitterOptions> type) {
        return ByteBufCodecs.fromCodec(createCodec(type));
    }

    @Override
    public @NotNull ParticleType<RandomDistributionEmitterOptions> getType() {
        return this.type;
    }

    public int getRepeat() {
        return repeat;
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
