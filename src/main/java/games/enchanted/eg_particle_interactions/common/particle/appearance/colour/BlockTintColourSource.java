package games.enchanted.eg_particle_interactions.common.particle.appearance.colour;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ColourCodecs;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ExtraCodecs;

public class BlockTintColourSource implements ColourSource {
    public static final MapCodec<BlockTintColourSource> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("tint_index", 0).forGetter(BlockTintColourSource::getTintIndex),
            ColourCodecs.HEX_OR_ARGB_LIST_CODEC.optionalFieldOf("fallback", 0xffffffff).forGetter(BlockTintColourSource::getFallbackARGB)
        ).apply(
            instance,
            BlockTintColourSource::new
        )
    );

    final int tintIndex;
    final int fallbackARGB;

    public BlockTintColourSource(int tintIndex, int fallbackARGB) {
        this.tintIndex = tintIndex;
        this.fallbackARGB = fallbackARGB;
    }

    protected int getTintIndex() {
        return this.tintIndex;
    }

    protected int getFallbackARGB() {
        return this.fallbackARGB;
    }

    @Override
    public int[] getARGB(ParticleContext context) {
        if(context.blockContext() == null || this.tintIndex < 0) return ColourUtil.ARGBint_to_ARGB(this.fallbackARGB);

        ParticleContext.BlockContext bContext = context.blockContext();
        int tintColour = Minecraft.getInstance().getBlockColors().getColor(bContext.state(), context.level(), bContext.pos(), this.tintIndex);
        return ColourUtil.RGBint_to_ARGB(tintColour);
    }

    @Override
    public MapCodec<? extends ColourSource> codec() {
        return CODEC;
    }
}
