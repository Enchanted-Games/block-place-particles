package games.enchanted.eg_particle_interactions.common.rendering.particle;

import net.minecraft.client.particle.ParticleRenderType;

//? if minecraft: <= 1.21.8 {
/*import net.minecraft.client.renderer.texture.TextureAtlas;
import games.enchanted.eg_particle_interactions.common.rendering.ModRenderTypes;
*///?}

public class ModParticleRenderTypes {
    //? if minecraft: <= 1.21.8 {
    /*public static ParticleRenderType BACKFACE_TERRAIN_PARTICLE = new ParticleRenderType("BACKFACE_TERRAIN_PARTICLE", ModRenderTypes.translucentParticleBackface(TextureAtlas.LOCATION_BLOCKS));
    *///?} else {
    public static ParticleRenderType CUSTOM_GEOMETRY = new ParticleRenderType("CUSTOM_GEOMETRY");
    //?}
}
