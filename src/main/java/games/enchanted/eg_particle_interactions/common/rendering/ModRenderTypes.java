package games.enchanted.eg_particle_interactions.common.rendering;

import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.CompositeStateBuilderInvoker;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.RenderTypeInvoker;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public abstract class ModRenderTypes extends RenderType {
    public ModRenderTypes(String name, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    private static final Function<ResourceLocation, RenderType> TRANSLUCENT_PARTICLE_BACKFACE = Util.memoize(ModRenderTypes::createTranslucentParticleBackface);

    private static RenderType createTranslucentParticleBackface(ResourceLocation location) {
        RenderType.CompositeState.CompositeStateBuilder stateBuilder = RenderType.CompositeState.builder();
            ((CompositeStateBuilderInvoker) stateBuilder).block_place_particle$invokeSetTextureState(new RenderStateShard.TextureStateShard(location, false));
            ((CompositeStateBuilderInvoker) stateBuilder).block_place_particle$invokeSetLightmapState(LIGHTMAP);
        RenderType.CompositeState state = ((CompositeStateBuilderInvoker) stateBuilder).block_place_particle$invokeCreateCompositeState(false);
        return RenderTypeInvoker.block_place_particle$invokeCreate("translucent_particle_backface", 1536, false, false, ModRenderPipelines.BACKFACE_TRANSLUCENT_PARTICLE, state);
    }

    public static RenderType translucentParticleBackface(ResourceLocation location) {
        return TRANSLUCENT_PARTICLE_BACKFACE.apply(location);
    }
}
