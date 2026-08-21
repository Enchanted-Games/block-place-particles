//? if minecraft: < 26.2 {
/*package games.enchanted.eg_particle_interactions.common.particle.render.feature.mc26_1;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import games.enchanted.eg_particle_interactions.common.duck.mc26_1.CustomSubmitsAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureManager;
import org.jspecify.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.*;

public class CustomParticleGeometryFeatureRenderer implements AutoCloseable {
    private final Queue<BufferCache> availableBuffers = new ArrayDeque<>();
    private final List<BufferCache> usedBuffers = new ArrayList<>();

    public void renderSolids(SubmitNodeCollection submitNodeCollection) {
        this.render(submitNodeCollection, false);
    }

    public void renderTranslucents(SubmitNodeCollection submitNodeCollection) {
        this.render(submitNodeCollection, true);
    }

    private void render(SubmitNodeCollection nodeCollection, boolean translucent) {
        List<CustomParticleGroupRenderer> particleRenderers = ((CustomSubmitsAccess) nodeCollection).eg_particle_interactions$getCustomGeometryParticleSubmits();
        if (particleRenderers.isEmpty()) return;

        GpuDevice device = RenderSystem.getDevice();
        Minecraft minecraft = Minecraft.getInstance();
        TextureManager textureManager = minecraft.getTextureManager();
        RenderTarget mainTarget = minecraft.getMainRenderTarget();
        RenderTarget particleTarget = minecraft.levelRenderer.getParticlesTarget();

        for (CustomParticleGroupRenderer particleGroupRenderer : particleRenderers) {
            if (particleGroupRenderer.isEmpty()) return;

            BufferCache buffer = this.availableBuffers.poll();
            if (buffer == null) {
                buffer = new BufferCache();
            }

            this.usedBuffers.add(buffer);
            QuadParticleRenderState.PreparedBuffers prepared = particleGroupRenderer.prepare(buffer, translucent);
            if (prepared != null) {
                boolean useParticleTarget = particleTarget != null && translucent;
                GpuTextureView colorTextureView = useParticleTarget ? particleTarget.getColorTextureView() : mainTarget.getColorTextureView();
                GpuTextureView depthTextureView = useParticleTarget ? particleTarget.getDepthTextureView() : mainTarget.getDepthTextureView();

                try (RenderPass renderPass = device.createCommandEncoder()
                    .createRenderPass(
                        () -> "[Particle Interactions] Particles - " + (translucent ? "Translucent" : "Solid"),
                        colorTextureView,
                        OptionalInt.empty(),
                        depthTextureView,
                        OptionalDouble.empty()
                    )) {
                    this.prepareRenderPass(renderPass);
                    particleGroupRenderer.render(prepared, buffer, renderPass, textureManager);
                }
            }
        }
    }

    public void endFrame() {
        for (BufferCache usedBuffer : this.usedBuffers) {
            usedBuffer.rotate();
        }

        this.availableBuffers.addAll(this.usedBuffers);
        this.usedBuffers.clear();
    }

    private void prepareRenderPass(RenderPass renderPass) {
        renderPass.setUniform("Projection", Objects.requireNonNull(RenderSystem.getProjectionMatrixBuffer()));
        renderPass.setUniform("Fog", Objects.requireNonNull(RenderSystem.getShaderFog()));
        RenderSystem.bindDefaultUniforms(renderPass);
        renderPass.bindTexture("Sampler2", Minecraft.getInstance().gameRenderer.lightmap(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
    }

    @Override
    public void close() {
        this.availableBuffers.forEach(BufferCache::close);
    }

    public static class BufferCache implements AutoCloseable {
        private @Nullable MappableRingBuffer ringBuffer;

        public void write(ByteBuffer byteBuffer) {
            if (this.ringBuffer == null || this.ringBuffer.size() < byteBuffer.remaining()) {
                if (this.ringBuffer != null) {
                    this.ringBuffer.close();
                }

                this.ringBuffer = new MappableRingBuffer(() -> "[Particle Interactions] Particle Vertices", GpuBuffer.USAGE_VERTEX + GpuBuffer.USAGE_MAP_WRITE, byteBuffer.remaining());
            }

            try (GpuBuffer.MappedView view = RenderSystem.getDevice().createCommandEncoder().mapBuffer(this.ringBuffer.currentBuffer().slice(), false, true)) {
                view.data().put(byteBuffer);
            }
        }

        public GpuBuffer get() {
            if (this.ringBuffer != null) return this.ringBuffer.currentBuffer();
            throw new IllegalStateException("Can't get buffer before it's made");
        }

        void rotate() {
            if (this.ringBuffer == null) return;
            this.ringBuffer.rotate();
        }

        @Override
        public void close() {
            if (this.ringBuffer == null) return;
            this.ringBuffer.close();
        }
    }
}
*///? }