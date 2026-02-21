package games.enchanted.eg_particle_interactions.common.particle.appearance.colour;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;
import net.minecraft.client.Minecraft;

public class BlockTintColourSource implements ColourSource {
    final int tintIndex;

    public BlockTintColourSource(int tintIndex) {
        this.tintIndex = tintIndex;
    }

    @Override
    public int[] getARGB(ParticleContext context) {
        if(context.blockContext() == null || this.tintIndex < 0) return new int[]{255, 255, 255, 255};

        ParticleContext.BlockContext bContext = context.blockContext();
        int tintColour = Minecraft.getInstance().getBlockColors().getColor(bContext.state(), context.level(), bContext.pos(), this.tintIndex);
        return ColourUtil.RGBint_to_ARGB(tintColour);
    }
}
