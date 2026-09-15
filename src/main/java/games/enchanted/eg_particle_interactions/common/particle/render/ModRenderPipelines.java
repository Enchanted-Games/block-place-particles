package games.enchanted.eg_particle_interactions.common.particle.render;

import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.RenderPipelinesAccessor;

//? if minecraft: <= 26.2 {
/*
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
 *///? } else {
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import net.minecraft.client.renderer.oit.OitPipelineSet;
//? }

public class ModRenderPipelines {
    public static final RenderPipeline BACKFACE_TRANSLUCENT_PARTICLE = RenderPipeline.builder(RenderPipelinesAccessor.eg_particle_interactions$getParticleSnippet())
        .withLocation(ParticleInteractionsMod.id("pipeline/translucent_backface_particle"))
        .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
        .withCull(false)
    .build();

    public static final RenderPipeline BACKFACE_CUTOUT_PARTICLE = RenderPipeline.builder(RenderPipelinesAccessor.eg_particle_interactions$getParticleSnippet())
        .withLocation(ParticleInteractionsMod.id("pipeline/cutout_backface_particle"))
        .withCull(false)
    .build();

    //? if minecraft: >= 26.3 {
    public static final OitPipelineSet BACKFACE_OIT_PARTICLE = OitPipelineSet.builder(
        "eg_particle_interactions_backface_particle",
        RenderPipeline.builder(RenderPipelinesAccessor.eg_particle_interactions$getOitParticleSnippet())
            .withLocation(ParticleInteractionsMod.id("pipeline/oit_backface_particle"))
            .withCull(false)
        ).build();
    //? }
}
