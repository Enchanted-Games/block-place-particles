//? if minecraft: >= 26.2 {
package games.enchanted.eg_particle_interactions.common.particle.render.feature.mc26_2;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.textures.FilterMode;
import games.enchanted.eg_particle_interactions.common.particle.render.state.mc26_2.CustomParticleGeometryRenderState;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.StagedVertexBuffer;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FeatureRenderer;
import net.minecraft.client.renderer.feature.FeatureRendererType;
import net.minecraft.client.renderer.feature.submit.SubmitNode;
import net.minecraft.client.renderer.oit.OitStage;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class CustomParticleGeometryFeatureRenderer implements FeatureRenderer<CustomParticleGeometryFeatureRenderer.Submit> {
    public static final FeatureRendererType<CustomParticleGeometryFeatureRenderer.Submit> TYPE = FeatureRendererType.create("Custom Geometry Particle");
    private final List<CustomParticleGeometryFeatureRenderer.PreparedGroup> groups = new ArrayList<>();
    @Nullable
    private GpuBufferSlice dynamicTransforms;

    @Override
    public void prepareGroup(final FeatureFrameContext context, final List<CustomParticleGeometryFeatureRenderer.Submit> submits, final boolean strictlyOrdered) {
        if(submits.isEmpty()) return;

        StagedVertexBuffer stagedVertexBuffer = context.stagedVertexBuffer();
        Map<SingleQuadParticle.Layer, StagedVertexBuffer.Draw> drawByLayer = new IdentityHashMap<>();

        for (CustomParticleGeometryFeatureRenderer.Submit submit : submits) {
            CustomParticleGeometryRenderState particles = submit.particles();
            if(particles.isEmpty()) continue;

            for (SingleQuadParticle.Layer layer : particles.layers()) {
                if(layer.translucent() != submit.translucent()) continue;

                StagedVertexBuffer.Draw draw = drawByLayer.computeIfAbsent(
                    layer, _ -> stagedVertexBuffer.appendDraw(DefaultVertexFormat.PARTICLE, PrimitiveTopology.QUADS, null)
                );
                particles.buildLayer(layer, stagedVertexBuffer.getVertexBuilder(draw));
            }
        }

        boolean translucent = submits.getFirst().translucent();
        this.groups.add(new CustomParticleGeometryFeatureRenderer.PreparedGroup(drawByLayer, translucent));
    }

    @Override
    public void finishPrepare(final FeatureFrameContext context) {
        this.dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(RenderSystem.getModelViewMatrixCopy());
    }

    @Override
    public void executeGroup(
        final FeatureFrameContext context,
        final @Nullable OitStage stage,
        final RenderPass renderPass,
        final int groupIndex,
        final List<CustomParticleGeometryFeatureRenderer.Submit> submits,
        final boolean strictlyOrdered
    ) {
        CustomParticleGeometryFeatureRenderer.PreparedGroup group = this.groups.get(groupIndex);

        RenderSystem.bindDefaultUniforms(renderPass);
        renderPass.setUniform("DynamicTransforms", Objects.requireNonNull(this.dynamicTransforms));
        renderPass.setUniform("Sampler2", context.lightmap(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
        drawLayers(context.stagedVertexBuffer(), group.layers, renderPass, context.textureManager(), stage);
    }

    private static void drawLayers(
        final StagedVertexBuffer stagedBuffer,
        final Map<SingleQuadParticle.Layer, StagedVertexBuffer.Draw> layers,
        final RenderPass renderPass,
        final TextureManager textureManager,
        final @Nullable OitStage stage
    ) {
        for (Map.Entry<SingleQuadParticle.Layer, StagedVertexBuffer.Draw> entry : layers.entrySet()) {
            StagedVertexBuffer.ExecuteInfo executeInfo = stagedBuffer.getExecuteInfo(entry.getValue());
            if(executeInfo == null) continue;

            renderPass.setPipeline(
                RenderSystem.getCompiledPipeline(stage != null ? getOitPipeline(stage, entry.getKey()) : (entry.getKey()).pipeline())
            );
            renderPass.setVertexBuffer(0, executeInfo.vertexBuffer().slice());
            renderPass.setIndexBuffer(executeInfo.indexBuffer(), executeInfo.indexType());
            AbstractTexture texture = textureManager.getTexture(entry.getKey().textureAtlasLocation());
            renderPass.setUniform("Sampler0", texture.getTextureView(), texture.getSampler());
            renderPass.drawIndexed(executeInfo.indexCount(), 1, executeInfo.firstIndex(), executeInfo.baseVertex(), 0);
        }
    }

    private static RenderPipeline getOitPipeline(final OitStage stage, final SingleQuadParticle.Layer layer) {
        if (layer.oitPipelineSet() == null) {
            throw new IllegalStateException("[Particle Interactions]: No OIT pipeline set for particle layer: " + layer);
        } else {
            return layer.oitPipelineSet().getPipeline(stage);
        }
    }

    @Override
    public void finishExecute(final FeatureFrameContext context) {
        this.groups.clear();
        this.dynamicTransforms = null;
    }

    private record PreparedGroup(Map<SingleQuadParticle.Layer, StagedVertexBuffer.Draw> layers, boolean translucent) {
    }

    public record Submit(CustomParticleGeometryRenderState particles, boolean translucent) implements SubmitNode {
        @Override
        public FeatureRendererType<Submit> featureType() {
            return TYPE;
        }
    }
}
//? }