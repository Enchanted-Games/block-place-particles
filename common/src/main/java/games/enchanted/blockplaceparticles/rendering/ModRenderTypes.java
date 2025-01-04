package games.enchanted.blockplaceparticles.rendering;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;

import java.util.function.Function;

public class ModRenderTypes extends RenderType {
    public ModRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    private static final Function<ResourceLocation, RenderType> TRANSLUCENT_PARTICLE_BACKFACE = Util.memoize(ModRenderTypes::createTranslucentParticleBackface);

    private static RenderType createTranslucentParticleBackface(ResourceLocation location) {
        RenderType.CompositeState renderState = RenderType.CompositeState.builder()
            .setShaderState(PARTICLE_SHADER)
            .setTextureState(new RenderStateShard.TextureStateShard(location, TriState.FALSE, false))
            .setCullState(RenderStateShard.NO_CULL)
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setOutputState(PARTICLES_TARGET)
            .setLightmapState(LIGHTMAP)
            .setWriteMaskState(COLOR_DEPTH_WRITE)
            .createCompositeState(false);
        return create("translucent_particle_backface", DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS, 1536, false, false, renderState);
    }

    public static RenderType translucentParticleBackface(ResourceLocation location) {
        return TRANSLUCENT_PARTICLE_BACKFACE.apply(location);
    }
}
