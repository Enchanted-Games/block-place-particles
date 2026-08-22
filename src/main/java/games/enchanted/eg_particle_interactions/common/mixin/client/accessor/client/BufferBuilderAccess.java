package games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BufferBuilder.class)
public interface BufferBuilderAccess {
    //? if minecraft: >= 26.2 {
    @Accessor("elements")
    VertexFormatElement[] eg_particle_interactions$getElements();

    @Accessor("initialElementsToFill")
    int eg_particle_interactions$getInitialElementsToFill();

    @Accessor("elementsToFill")
    void eg_particle_interactions$setElementsToFill(int value);

    @Invoker("beginElement")
    long eg_particle_interactions$beginElement(final int semanticID);
    @Invoker("beginVertex")
    long eg_particle_interactions$beginVertex();

    @Invoker("putVec3f")
    static void eg_particle_interactions$putVec3f(final long pointer, final float x, final float y, final float z) {
        throw new AssertionError("Mixin not applied");
    }
    @Invoker("putRgba")
    static void eg_particle_interactions$putRgba(final long pointer, final int argb) {
        throw new AssertionError("Mixin not applied");
    }
    @Invoker("putPackedUv")
    static void eg_particle_interactions$putPackedUv(final long pointer, final int packedUv) {
        throw new AssertionError("Mixin not applied");
    }
    //? } else {
    /*@Invoker("beginVertex")
    long eg_particle_interactions$beginVertex();

    @Accessor("vertexPointer")
    long eg_particle_interactions$vertexPointer();

    @Invoker("putRgba")
    static void eg_particle_interactions$putRgba(final long pointer, final int argb) {
        throw new AssertionError("Mixin not applied");
    }
    @Invoker("putPackedUv")
    static void eg_particle_interactions$putPackedUv(final long pointer, final int packedUv) {
        throw new AssertionError("Mixin not applied");
    }
    *///? }
}
