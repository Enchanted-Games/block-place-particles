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

public class ArcEmitterOptions implements ParticleOptions {
    private final ParticleType<ArcEmitterOptions> type;
    private final int length;
    private final boolean canSpawnSplits;

    public ArcEmitterOptions(ParticleType<ArcEmitterOptions> type, int length) {
        this.type = type;
        this.length = length;
        this.canSpawnSplits = true;
    }

    public ArcEmitterOptions(ParticleType<ArcEmitterOptions> type, int length, boolean canSpawnSplits) {
        this.type = type;
        this.length = length;
        this.canSpawnSplits = canSpawnSplits;
    }

    public ArcEmitterOptions withSplitsDisabled() {
        return new ArcEmitterOptions(this.type, this.length, false);
    }

    private static Codec<ArcEmitterOptions> createCodec(ParticleType<ArcEmitterOptions> type) {
        return RecordCodecBuilder.create((RecordCodecBuilder.Instance<ArcEmitterOptions> instance) ->
            instance.group(
                ExtraCodecs.POSITIVE_INT.fieldOf("length").forGetter(ArcEmitterOptions::getLength)
            ).apply(
                instance,
                (Integer length) -> new ArcEmitterOptions(type, length)
            )
        );
    }

    public static MapCodec<ArcEmitterOptions> codec(ParticleType<ArcEmitterOptions> type) {
        return createCodec(type).fieldOf("emitter_options");
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, ArcEmitterOptions> streamCodec(ParticleType<ArcEmitterOptions> type) {
        return ByteBufCodecs.fromCodec(createCodec(type));
    }

    @Override
    public @NotNull ParticleType<ArcEmitterOptions> getType() {
        return this.type;
    }

    public int getLength() {
        return length;
    }

    public boolean canSpawnSplits() {
        return canSpawnSplits;
    }
}
