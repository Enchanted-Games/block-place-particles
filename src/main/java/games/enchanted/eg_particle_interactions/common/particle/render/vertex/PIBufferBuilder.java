package games.enchanted.eg_particle_interactions.common.particle.render.vertex;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.vertex.*;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.BufferBuilderAccess;
import org.jspecify.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;

public class PIBufferBuilder extends BufferBuilder {
    private static final int POSITION_SEMANTIC_ID = 0;
    private static final int COLOR_SEMANTIC_ID = 1;
    private static final int UV0_SEMANTIC_ID = 2;
    private static final int MASK_UV_ID = 3;
    private static final int UV2_SEMANTIC_ID = 4;
    private static final String[] elementNames = new String[]{
        DefaultVertexFormat.POSITION_SEMANTIC_NAME,
        DefaultVertexFormat.COLOR_SEMANTIC_NAME,
        DefaultVertexFormat.UV0_SEMANTIC_NAME,
        PIVertexFormats.MASK_UV_SEMANTIC_NAME,
        DefaultVertexFormat.UV2_SEMANTIC_NAME
    };

    public PIBufferBuilder(final ByteBufferBuilder buffer, final PrimitiveTopology primitiveTopology, final VertexFormat format) {
        super(buffer, primitiveTopology, format);

        @Nullable VertexFormatElement[] elms = new VertexFormatElement[elementNames.length];
        int elementsMask = 0;

        for(int i = 0; i < elementNames.length; ++i) {
            String elementName = elementNames[i];
            VertexFormatElement element = format.getElement(elementName);
            if (element != null) {
                elementsMask |= 1 << i;
            }

            elms[i] = element;
        }

        this.elements = elms;
        this.initialElementsToFill = elementsMask & -2;
    }

    @Override
    public VertexConsumer addVertex(final float x, final float y, final float z) {
        BufferBuilderAccess access = (BufferBuilderAccess) this;
        VertexFormatElement positionElement = access.eg_particle_interactions$getElements()[POSITION_SEMANTIC_ID];
        long pointer = access.eg_particle_interactions$beginVertex() + (long)positionElement.offset();
        access.eg_particle_interactions$setElementsToFill(access.eg_particle_interactions$getInitialElementsToFill());
        BufferBuilderAccess.eg_particle_interactions$putVec3f(pointer, x, y, z);
        return this;
    }

    @Override
    public VertexConsumer setColor(final int r, final int g, final int b, final int a) {
        long pointer = ((BufferBuilderAccess) this).eg_particle_interactions$beginElement(COLOR_SEMANTIC_ID);
        if (pointer != -1L) {
            MemoryUtil.memPutByte(pointer, (byte)r);
            MemoryUtil.memPutByte(pointer + 1L, (byte)g);
            MemoryUtil.memPutByte(pointer + 2L, (byte)b);
            MemoryUtil.memPutByte(pointer + 3L, (byte)a);
        }

        return this;
    }

    @Override
    public VertexConsumer setColor(final int color) {
        long pointer = ((BufferBuilderAccess) this).eg_particle_interactions$beginElement(COLOR_SEMANTIC_ID);
        if (pointer != -1L) {
            BufferBuilderAccess.eg_particle_interactions$putRgba(pointer, color);
        }

        return this;
    }

    @Override
    public VertexConsumer setUv(final float u, final float v) {
        long pointer = ((BufferBuilderAccess) this).eg_particle_interactions$beginElement(UV0_SEMANTIC_ID);
        if (pointer != -1L) {
            MemoryUtil.memPutFloat(pointer, u);
            MemoryUtil.memPutFloat(pointer + 4L, v);
        }

        return this;
    }

    public VertexConsumer setMaskUv(final float u, final float v) {
        long pointer = ((BufferBuilderAccess) this).eg_particle_interactions$beginElement(MASK_UV_ID);
        if (pointer != -1L) {
            MemoryUtil.memPutFloat(pointer, u);
            MemoryUtil.memPutFloat(pointer + 4L, v);
        }

        return this;
    }

    @Override
    public VertexConsumer setUv2(final int u, final int v) {
        return ((BufferBuilderAccess) this).eg_particle_interactions$uvShort((short)u, (short)v, UV2_SEMANTIC_ID);
    }

    @Override
    public VertexConsumer setLight(final int packedLightCoords) {
        long pointer = ((BufferBuilderAccess) this).eg_particle_interactions$beginElement(UV2_SEMANTIC_ID);
        if (pointer != -1L) {
            BufferBuilderAccess.eg_particle_interactions$putPackedUv(pointer, packedLightCoords);
        }

        return this;
    }
}
