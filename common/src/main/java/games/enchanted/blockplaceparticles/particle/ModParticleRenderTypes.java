package games.enchanted.blockplaceparticles.particle;

import games.enchanted.blockplaceparticles.rendering.ModRenderTypes;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;

public class ModParticleRenderTypes {
    public static ParticleRenderType BACKFACE_TERRAIN_PARTICLE = new ParticleRenderType("BACKFACE_TERRAIN_PARTICLE", ModRenderTypes.translucentParticleBackface(TextureAtlas.LOCATION_BLOCKS));
}
