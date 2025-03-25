package games.enchanted.blockplaceparticles.mixin.accessor.client;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderPipelines.class)
public interface RenderPipelinesAccessor {
    @Invoker("register")
    static RenderPipeline evs$invokeRegister(RenderPipeline p_404995_) {
        throw new AssertionError("Mixin not applied");
    }

    @Accessor("PARTICLE_SNIPPET")
    static RenderPipeline.Snippet evs$getParticleSnippet() {
        throw new AssertionError("Mixin not applied");
    }
}
