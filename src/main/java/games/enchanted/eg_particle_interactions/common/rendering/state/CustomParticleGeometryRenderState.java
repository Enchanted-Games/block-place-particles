package games.enchanted.eg_particle_interactions.common.rendering.state;

import com.mojang.blaze3d.systems.RenderPass;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ParticleFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.state.ParticleGroupRenderState;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureManager;
import org.jetbrains.annotations.Nullable;

public class CustomParticleGeometryRenderState implements SubmitNodeCollector.ParticleGroupRenderer, ParticleGroupRenderState {
    @Override
    public @Nullable QuadParticleRenderState.PreparedBuffers prepare(ParticleFeatureRenderer.ParticleBufferCache particleBufferCache) {
        return null;
    }

    @Override
    public void render(QuadParticleRenderState.PreparedBuffers preparedBuffers, ParticleFeatureRenderer.ParticleBufferCache particleBufferCache, RenderPass renderPass, TextureManager textureManager, boolean bl) {

    }

    @Override
    public void submit(SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {

    }
}
