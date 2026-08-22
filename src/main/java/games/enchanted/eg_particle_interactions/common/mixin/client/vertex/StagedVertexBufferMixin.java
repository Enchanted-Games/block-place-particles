package games.enchanted.eg_particle_interactions.common.mixin.client.vertex;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import games.enchanted.eg_particle_interactions.common.duck.StagedVertexBufferAdditions;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.StagedVertexBufferDrawAccessor;
import games.enchanted.eg_particle_interactions.common.particle.render.vertex.PIBufferBuilder;
import net.minecraft.client.renderer.StagedVertexBuffer;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(StagedVertexBuffer.class)
public abstract class StagedVertexBufferMixin implements StagedVertexBufferAdditions {
    @Shadow
    private @Nullable GpuBuffer currentVertexBuffer;

    @Shadow
    private StagedVertexBuffer.@Nullable Draw lastBuildingDraw;

    @Shadow
    private @Nullable BufferBuilder lastVertexBuilder;

    @Shadow
    @Final
    private ByteBufferBuilder stagingBuffer;

    @Shadow
    protected abstract void finishLastVertexBuilder();

    @Override
    public VertexConsumer eg_particle_interactions$getCustomBuffer(StagedVertexBuffer.Draw draw) {
        if (this.currentVertexBuffer != null) {
            throw new IllegalStateException("Cannot append draw after upload");
        } else if (this.lastBuildingDraw == draw) {
            return Objects.requireNonNull(this.lastVertexBuilder);
        } else {
            this.finishLastVertexBuilder();
            this.lastBuildingDraw = draw;
            this.lastVertexBuilder = new PIBufferBuilder(
                this.stagingBuffer,
                ((StagedVertexBufferDrawAccessor) draw).eg_particle_interactions$primitiveTopology(),
                ((StagedVertexBufferDrawAccessor) draw).eg_particle_interactions$format()
            );
            return this.lastVertexBuilder;
        }
    }
}
