package games.enchanted.eg_particle_interactions.common.resource.texture_source.colour;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;
import net.minecraft.client.Minecraft;

public class BlockTextureColourSource implements ColourSource {
    final int tintIndex;

    public BlockTextureColourSource(int tintIndex) {
        this.tintIndex = tintIndex;
    }

    @Override
    public int[] getARGB(ParticleContext context) {
        if(context.blockContext() == null || this.tintIndex < 0) return new int[]{255, 255, 255, 255};

        ParticleContext.BlockContext bContext = context.blockContext();
        int tintColour = Minecraft.getInstance().getBlockColors().getColor(bContext.state(), context.level(), bContext.pos(), this.tintIndex);
        int[] tintColourARGB = ColourUtil.RGBint_to_ARGB(tintColour);
        return ColourUtil.getRandomBlockColour(bContext.state(), tintColourARGB);
    }
}
