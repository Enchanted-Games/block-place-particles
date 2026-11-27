package games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client;

import net.minecraft.client.renderer.RenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;

@Mixin(RenderPipelines.class)
public interface RenderPipelinesAccessor {
    @Invoker("register")
    static RenderPipeline eg_particle_interactions$invokeRegister(RenderPipeline p_404995_) {
        throw new AssertionError("Mixin not applied");
    }

    @Accessor("PARTICLE_SNIPPET")
    static RenderPipeline.Snippet eg_particle_interactions$getParticleSnippet() {
        throw new AssertionError("Mixin not applied");
    }

    @Accessor("OIT_PARTICLE_SNIPPET")
    static RenderPipeline.Snippet eg_particle_interactions$getOitParticleSnippet() {
        throw new AssertionError("Mixin not applied");
    }
}
