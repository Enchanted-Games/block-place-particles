package games.enchanted.blockplaceparticles.mixin.accessor.client;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderType.CompositeState.CompositeStateBuilder.class)
public interface CompositeStateBuilderInvoker {
    @Invoker("setTextureState")
    RenderType.CompositeState.CompositeStateBuilder evs$invokeSetTextureState(RenderStateShard.EmptyTextureStateShard textureState);

    @Invoker("setLightmapState")
    RenderType.CompositeState.CompositeStateBuilder evs$invokeSetLightmapState(RenderStateShard.LightmapStateShard lightmapState);

    @Invoker("createCompositeState")
    RenderType.CompositeState evs$invokeCreateCompositeState(boolean outline);
}