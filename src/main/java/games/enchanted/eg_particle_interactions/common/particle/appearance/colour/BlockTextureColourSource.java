package games.enchanted.eg_particle_interactions.common.particle.appearance.colour;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ColourCodecs;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;
import games.enchanted.eg_particle_interactions.common.resource.texture.TexturePalettes;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ExtraCodecs;

public class BlockTextureColourSource implements ColourSource {
    public static final MapCodec<BlockTextureColourSource> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("tint_index", -1).forGetter(BlockTextureColourSource::getTintIndex),
            ColourCodecs.HEX_OR_ARGB_LIST_CODEC.optionalFieldOf("fallback", 0xffffffff).forGetter(BlockTextureColourSource::getFallbackARGB)
        ).apply(
            instance,
            BlockTextureColourSource::new
        )
    );

    final int tintIndex;
    final int fallbackARGB;

    public BlockTextureColourSource(int tintIndex, int fallbackARGB) {
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
        if(context.blockContext() == null || this.tintIndex < -1) return ColourUtil.ARGBint_to_ARGB(this.fallbackARGB);

        ParticleContext.BlockContext bContext = context.blockContext();
        int tintColour;

        if(this.tintIndex == -1) {
            tintColour = 0xffffff;
        } else {
            var source = Minecraft.getInstance().getBlockColors().getTintSource(bContext.state(), this.tintIndex);
            if(source == null) {
                tintColour = 0xffffff;
            } else {
                tintColour = source.colorInWorld(bContext.state(), context.level(), context.pos());
            }
        }

        int[] tintColourARGB = ColourUtil.RGBint_to_ARGB(tintColour);
        return TexturePalettes.getRandomBlockColour(bContext.state(), tintColourARGB);
    }

    @Override
    public MapCodec<? extends ColourSource> codec() {
        return CODEC;
    }
}
