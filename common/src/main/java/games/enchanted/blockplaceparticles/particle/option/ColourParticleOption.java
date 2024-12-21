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

public class ColourParticleOption implements ParticleOptions {
    private final ParticleType<ColourParticleOption> type;
    private final int color;
    private final float variation;

    public ColourParticleOption(ParticleType<ColourParticleOption> type, int colour, float variation) {
        this.type = type;
        this.color = colour;
        this.variation = variation;
    }

    private static Codec<ColourParticleOption> createCodec(ParticleType<ColourParticleOption> type) {
        return RecordCodecBuilder.create((RecordCodecBuilder.Instance<ColourParticleOption> instance) ->
            instance.group(
                ExtraCodecs.RGB_COLOR_CODEC.fieldOf("colour").forGetter(ColourParticleOption::getColor),
                Codec.FLOAT.optionalFieldOf("random_variation", 0.06f).forGetter(ColourParticleOption::getVariation)
            )
            .apply(
                instance,
                (Integer colour, Float variation) -> new ColourParticleOption(type, colour, variation)
            )
        );
    }

    public static MapCodec<ColourParticleOption> codec(ParticleType<ColourParticleOption> type) {
        return createCodec(type).fieldOf("colour_options");
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, ColourParticleOption> streamCodec(ParticleType<ColourParticleOption> type) {
        return ByteBufCodecs.fromCodec(createCodec(type));
    }

    public @NotNull ParticleType<ColourParticleOption> getType() {
        return this.type;
    }

    public int getColor() {
        return this.color;
    }

    public float getVariation() {
        return this.variation;
    }
}