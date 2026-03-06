package games.enchanted.eg_particle_interactions.common.particle.appearance.colour;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ColourCodecs;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;

public class VariableColourSource implements ColourSource {
    public static final MapCodec<VariableColourSource> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            ColourCodecs.HEX_OR_ARGB_LIST_CODEC.fieldOf("base_colour").forGetter(VariableColourSource::getBaseArgb),
            Codec.floatRange(0, 1).fieldOf("variation").forGetter(VariableColourSource::getVariation),
            Codec.BOOL.optionalFieldOf("uniform", true).forGetter(VariableColourSource::isUniform)
        ).apply(
            instance,
            VariableColourSource::new
        )
    );

    final int baseARGB;
    final float variation;
    final boolean uniform;

    public VariableColourSource(int baseARGB, float variation, boolean uniform) {
        this.baseARGB = baseARGB;
        this.variation = variation;
        this.uniform = uniform;
    }

    protected int getBaseArgb() {
        return this.baseARGB;
    }

    protected float getVariation() {
        return this.variation;
    }

    protected boolean isUniform() {
        return this.uniform;
    }

    @Override
    public int[] getARGB(ParticleContext context) {
        if(this.isUniform()) {
            return ColourUtil.randomiseNegativeUniform(ColourUtil.ARGBint_to_ARGB(this.getBaseArgb()), this.getVariation());
        }
        return ColourUtil.randomiseNegative(ColourUtil.ARGBint_to_ARGB(this.getBaseArgb()), this.getVariation());
    }

    @Override
    public MapCodec<? extends ColourSource> codec() {
        return CODEC;
    }
}
