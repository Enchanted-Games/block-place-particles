package games.enchanted.blockplaceparticles.mixin.accessor.client;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderType.class)
public interface RenderTypeInvoker {
    @Invoker("create")
    static RenderType.CompositeRenderType evs$invokeCreate(String p_173210_, int p_173213_, boolean p_405273_, boolean p_405428_, RenderPipeline p_404631_, RenderType.CompositeState p_173214_) {
        throw new AssertionError("Mixin not applied");
    }
}
