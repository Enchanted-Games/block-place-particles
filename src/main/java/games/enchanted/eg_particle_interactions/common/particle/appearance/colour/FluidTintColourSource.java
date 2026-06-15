package games.enchanted.eg_particle_interactions.common.particle.appearance.colour;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ColourCodecs;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class FluidTintColourSource implements ColourSource {
    public static final MapCodec<FluidTintColourSource> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("tint_index", 0).forGetter(FluidTintColourSource::getTintIndex),
            ColourCodecs.HEX_OR_ARGB_LIST_CODEC.optionalFieldOf("fallback", 0xffffffff).forGetter(FluidTintColourSource::getFallbackARGB)
        ).apply(
            instance,
            FluidTintColourSource::new
        )
    );

    final int tintIndex;
    final int fallbackARGB;

    public FluidTintColourSource(int tintIndex, int fallbackARGB) {
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
        if(context.fluidContext() == null || this.tintIndex < 0) return ColourUtil.ARGBint_to_ARGB(this.fallbackARGB);

        ParticleContext.FluidContext fContext = context.fluidContext();
        var source = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fContext.state()).tintSource();
        if(source == null) return ColourUtil.ARGBint_to_ARGB(this.fallbackARGB);

        int tintColour = source.colorInWorld(fContext.state().createLegacyBlock(), context.level(), context.pos());
        return ColourUtil.RGBint_to_ARGB(tintColour);
    }

    @Override
    public MapCodec<? extends ColourSource> codec() {
        return CODEC;
    }
}
