package games.enchanted.eg_particle_interactions.common.particle.render;

import com.mojang.renderpearl.api.pipeline.*;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.RenderPipelinesAccessor;
import games.enchanted.eg_particle_interactions.common.particle.render.vertex.PIVertexFormats;
import net.minecraft.resources.Identifier;

//? if minecraft: < 26.2 {
/*import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.pipeline.RenderPipeline;
*///? } else {
import net.minecraft.client.renderer.oit.OitPipelineSet;
//? }

public class PIRenderPipelines {
    private static final Identifier MASK_PARTICLE_SHADER = ParticleInteractionsMod.id("core/mask_particle");
    public static final String MASK_SAMPLER_SEMANTIC_NAME = "MaskSampler";

    private static final RenderPipeline.Snippet MASK_PARTICLE_SNIPPET = RenderPipeline.builder()
        //? if minecraft: < 26.2 {
        /*.withVertexFormat(PIVertexFormats.MASK_PARTICLE_VERTEX_FORMAT, VertexFormat.Mode.QUADS)
        .withSampler(MASK_SAMPLER_SEMANTIC_NAME)
        *///? } else {
        .withVertexBinding(0, PIVertexFormats.MASK_PARTICLE_VERTEX_FORMAT)
        .withBindGroupLayout(BindGroupLayout.builder().withUniform(MASK_SAMPLER_SEMANTIC_NAME, UniformType.COMBINED_IMAGE_SAMPLER).build())
        //? }
        .withVertexShader(MASK_PARTICLE_SHADER)
        .withFragmentShader(MASK_PARTICLE_SHADER)
        .buildSnippet();


    public static final RenderPipeline BACKFACE_TRANSLUCENT_PARTICLE = RenderPipeline.builder(RenderPipelinesAccessor.eg_particle_interactions$getParticleSnippet())
        .withLocation(ParticleInteractionsMod.id("pipeline/translucent_backface_particle"))
        .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
        .withCull(false)
    .build();

    public static final OitPipelineSet BACKFACE_OIT_PARTICLE = OitPipelineSet.builder("eg_particle_interactions_backface_particle", RenderPipeline.builder(
            RenderPipelinesAccessor.eg_particle_interactions$getOitParticleSnippet()
        )
        .withLocation(ParticleInteractionsMod.id("pipeline/oit_backface_particle"))
        .withCull(false))
    .build();

    public static final RenderPipeline BACKFACE_CUTOUT_PARTICLE = RenderPipeline.builder(RenderPipelinesAccessor.eg_particle_interactions$getParticleSnippet())
        .withLocation(ParticleInteractionsMod.id("pipeline/cutout_backface_particle"))
        .withCull(false)
    .build();


    public static final RenderPipeline MASK_BACKFACE_TRANSLUCENT_PARTICLE = RenderPipeline.builder(
            RenderPipelinesAccessor.eg_particle_interactions$getParticleSnippet(),
            MASK_PARTICLE_SNIPPET
        )
        .withLocation(ParticleInteractionsMod.id("pipeline/translucent_mask_backface_particle"))
        .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
        .withCull(false)
    .build();

    public static final OitPipelineSet MASK_BACKFACE_OIT_PARTICLE = OitPipelineSet.builder("eg_particle_interactions_oit_mask_backface_particle", RenderPipeline.builder(
            RenderPipelinesAccessor.eg_particle_interactions$getOitParticleSnippet(),
            MASK_PARTICLE_SNIPPET
        )
        .withLocation(ParticleInteractionsMod.id("pipeline/oit_mask_backface_particle"))
        .withCull(false))
    .build();

    public static final RenderPipeline MASK_BACKFACE_CUTOUT_PARTICLE = RenderPipeline.builder(
            RenderPipelinesAccessor.eg_particle_interactions$getParticleSnippet(),
            MASK_PARTICLE_SNIPPET
        )
        .withLocation(ParticleInteractionsMod.id("pipeline/cutout_mask_backface_particle"))
        .withCull(false)
    .build();


    public static final RenderPipeline MASK_TRANSLUCENT_PARTICLE = RenderPipeline.builder(
            RenderPipelinesAccessor.eg_particle_interactions$getParticleSnippet(),
            MASK_PARTICLE_SNIPPET
        )
        .withLocation(ParticleInteractionsMod.id("pipeline/translucent_mask_particle"))
        .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
    .build();

    public static final OitPipelineSet MASK_OIT_PARTICLE = OitPipelineSet.builder("eg_particle_interactions_oit_mask_particle", RenderPipeline.builder(
            RenderPipelinesAccessor.eg_particle_interactions$getOitParticleSnippet(),
            MASK_PARTICLE_SNIPPET
        )
        .withLocation(ParticleInteractionsMod.id("pipeline/oit_mask_particle")))
    .build();

    public static final RenderPipeline MASK_CUTOUT_PARTICLE = RenderPipeline.builder(
            RenderPipelinesAccessor.eg_particle_interactions$getParticleSnippet(),
            MASK_PARTICLE_SNIPPET
        )
        .withLocation(ParticleInteractionsMod.id("pipeline/cutout_mask_particle"))
    .build();
}
