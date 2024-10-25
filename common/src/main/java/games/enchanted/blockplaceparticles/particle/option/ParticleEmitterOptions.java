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

public class ParticleEmitterOptions implements ParticleOptions {
    private final ParticleType<ParticleEmitterOptions> type;
    private final int tickIterations;
    private final int tickInterval;
    private final int particlesPerEmission;
    private final boolean emitOnFirstTick;
    private final float width;
    private final float height;
    private final float depth;

    public ParticleEmitterOptions(ParticleType<ParticleEmitterOptions> type, int tickIterations, int tickInvertal, int particlesPerEmission, boolean emitOnFirstTick, float width, float height, float depth) {
        this.type = type;
        this.tickIterations = tickIterations;
        this.tickInterval = tickInvertal;
        this.particlesPerEmission = particlesPerEmission;
        this.emitOnFirstTick = emitOnFirstTick;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    private static Codec<ParticleEmitterOptions> createCodec(ParticleType<ParticleEmitterOptions> type) {
        return RecordCodecBuilder.create((RecordCodecBuilder.Instance<ParticleEmitterOptions> instance) ->
            instance.group(
                ExtraCodecs.POSITIVE_INT.fieldOf("repeat").forGetter(ParticleEmitterOptions::getTickIterations),
                ExtraCodecs.POSITIVE_INT.fieldOf("interval").forGetter(ParticleEmitterOptions::getTickInterval),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("amount_per_emission", 1).forGetter(ParticleEmitterOptions::getParticlesPerEmission),
                Codec.BOOL.optionalFieldOf("emit_on_first_tick", true).forGetter(ParticleEmitterOptions::getEmitOnFirstTick),
                ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("width", 0f).forGetter(ParticleEmitterOptions::getWidth),
                ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("height", 0f).forGetter(ParticleEmitterOptions::getHeight),
                ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("depth", 0f).forGetter(ParticleEmitterOptions::getDepth)
            ).apply(
                instance,
                (Integer tickIterations, Integer tickInterval, Integer particlesPerEmission, Boolean emitOnFirstTick, Float width, Float height, Float depth) -> new ParticleEmitterOptions(type, tickIterations, tickInterval, particlesPerEmission, emitOnFirstTick, width, height, depth)
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

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getDepth() {
        return depth;
    }
}
