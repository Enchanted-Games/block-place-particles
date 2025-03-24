package games.enchanted.blockplaceparticles.rendering;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;

import java.util.function.Function;

public abstract class ModRenderTypes extends RenderType {
    public ModRenderTypes(String p_173178_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }

    private static final Function<ResourceLocation, RenderType> TRANSLUCENT_PARTICLE_BACKFACE = Util.memoize(ModRenderTypes::createTranslucentParticleBackface);

    private static RenderType createTranslucentParticleBackface(ResourceLocation location) {
//        RenderType.CompositeState renderState = RenderType.CompositeState.builder()
//            .setShaderState(PARTICLE_SHADER)
//            .setTextureState(new RenderStateShard.TextureStateShard(location, TriState.FALSE, false))
//            .setCullState(RenderStateShard.NO_CULL)
//            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
//            .setOutputState(PARTICLES_TARGET)
//            .setLightmapState(LIGHTMAP)
//            .setWriteMaskState(COLOR_DEPTH_WRITE)
//            .createCompositeState(false);
        RenderType.CompositeState renderState = RenderType.CompositeState.builder()
            .setTextureState(new RenderStateShard.TextureStateShard(location, TriState.FALSE, false))
            .setLightmapState(LIGHTMAP).createCompositeState(false);
        return RenderType.create("translucent_particle_backface", 1536, false, false, RenderPipelines.TRANSLUCENT_PARTICLE, renderState);
    }

    public static RenderType translucentParticleBackface(ResourceLocation location) {
        return TRANSLUCENT_PARTICLE_BACKFACE.apply(location);
    }
}
