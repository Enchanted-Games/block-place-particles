package games.enchanted.eg_particle_interactions.common.particle.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.RenderPipelinesAccessor;

public class ModRenderPipelines {
    public static final RenderPipeline BACKFACE_TRANSLUCENT_PARTICLE = RenderPipeline.builder(RenderPipelinesAccessor.block_place_particle$getParticleSnippet())
        .withLocation(ParticleInteractionsMod.id("pipeline/translucent_backface_particle"))
        .withBlend(BlendFunction.TRANSLUCENT)
        .withCull(false)
    .build();

    public static final RenderPipeline BACKFACE_CUTOUT_PARTICLE = RenderPipeline.builder(RenderPipelinesAccessor.block_place_particle$getParticleSnippet())
        .withLocation(ParticleInteractionsMod.id("pipeline/cutout_backface_particle"))
        .withCull(false)
    .build();
}
