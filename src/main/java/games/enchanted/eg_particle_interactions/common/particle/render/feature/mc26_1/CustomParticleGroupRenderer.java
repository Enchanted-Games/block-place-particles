//? if minecraft: < 26.2 {
/*package games.enchanted.eg_particle_interactions.common.particle.render.feature.mc26_1;

import com.mojang.blaze3d.systems.RenderPass;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureManager;
import org.jspecify.annotations.Nullable;

public interface CustomParticleGroupRenderer {
    boolean isEmpty();

    QuadParticleRenderState.@Nullable PreparedBuffers prepare(CustomParticleGeometryFeatureRenderer.BufferCache buffer, boolean translucent);

    void render(
        QuadParticleRenderState.PreparedBuffers buffers,
        CustomParticleGeometryFeatureRenderer.BufferCache bufferCache,
        RenderPass renderPass,
        TextureManager textureManager
    );
}
*///?}
