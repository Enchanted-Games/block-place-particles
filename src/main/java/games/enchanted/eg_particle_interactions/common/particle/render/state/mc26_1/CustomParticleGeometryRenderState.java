//? if minecraft: >= 26.1 < 26.2 {
/*package games.enchanted.eg_particle_interactions.common.particle.render.state.mc26_1;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.QuadConsumer;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.QuadConsumerProvider;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.mc26_1.CustomParticleGeometryQuadConsumer;
import games.enchanted.eg_particle_interactions.common.particle.render.state.QuadStorage;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ParticleFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.Map;

public class CustomParticleGeometryRenderState implements SubmitNodeCollector.ParticleGroupRenderer, ParticleGroupRenderState, QuadConsumerProvider {
    private final Map<SingleQuadParticle.Layer, QuadStorage> quadStoragePerLayer = new HashMap<>();
    private int vertexAmount = 0;

    @Override
    public void clear() {
        this.quadStoragePerLayer.values().forEach(QuadStorage::clear);
        this.vertexAmount = 0;
    }

    @Override
    public @Nullable QuadParticleRenderState.PreparedBuffers prepare(ParticleFeatureRenderer.ParticleBufferCache particleBufferCache, boolean translucentOnly) {
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(this.vertexAmount * DefaultVertexFormat.PARTICLE.getVertexSize())) {
            BufferBuilder vertexBuffer = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);

            HashMap<SingleQuadParticle.Layer, QuadParticleRenderState.PreparedLayer> layerToPreparedMap = prepareLayers(vertexBuffer, translucentOnly);

            MeshData meshData = vertexBuffer.build();

            if (meshData == null) {
                return null;
            }

            particleBufferCache.write(meshData.vertexBuffer());
            RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS).getBuffer(meshData.drawState().indexCount());
            GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(
                RenderSystem.getModelViewMatrix(),
                new Vector4f(1.0f, 1.0f, 1.0f, 1.0f),
                new Vector3f(),
                new Matrix4f()
            );
            return new QuadParticleRenderState.PreparedBuffers(meshData.drawState().indexCount(), dynamicTransforms, layerToPreparedMap);
        }
    }

    private HashMap<SingleQuadParticle.Layer, QuadParticleRenderState.PreparedLayer> prepareLayers(BufferBuilder vertexBuffer, boolean translucentOnly) {
        HashMap<SingleQuadParticle.Layer, QuadParticleRenderState.PreparedLayer> layerToPreparedMap = new HashMap<>();
        int vertexOffset = 0;

        for (Map.Entry<SingleQuadParticle.Layer, QuadStorage> entry : this.quadStoragePerLayer.entrySet()) {
            if(entry.getKey().translucent() != translucentOnly) continue;

            QuadStorage storage = entry.getValue();

            storage.forEachVertex((x, y, z, u, v, packedLight, argb) -> vertexBuffer.addVertex(x, y, z).setUv(u, v).setColor(argb).setLight(packedLight));

            if (storage.vertexAmount() > 0) {
                layerToPreparedMap.put(entry.getKey(), new QuadParticleRenderState.PreparedLayer(vertexOffset, (int) (storage.vertexAmount() * 1.5)));
            }
            vertexOffset += storage.vertexAmount();
        }
        return layerToPreparedMap;
    }

    @Override
    public void render(QuadParticleRenderState.PreparedBuffers preparedBuffers, ParticleFeatureRenderer.ParticleBufferCache particleBufferCache, RenderPass renderPass, TextureManager textureManager) {
        RenderSystem.AutoStorageIndexBuffer quadIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
        renderPass.setVertexBuffer(0, particleBufferCache.get());
        renderPass.setIndexBuffer(quadIndexBuffer.getBuffer(preparedBuffers.indexCount()), quadIndexBuffer.type());
        renderPass.setUniform("DynamicTransforms", preparedBuffers.dynamicTransforms());
        RenderSystem.bindDefaultUniforms(renderPass);

        for (Map.Entry<SingleQuadParticle.Layer, QuadParticleRenderState.PreparedLayer> entry : preparedBuffers.layers().entrySet()) {
            renderPass.setPipeline(entry.getKey().pipeline());
            AbstractTexture atlas = textureManager.getTexture(entry.getKey().textureAtlasLocation());
            renderPass.bindTexture("Sampler0", atlas.getTextureView(), atlas.getSampler());
            renderPass.drawIndexed(entry.getValue().vertexOffset(), 0, entry.getValue().indexCount(), 1);
        }
    }

    @Override
    public void submit(SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (this.vertexAmount > 0) {
            submitNodeCollector.submitParticleGroup(this);
        }
    }

    @Override
    public boolean isEmpty() {
        return this.vertexAmount <= 0;
    }

    public void startQuad(SingleQuadParticle.Layer layer) {
        this.quadStoragePerLayer.computeIfAbsent(layer, (l) -> new QuadStorage()).startQuad();
    }
    public void finishQuad(SingleQuadParticle.Layer layer) {
        this.quadStoragePerLayer.computeIfAbsent(layer, (l) -> new QuadStorage()).finishQuad();
    }

    public void addVertex(SingleQuadParticle.Layer layer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float scale, float u, float v, int packedLight, float rCol, float gCol, float bCol, float alpha) {
        this.quadStoragePerLayer.computeIfAbsent(layer, (l) -> new QuadStorage()).addVertex(
            quaternion,
            x,
            y,
            z,
            xOffset,
            yOffset,
            scale,
            u,
            v,
            packedLight,
            rCol,
            gCol,
            bCol,
            alpha
        );
        this.vertexAmount++;
    }

    @Override
    public QuadConsumer getConsumer(SingleQuadParticle.Layer layer) {
        return new CustomParticleGeometryQuadConsumer(this, layer);
    }
}
*///? }