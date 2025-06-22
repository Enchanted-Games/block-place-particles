package games.enchanted.eg_particle_interactions.common.particle;

import games.enchanted.eg_particle_interactions.common.rendering.ModRenderTypes;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;

public class ModParticleRenderTypes {
    public static ParticleRenderType BACKFACE_TERRAIN_PARTICLE = new ParticleRenderType("BACKFACE_TERRAIN_PARTICLE", ModRenderTypes.translucentParticleBackface(TextureAtlas.LOCATION_BLOCKS));
}
