//? if minecraft: < 26.2 {
/*package games.enchanted.eg_particle_interactions.common.particle.render.feature.mc26_1;

import com.mojang.blaze3d.systems.RenderPass;
import games.enchanted.eg_particle_interactions.common.particle.render.state.mc26_1.CustomParticleGeometryRenderState;
import net.minecraft.client.renderer.texture.TextureManager;
import org.jspecify.annotations.Nullable;

public interface CustomParticleGroupRenderer {
    boolean isEmpty();

    CustomParticleGeometryRenderState.PreparedBufferPair prepare(CustomParticleGeometryFeatureRenderer.BufferCache buffer, boolean translucent);

    void render(
        CustomParticleGeometryRenderState.PreparedBufferPair buffers,
        CustomParticleGeometryFeatureRenderer.BufferCache bufferCache,
        RenderPass renderPass,
        TextureManager textureManager
    );
}
*///?}
