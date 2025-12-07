package games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderType.CompositeState.CompositeStateBuilder.class)
public interface CompositeStateBuilderInvoker {
    @Invoker("setTextureState")
    RenderType.CompositeState.CompositeStateBuilder block_place_particle$invokeSetTextureState(RenderStateShard.EmptyTextureStateShard textureState);

    @Invoker("setLightmapState")
    RenderType.CompositeState.CompositeStateBuilder block_place_particle$invokeSetLightmapState(RenderStateShard.LightmapStateShard lightmapState);

    @Invoker("createCompositeState")
    RenderType.CompositeState block_place_particle$invokeCreateCompositeState(boolean outline);
}