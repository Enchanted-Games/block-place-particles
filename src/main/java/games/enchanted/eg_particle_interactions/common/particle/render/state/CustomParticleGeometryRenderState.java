package games.enchanted.eg_particle_interactions.common.particle.render.state;

//? if minecraft: > 1.21.8 {
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import games.enchanted.eg_particle_interactions.common.Logging;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ParticleFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CustomParticleGeometryRenderState implements SubmitNodeCollector.ParticleGroupRenderer, ParticleGroupRenderState {
    private static final int INITIAL_CAPACITY = 512;
    private static final int ADDITION_CAPACITY = 128;
    private static final int VERTS_PER_QUAD = 4;
    private static final int INTS_PER_QUAD = 2;
    private static final int FLOATS_PER_QUAD = 5;
    private final Map<SingleQuadParticle.Layer, QuadStorage> quadStoragePerLayer = new HashMap<>();
    private int vertexAmount = 0;

    @Override
    public void clear() {
        this.quadStoragePerLayer.values().forEach(QuadStorage::clear);
        this.vertexAmount = 0;
    }

    @Override
    public @Nullable QuadParticleRenderState.PreparedBuffers prepare(
        ParticleFeatureRenderer.ParticleBufferCache particleBufferCache
        //? if minecraft: >= 26.1 {
         , boolean translucentOnly
        //? }
    ) {
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(this.vertexAmount * DefaultVertexFormat.PARTICLE.getVertexSize())) {
            BufferBuilder vertexBuffer = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);

            HashMap<SingleQuadParticle.Layer, QuadParticleRenderState.PreparedLayer> layerToPreparedMap = prepareLayers(
                vertexBuffer
                //? if minecraft: >= 26.1 {
                , translucentOnly
                //? }
            );

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

    private @NotNull HashMap<SingleQuadParticle.Layer, QuadParticleRenderState.PreparedLayer> prepareLayers(
        BufferBuilder vertexBuffer
        //? if minecraft: >= 26.1 {
        , boolean translucentOnly
        //? }
    ) {
        HashMap<SingleQuadParticle.Layer, QuadParticleRenderState.PreparedLayer> layerToPreparedMap = new HashMap<>();
        int vertexOffset = 0;

        for (Map.Entry<SingleQuadParticle.Layer, QuadStorage> entry : this.quadStoragePerLayer.entrySet()) {
            //? if minecraft: >= 26.1 {
            if(entry.getKey().translucent() != translucentOnly) continue;
            //? }

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
    public void render(
        QuadParticleRenderState.PreparedBuffers preparedBuffers, ParticleFeatureRenderer.ParticleBufferCache particleBufferCache, RenderPass renderPass, TextureManager textureManager
        //? if minecraft: < 26.1 {
        /*, boolean translucentOnly
        *///? }
    ) {
        RenderSystem.AutoStorageIndexBuffer quadIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
        renderPass.setVertexBuffer(0, particleBufferCache.get());
        renderPass.setIndexBuffer(quadIndexBuffer.getBuffer(preparedBuffers.indexCount()), quadIndexBuffer.type());
        renderPass.setUniform("DynamicTransforms", preparedBuffers.dynamicTransforms());

        for (Map.Entry<SingleQuadParticle.Layer, QuadParticleRenderState.PreparedLayer> entry : preparedBuffers.layers().entrySet()) {
            //? if minecraft: < 26.1 {
            /*if (translucentOnly != entry.getKey().translucent()) continue;
            *///? }
            
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

    //? if minecraft: >= 26.1 {
    @Override
    public boolean isEmpty() {
        return this.vertexAmount <= 0;
    }
    //? }

    public void startQuad(SingleQuadParticle.Layer layer) {
        this.quadStoragePerLayer.computeIfAbsent(layer, (l) -> new QuadStorage()).startQuad();
    }
    public void finishQuad(SingleQuadParticle.Layer layer) {
        this.quadStoragePerLayer.computeIfAbsent(layer, (l) -> new QuadStorage()).finishQuad();
    }

    public void addVertex(SingleQuadParticle.Layer layer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float scale, float u, float v, int packedLight) {
        this.addVertex(
            layer,
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
            1.0f,
            1.0f,
            1.0f,
            1.0f
        );
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

    static class QuadStorage {
        private int capacity = INITIAL_CAPACITY * VERTS_PER_QUAD;
        private float[] floats = new float[capacity * FLOATS_PER_QUAD];
        private int[] ints = new int[capacity * INTS_PER_QUAD];

        private int currentVertexIndex = 0;
        private int currentQuadVertCount = -1;

        public void startQuad() {
            if(this.currentQuadVertCount != -1) {
                throw new IllegalStateException("Cannot start new quad before previous quad has 4 vertices");
            }
            this.currentQuadVertCount = 0;
        }

        public void finishQuad() {
            if(this.currentQuadVertCount != 4) {
                throw new IllegalStateException("Cannot finish quad without 4 vertices");
            }
            this.currentQuadVertCount = -1;
        }

        public boolean validStateForAddingVertex() {
            return this.currentQuadVertCount >= 0 && this.currentQuadVertCount < 4;
        }

        public void addVertex(Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float scale, float u, float v, int packedLight, float rCol, float gCol, float bCol, float alpha) {
            if(!validStateForAddingVertex()) {
                throw new IllegalStateException("Cannot add vertex, make sure to finish the previous quad or start a new quad");
            }
            Vector3f vertexPos = (new Vector3f(xOffset, yOffset, 0.0F)).rotate(quaternion).mul(scale).add(x, y, z);
            this.putData(vertexPos.x(), vertexPos.y(), vertexPos.z(), u, v, packedLight, ARGB.colorFromFloat(alpha, rCol, gCol, bCol));
        }

        private void putData(float x, float y, float z, float u, float v, int packedLight, int argb) {
            if(this.currentVertexIndex + 1 > this.capacity) {
                expandCapacity();
            }
            this.currentQuadVertCount++;
            int i = this.currentVertexIndex * FLOATS_PER_QUAD;
            this.floats[i++] = x;
            this.floats[i++] = y;
            this.floats[i++] = z;
            this.floats[i++] = u;
            this.floats[i] = v;
            i = this.currentVertexIndex * INTS_PER_QUAD;
            this.ints[i++] = packedLight;
            this.ints[i] = argb;
            this.currentVertexIndex++;
        }

        public void forEachVertex(QuadConsumer consumer) {
            for (int i = 0; i < this.currentVertexIndex; i++) {
                int floatsIndex = i * FLOATS_PER_QUAD;
                int intsIndex = i * INTS_PER_QUAD;
                consumer.consume(
                    this.floats[floatsIndex++],
                    this.floats[floatsIndex++],
                    this.floats[floatsIndex++],
                    this.floats[floatsIndex++],
                    this.floats[floatsIndex],
                    this.ints[intsIndex++],
                    this.ints[intsIndex]
                );
            }
        }

        public int vertexAmount() {
            return this.currentVertexIndex;
        }

        public void clear() {
            this.currentQuadVertCount = -1;
            this.currentVertexIndex = 0;
        }

        private void expandCapacity() {
            int oldCapacity = this.capacity;
            this.capacity += ADDITION_CAPACITY * VERTS_PER_QUAD;
            this.floats = Arrays.copyOf(this.floats, capacity * FLOATS_PER_QUAD);
            this.ints = Arrays.copyOf(this.ints, capacity * INTS_PER_QUAD);
            Logging.info("Expanding QuadStorage: old capacity [{} verts], new capacity: [{} verts]", oldCapacity, this.capacity);
        }
    }

    interface QuadConsumer {
        void consume(float x, float y, float z, float u, float v, int packedLight, int argb);
    }
}
//?}