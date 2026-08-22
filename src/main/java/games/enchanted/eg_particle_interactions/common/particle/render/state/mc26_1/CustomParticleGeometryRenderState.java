//? if minecraft: >= 26.1 < 26.2 {
/*package games.enchanted.eg_particle_interactions.common.particle.render.state.mc26_1;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import games.enchanted.eg_particle_interactions.common.duck.CustomSubmits;
import games.enchanted.eg_particle_interactions.common.particle.render.PIRenderPipelines;
import games.enchanted.eg_particle_interactions.common.particle.render.feature.mc26_1.CustomParticleGeometryFeatureRenderer;
import games.enchanted.eg_particle_interactions.common.particle.render.feature.mc26_1.CustomParticleGroupRenderer;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.QuadConsumer;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.QuadConsumerProvider;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.mc26_1.CustomParticleGeometryQuadConsumer;
import games.enchanted.eg_particle_interactions.common.particle.render.layer.ParticleLayer;
import games.enchanted.eg_particle_interactions.common.particle.render.state.QuadStorage;
import games.enchanted.eg_particle_interactions.common.particle.render.vertex.PIBufferBuilder;
import games.enchanted.eg_particle_interactions.common.particle.render.vertex.PIVertexFormats;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CustomParticleGeometryRenderState implements ParticleGroupRenderState, CustomParticleGroupRenderer, QuadConsumerProvider {
    private final Map<ParticleLayer, QuadStorage> quadStoragePerLayer = new HashMap<>();
    private int vertexAmount = 0;
    private int maskVertexAmount = 0;

    @Override
    public void clear() {
        this.quadStoragePerLayer.values().forEach(QuadStorage::clear);
        this.vertexAmount = 0;
        this.maskVertexAmount = 0;
    }

    @Override
    public CustomParticleGeometryRenderState.PreparedBufferPair prepare(CustomParticleGeometryFeatureRenderer.BufferCache particleBufferCache, boolean translucentOnly) {
        PreparedBuffer normalBuffer = null;
        PreparedBuffer maskBuffer = null;

        if(this.vertexAmount > 0) {
            try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(this.vertexAmount * DefaultVertexFormat.PARTICLE.getVertexSize())) {
                BufferBuilder vertexBuffer = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);

                HashMap<ParticleLayer, PreparedLayer> layerToPreparedMap = prepareLayers(vertexBuffer, false, translucentOnly);
                MeshData meshData = vertexBuffer.build();

                if (meshData != null) normalBuffer = prepareBuffer(particleBufferCache, meshData, layerToPreparedMap);
            }
        }

        if(this.maskVertexAmount > 0) {
            try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(this.maskVertexAmount * PIVertexFormats.MASK_PARTICLE_VERTEX_FORMAT.getVertexSize())) {
                BufferBuilder vertexBuffer = new PIBufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS);

                HashMap<ParticleLayer, PreparedLayer> layerToPreparedMap = prepareLayers(vertexBuffer, true, translucentOnly);
                MeshData meshData = vertexBuffer.build();

                if (meshData != null) maskBuffer = prepareBuffer(particleBufferCache, meshData, layerToPreparedMap);
            }
        }

        return new PreparedBufferPair(normalBuffer, maskBuffer);
    }

    private PreparedBuffer prepareBuffer(CustomParticleGeometryFeatureRenderer.BufferCache particleBufferCache, MeshData meshData, Map<ParticleLayer, PreparedLayer> layerToPreparedMap) {
        particleBufferCache.write(meshData.vertexBuffer());
        RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS).getBuffer(meshData.drawState().indexCount());
        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(
            RenderSystem.getModelViewMatrix(),
            new Vector4f(1.0f, 1.0f, 1.0f, 1.0f),
            new Vector3f(),
            new Matrix4f()
        );

        return new PreparedBuffer(meshData.drawState().indexCount(), dynamicTransforms, layerToPreparedMap);
    }

    private HashMap<ParticleLayer, PreparedLayer> prepareLayers(BufferBuilder vertexBuffer, boolean maskLayers, boolean translucentOnly) {
        HashMap<ParticleLayer, PreparedLayer> layerToPreparedMap = new HashMap<>();
        int vertexOffset = 0;

        for (Map.Entry<ParticleLayer, QuadStorage> entry : this.quadStoragePerLayer.entrySet()) {
            if(entry.getKey().translucent() != translucentOnly) continue;

            QuadStorage storage = entry.getValue();
            boolean isMaskLayer = entry.getKey().maskAtlasTexture() != null;

            if(!isMaskLayer && !maskLayers) {
                storage.forEachVertex((x, y, z, u, v, maskU, maskV, packedLight, argb) -> {
                    vertexBuffer
                        .addVertex(x, y, z)
                        .setUv(u, v)
                        .setColor(argb)
                        .setLight(packedLight);
                });
            }
            if(isMaskLayer && maskLayers) {
                storage.forEachVertex((x, y, z, u, v, maskU, maskV, packedLight, argb) -> {
                    VertexConsumer b = vertexBuffer
                        .addVertex(x, y, z)
                        .setColor(argb)
                        .setUv(u, v);
                    ((PIBufferBuilder) b).setMaskUv(maskU, maskV)
                        .setLight(packedLight);
                });
            }

            if (storage.vertexAmount() > 0) {
                layerToPreparedMap.put(entry.getKey(), new PreparedLayer(vertexOffset, (int) (storage.vertexAmount() * 1.5)));
            }
            vertexOffset += storage.vertexAmount();
        }
        return layerToPreparedMap;
    }

    @Override
    public void render(PreparedBufferPair preparedBuffers, CustomParticleGeometryFeatureRenderer.BufferCache particleBufferCache, RenderPass renderPass, TextureManager textureManager) {
        if(preparedBuffers.normal() != null) {
            renderBuffer(preparedBuffers.normal(), particleBufferCache, renderPass, textureManager);
        }
        if(preparedBuffers.mask() != null) {
            renderBuffer(preparedBuffers.mask(), particleBufferCache, renderPass, textureManager);
        }
    }

    public void renderBuffer(PreparedBuffer preparedBuffer, CustomParticleGeometryFeatureRenderer.BufferCache particleBufferCache, RenderPass renderPass, TextureManager textureManager) {
        RenderSystem.AutoStorageIndexBuffer quadIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
        renderPass.setVertexBuffer(0, particleBufferCache.get());
        renderPass.setIndexBuffer(quadIndexBuffer.getBuffer(preparedBuffer.indexCount()), quadIndexBuffer.type());
        renderPass.setUniform("DynamicTransforms", preparedBuffer.dynamicTransforms());
        RenderSystem.bindDefaultUniforms(renderPass);

        for (Map.Entry<ParticleLayer, PreparedLayer> entry : preparedBuffer.layers().entrySet()) {
            renderPass.setPipeline(entry.getKey().pipeline());
            AbstractTexture atlas = textureManager.getTexture(entry.getKey().atlasTexture());
            renderPass.bindTexture("Sampler0", atlas.getTextureView(), atlas.getSampler());
            if(entry.getKey().maskAtlasTexture() != null) {
                AbstractTexture maskTexture = textureManager.getTexture(entry.getKey().maskAtlasTexture());
                renderPass.bindTexture(PIRenderPipelines.MASK_SAMPLER_SEMANTIC_NAME, maskTexture.getTextureView(), maskTexture.getSampler());
            }

            renderPass.drawIndexed(entry.getValue().vertexOffset(), 0, entry.getValue().indexCount(), 1);
        }
    }

    @Override
    public void submit(SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (this.vertexAmount > 0 || this.maskVertexAmount > 0) {
            ((CustomSubmits) submitNodeCollector).eg_particle_interactions$submitCustomGeometryParticles(this);
        }
    }

    @Override
    public boolean isEmpty() {
        return this.vertexAmount <= 0 && this.maskVertexAmount <= 0;
    }

    public void startQuad(ParticleLayer layer) {
        this.quadStoragePerLayer.computeIfAbsent(layer, (l) -> new QuadStorage()).startQuad();
    }
    public void finishQuad(ParticleLayer layer) {
        this.quadStoragePerLayer.computeIfAbsent(layer, (l) -> new QuadStorage()).finishQuad();
    }

    public void addVertex(ParticleLayer layer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float scale, float u, float v, float maskU, float maskV, int packedLight, float rCol, float gCol, float bCol, float alpha) {
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
            maskU,
            maskV,
            packedLight,
            rCol,
            gCol,
            bCol,
            alpha
        );
        if(layer.maskAtlasTexture() == null) {
            this.vertexAmount++;
        } else {
            this.maskVertexAmount++;
        }
    }

    @Override
    public QuadConsumer getConsumer(ParticleLayer layer) {
        return new CustomParticleGeometryQuadConsumer(this, layer);
    }

    public record PreparedBufferPair(@Nullable PreparedBuffer normal, @Nullable PreparedBuffer mask) {
    }
    public record PreparedBuffer(int indexCount, GpuBufferSlice dynamicTransforms, Map<ParticleLayer, PreparedLayer> layers) {
    }
    public record PreparedLayer(int vertexOffset, int indexCount) {
    }
}
*///? }