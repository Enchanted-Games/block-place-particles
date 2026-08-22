package games.enchanted.eg_particle_interactions.common.particle.render.vertex;

import com.mojang.blaze3d.vertex.VertexFormat;

//? if minecraft: < 26.2 {
/*import com.mojang.blaze3d.vertex.VertexFormatElement;
*///? } else {
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
//? }

public class PIVertexFormats {
    public static final String MASK_UV_SEMANTIC_NAME = "MaskUV";

    //? if minecraft: < 26.2 {
    /*public static final int MASK_UV_ID = 937;
    public static final VertexFormatElement MASK_UV_ELEMENT = VertexFormatElement.register(MASK_UV_ID, 0, VertexFormatElement.Type.FLOAT, false, 2);
    *///? }

    public static final VertexFormat MASK_PARTICLE_VERTEX_FORMAT =
    //? if minecraft: < 26.2 {
    /*VertexFormat.builder()
        .add("Position", VertexFormatElement.POSITION)
        .add("Color", VertexFormatElement.COLOR)
        .add("UV0", VertexFormatElement.UV0)
        .add(MASK_UV_SEMANTIC_NAME, MASK_UV_ELEMENT)
        .add("UV2", VertexFormatElement.UV2)
    .build();
    *///? } else {
    VertexFormat.builder(0)
        .addAttribute(DefaultVertexFormat.POSITION_SEMANTIC_NAME, GpuFormat.RGB32_FLOAT)
        .addAttribute(DefaultVertexFormat.COLOR_SEMANTIC_NAME, GpuFormat.RGBA8_UNORM)
        .addAttribute(DefaultVertexFormat.UV0_SEMANTIC_NAME, GpuFormat.RG32_FLOAT)
        .addAttribute(MASK_UV_SEMANTIC_NAME, GpuFormat.RG32_FLOAT)
        .addAttribute(DefaultVertexFormat.UV2_SEMANTIC_NAME, GpuFormat.RG16_SINT)
    .build();
    //? }
}
