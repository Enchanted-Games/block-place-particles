package games.enchanted.eg_particle_interactions.common.particle.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.EmitterRuleSet;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.EmitterRuleSetManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class RandomDistributionEmitterOptions implements PIParticleOptions {
    private final PIParticleType<RandomDistributionEmitterOptions> type;
    private final int repeat;
    private final int tickInterval;
    private final int particlesPerEmission;
    private final boolean emitOnFirstTick;
    private final Vector3f dimensions;
    private final Vector3f velocityVariance;
    private final EmitterRuleSet emitterRuleSet;

    public RandomDistributionEmitterOptions(PIParticleType<RandomDistributionEmitterOptions> type, int tickIterations, int tickInvertal, int particlesPerEmission, boolean emitOnFirstTick, Vector3fc dimensions, Vector3fc velocityVariance, EmitterRuleSet emitterRuleSet) {
        this.type = type;
        this.repeat = tickIterations;
        this.tickInterval = tickInvertal;
        this.particlesPerEmission = particlesPerEmission;
        this.emitOnFirstTick = emitOnFirstTick;
        this.dimensions = new Vector3f(dimensions);
        this.velocityVariance = new Vector3f(velocityVariance);
        this.emitterRuleSet = emitterRuleSet;
    }

    public RandomDistributionEmitterOptions(PIParticleType<RandomDistributionEmitterOptions> type, int repeat, int tickInvertal, int particlesPerEmission, EmitterRuleSet emitterRuleSet) {
        this(type, repeat, tickInvertal, particlesPerEmission, true, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), emitterRuleSet);
    }

    public RandomDistributionEmitterOptions(PIParticleType<RandomDistributionEmitterOptions> type, int repeat, int tickInvertal, int particlesPerEmission, Vector3f dimensions, EmitterRuleSet emitterRuleSet) {
        this(type, repeat, tickInvertal, particlesPerEmission, true, dimensions, new Vector3f(0, 0, 0), emitterRuleSet);
    }

    private static Codec<RandomDistributionEmitterOptions> createCodec(PIParticleType<RandomDistributionEmitterOptions> type) {
        return RecordCodecBuilder.create((RecordCodecBuilder.Instance<RandomDistributionEmitterOptions> instance) ->
            instance.group(
                ExtraCodecs.POSITIVE_INT.fieldOf("repeat").forGetter(RandomDistributionEmitterOptions::getRepeat),
                ExtraCodecs.POSITIVE_INT.fieldOf("interval").forGetter(RandomDistributionEmitterOptions::getTickInterval),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("amount_per_emission", 1).forGetter(RandomDistributionEmitterOptions::getParticlesPerEmission),
                Codec.BOOL.optionalFieldOf("emit_on_first_tick", true).forGetter(RandomDistributionEmitterOptions::getEmitOnFirstTick),
                ExtraCodecs.VECTOR3F.optionalFieldOf("dimensions", new Vector3f(0f, 0f, 0f)).forGetter(RandomDistributionEmitterOptions::getDimensions),
                ExtraCodecs.VECTOR3F.optionalFieldOf("velocity_variance", new Vector3f(0f, 0f, 0f)).forGetter(RandomDistributionEmitterOptions::getVelocityVariance),
                EmitterRuleSetManager.INLINE_OR_ID_CODEC.fieldOf("emitter").forGetter(RandomDistributionEmitterOptions::getEmitterRuleSet)
            ).apply(
                instance,
                (
                    Integer tickIterations,
                    Integer tickInterval,
                    Integer particlesPerEmission,
                    Boolean emitOnFirstTick,
                    Vector3fc dimensions,
                    Vector3fc velocityVariance,
                    EmitterRuleSet emitterRuleSet
                ) -> new RandomDistributionEmitterOptions(
                    type,
                    tickIterations,
                    tickInterval,
                    particlesPerEmission,
                    emitOnFirstTick,
                    dimensions,
                    velocityVariance,
                    emitterRuleSet
                )
            )
        );
    }

    public static MapCodec<RandomDistributionEmitterOptions> codec(PIParticleType<RandomDistributionEmitterOptions> type, ParticleConfig defaultConfig) {
        return createCodec(type).fieldOf("emitter_options");
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, RandomDistributionEmitterOptions> streamCodec(PIParticleType<RandomDistributionEmitterOptions> type, ParticleConfig defaultConfig) {
        return ByteBufCodecs.fromCodec(createCodec(type));
    }

    @Override
    public @NotNull PIParticleType<RandomDistributionEmitterOptions> type() {
        return this.type;
    }

    @Override
    public ParticleConfig config() {
        return ParticleConfig.DEFAULT;
    }

    public static String idPrefix() {
        return "distribution_emitter";
    }

    public int getRepeat() {
        return this.repeat;
    }

    public int getTickInterval() {
        return this.tickInterval;
    }

    public int getParticlesPerEmission() {
        return this.particlesPerEmission;
    }

    public boolean getEmitOnFirstTick() {
        return this.emitOnFirstTick;
    }

    public Vector3f getDimensions() {
        return this.dimensions;
    }

    public Vector3f getVelocityVariance() {
        return this.velocityVariance;
    }

    public EmitterRuleSet getEmitterRuleSet() {
        return this.emitterRuleSet;
    }
}
