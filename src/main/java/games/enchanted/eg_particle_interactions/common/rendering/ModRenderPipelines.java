package games.enchanted.eg_particle_interactions.common.rendering;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.RenderPipelinesAccessor;

public class ModRenderPipelines {
    public static final RenderPipeline BACKFACE_TRANSLUCENT_PARTICLE;

    static {
        BACKFACE_TRANSLUCENT_PARTICLE = RenderPipelinesAccessor.block_place_particle$invokeRegister(
            RenderPipeline.builder(RenderPipelinesAccessor.block_place_particle$getParticleSnippet())
                .withLocation("pipeline/translucent_particle")
                .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
                .withCull(false)
            .build());
    }
}
