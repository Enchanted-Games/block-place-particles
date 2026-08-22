package games.enchanted.eg_particle_interactions.common.particle.render.vertex;

import com.mojang.blaze3d.vertex.*;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.BufferBuilderAccess;
import org.jspecify.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;

//? if minecraft: >= 26.2 {
import com.mojang.blaze3d.PrimitiveTopology;
//? }

public class PIBufferBuilder extends BufferBuilder {
    private static final int POSITION_SEMANTIC_ID = 0;
    private static final int COLOR_SEMANTIC_ID = 1;
    private static final int UV0_SEMANTIC_ID = 2;
    private static final int MASK_UV_ID = 3;
    private static final int UV2_SEMANTIC_ID = 4;

    //? if minecraft: >= 26.2 {
    private static final String[] elementNames = new String[]{
        DefaultVertexFormat.POSITION_SEMANTIC_NAME,
        DefaultVertexFormat.COLOR_SEMANTIC_NAME,
        DefaultVertexFormat.UV0_SEMANTIC_NAME,
        PIVertexFormats.MASK_UV_SEMANTIC_NAME,
        DefaultVertexFormat.UV2_SEMANTIC_NAME
    };

    public PIBufferBuilder(
        final ByteBufferBuilder buffer,
        final PrimitiveTopology primitiveTopology,
        final VertexFormat format
    ) {
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

    @Override
    public VertexConsumer setLight(final int packedLightCoords) {
        long pointer = ((BufferBuilderAccess) this).eg_particle_interactions$beginElement(UV2_SEMANTIC_ID);
        if (pointer != -1L) {
            BufferBuilderAccess.eg_particle_interactions$putPackedUv(pointer, packedLightCoords);
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
    //? } else {
    /*final VertexFormatElement[] elements = new VertexFormatElement[]{
        VertexFormatElement.POSITION,
        VertexFormatElement.COLOR,
        VertexFormatElement.UV0,
        PIVertexFormats.MASK_UV_ELEMENT,
        VertexFormatElement.UV2
    };
    final int[] offsetById = new int[5];

    private final int initialElementsToFill;
    private int elementsToFill;

    public PIBufferBuilder(
        final ByteBufferBuilder buffer,
        final VertexFormat.Mode mode
    ) {
        super(buffer, mode, PIVertexFormats.MASK_PARTICLE_VERTEX_FORMAT);

        int elementsMask = 0;
        for(int i = 0; i < elements.length; ++i) {
            elementsMask |= 1 << i;
            if(i == 0) {
                offsetById[0] = 0;
            } else {
                offsetById[i] = offsetById[i - 1] + elements[i - 1].byteSize();
            }
        }
        this.initialElementsToFill = elementsMask & -2;
        this.vertexSize = PIVertexFormats.MASK_PARTICLE_VERTEX_FORMAT.getVertexSize();
    }

    @Override
    public VertexConsumer addVertex(final float x, final float y, final float z) {
        long pointer = ((BufferBuilderAccess) this).eg_particle_interactions$beginVertex() + this.offsetById[POSITION_SEMANTIC_ID];
        this.elementsToFill = this.initialElementsToFill;
        putVec3f(pointer, x, y, z);
        return this;
    }

    private static void putVec3f(final long pointer, final float x, final float y, final float z) {
        MemoryUtil.memPutFloat(pointer, x);
        MemoryUtil.memPutFloat(pointer + 4L, y);
        MemoryUtil.memPutFloat(pointer + 8L, z);
    }

    @Override
    public VertexConsumer setColor(final int color) {
        long pointer = beginElement(COLOR_SEMANTIC_ID);
        if (pointer != -1L) {
            BufferBuilderAccess.eg_particle_interactions$putRgba(pointer, color);
        }

        return this;
    }

    @Override
    public VertexConsumer setUv(final float u, final float v) {
        long pointer = beginElement(UV0_SEMANTIC_ID);
        if (pointer != -1L) {
            MemoryUtil.memPutFloat(pointer, u);
            MemoryUtil.memPutFloat(pointer + 4L, v);
        }

        return this;
    }

    @Override
    public VertexConsumer setLight(final int packedLightCoords) {
        long pointer = beginElement(UV2_SEMANTIC_ID);
        if (pointer != -1L) {
            BufferBuilderAccess.eg_particle_interactions$putPackedUv(pointer, packedLightCoords);
        }

        return this;
    }

    public VertexConsumer setMaskUv(final float u, final float v) {
        long pointer = beginElement(MASK_UV_ID);
        if (pointer != -1L) {
            MemoryUtil.memPutFloat(pointer, u);
            MemoryUtil.memPutFloat(pointer + 4L, v);
        }

        return this;
    }

    private long beginElement(int semanticID) {
        int oldElements = this.elementsToFill;
        int newElements = oldElements & ~(1 << semanticID);
        if (newElements != oldElements) {
            this.elementsToFill = newElements;
            long vertexPointer = ((BufferBuilderAccess) this).eg_particle_interactions$vertexPointer();
            if (vertexPointer == -1L) {
                throw new IllegalArgumentException("Not currently building vertex");
            } else {
                return vertexPointer + this.offsetById[semanticID];
            }
        } else {
            return -1L;
        }
    }
    *///? }
}
