package games.enchanted.blockplaceparticles.rendering;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import games.enchanted.blockplaceparticles.mixin.accessor.client.RenderPipelinesAccessor;

public class ModRenderPipelines {
    public static final RenderPipeline BACKFACE_TRANSLUCENT_PARTICLE;

    static {
        BACKFACE_TRANSLUCENT_PARTICLE = RenderPipelinesAccessor.evs$invokeRegister(
            RenderPipeline.builder(RenderPipelinesAccessor.evs$getParticleSnippet())
                .withLocation("pipeline/translucent_particle")
                .withBlend(BlendFunction.TRANSLUCENT)
                .withCull(false)
            .build());
    }
}
