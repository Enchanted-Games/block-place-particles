package games.enchanted.blockplaceparticles.particle.option;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;

public class TintedParticleOption implements ParticleOptions {
    public static final int BRUSH_COLOUR = 0xcec5d6;
    public static final float BRUSH_VARIATION = 0.23F;
    public static final TintedParticleOption BRUSH_OPTION = new TintedParticleOption(ModParticleTypes.BRUSH_DUST, TintedParticleOption.BRUSH_COLOUR, TintedParticleOption.BRUSH_VARIATION, true);
    public static final TintedParticleOption BRUSH_SPECK_OPTION = new TintedParticleOption(ModParticleTypes.BRUSH_DUST_SPECK, TintedParticleOption.BRUSH_COLOUR, TintedParticleOption.BRUSH_VARIATION, true);

    public static final int REDSTONE = 0xf70000;
    public static final int REDSTONE_UNPOWERED = 0x660000;
    public static final TintedParticleOption REDSTONE_DUST_OPTION = new TintedParticleOption(ModParticleTypes.REDSTONE_DUST, TintedParticleOption.REDSTONE, 0.4F, true);
    public static final TintedParticleOption REDSTONE_DUST_UNPOWERED_OPTION = new TintedParticleOption(ModParticleTypes.REDSTONE_DUST, TintedParticleOption.REDSTONE_UNPOWERED, 0.2F, true);

    private final ParticleType<TintedParticleOption> type;
    private final int color;
    private final float variationAmount;
    private boolean uniformVariation;

    public TintedParticleOption(ParticleType<TintedParticleOption> type, int colour, float variationAmount) {
        this.type = type;
        this.color = colour;
        this.variationAmount = variationAmount;
        this.uniformVariation = false;
    }

    public TintedParticleOption(ParticleType<TintedParticleOption> type, int colour, float variationAmount, boolean uniformVariation) {
        this(type, colour, variationAmount);
        this.uniformVariation = uniformVariation;
    }

    private static Codec<TintedParticleOption> createCodec(ParticleType<TintedParticleOption> type) {
        return RecordCodecBuilder.create((RecordCodecBuilder.Instance<TintedParticleOption> instance) ->
            instance.group(
                ExtraCodecs.RGB_COLOR_CODEC.fieldOf("colour").forGetter(TintedParticleOption::getColor),
                Codec.FLOAT.optionalFieldOf("variation_amount", 0.06f).forGetter(TintedParticleOption::getVariationAmount),
                Codec.BOOL.optionalFieldOf("uniform_variation", false).forGetter(TintedParticleOption::getUniformVariation)
            )
            .apply(
                instance,
                (Integer colour, Float variation, Boolean uniformVariation) -> new TintedParticleOption(type, colour, variation, uniformVariation)
            )
        );
    }

    public static MapCodec<TintedParticleOption> codec(ParticleType<TintedParticleOption> type) {
        return createCodec(type).fieldOf("colour_options");
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, TintedParticleOption> streamCodec(ParticleType<TintedParticleOption> type) {
        return ByteBufCodecs.fromCodec(createCodec(type));
    }

    public @NotNull ParticleType<TintedParticleOption> getType() {
        return this.type;
    }

    public int getColor() {
        return this.color;
    }

    public float getVariationAmount() {
        return this.variationAmount;
    }

    public boolean getUniformVariation() {
        return this.uniformVariation;
    }
}