package games.enchanted.eg_particle_interactions.common.particle.appearance.colour;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ColourCodecs;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;
import games.enchanted.eg_particle_interactions.common.util.texture.TexturePalettes;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ExtraCodecs;

public class FluidTextureColourSource implements ColourSource {
    public static final MapCodec<FluidTextureColourSource> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            TexturePalettes.FluidStateMaterialSource.CODEC.optionalFieldOf("texture_type", TexturePalettes.FluidStateMaterialSource.STILL).forGetter(FluidTextureColourSource::getMaterialSource),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("tint_index", -1).forGetter(FluidTextureColourSource::getTintIndex),
            ColourCodecs.HEX_OR_ARGB_LIST_CODEC.optionalFieldOf("fallback", 0xffffffff).forGetter(FluidTextureColourSource::getFallbackARGB)
        ).apply(
            instance,
            FluidTextureColourSource::new
        )
    );

    final TexturePalettes.FluidStateMaterialSource materialSource;
    final int tintIndex;
    final int fallbackARGB;

    public FluidTextureColourSource(TexturePalettes.FluidStateMaterialSource materialSource, int tintIndex, int fallbackARGB) {
        this.materialSource = materialSource;
        this.tintIndex = tintIndex;
        this.fallbackARGB = fallbackARGB;
    }

    protected TexturePalettes.FluidStateMaterialSource getMaterialSource() {
        return this.materialSource;
    }

    protected int getTintIndex() {
        return this.tintIndex;
    }

    protected int getFallbackARGB() {
        return this.fallbackARGB;
    }

    @Override
    public int[] getARGB(ParticleContext context) {
        if(context.fluidContext() == null || this.tintIndex < -1) return ColourUtil.ARGBint_to_ARGB(this.fallbackARGB);

        ParticleContext.FluidContext fContext = context.fluidContext();
        int tintColour;

        if(this.tintIndex == -1) {
            tintColour = 0xffffff;
        } else {
            var source = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fContext.state()).tintSource();
            if(source == null) {
                tintColour = 0xffffff;
            } else {
                tintColour = source.colorInWorld(fContext.state().createLegacyBlock(), context.level(), context.pos());
            }
        }

        int[] tintColourARGB = ColourUtil.RGBint_to_ARGB(tintColour);
        return TexturePalettes.getRandomFluidColour(fContext.state(), tintColourARGB, this.materialSource);
    }

    @Override
    public MapCodec<? extends ColourSource> codec() {
        return CODEC;
    }
}
