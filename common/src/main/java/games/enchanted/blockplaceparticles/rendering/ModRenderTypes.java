package games.enchanted.blockplaceparticles.rendering;

import games.enchanted.blockplaceparticles.mixin.accessor.client.CompositeStateBuilderInvoker;
import games.enchanted.blockplaceparticles.mixin.accessor.client.RenderTypeInvoker;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;

import java.util.function.Function;

public abstract class ModRenderTypes extends RenderType {
    public ModRenderTypes(String p_173178_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }

    private static final Function<ResourceLocation, RenderType> TRANSLUCENT_PARTICLE_BACKFACE = Util.memoize(ModRenderTypes::createTranslucentParticleBackface);

    private static RenderType createTranslucentParticleBackface(ResourceLocation location) {
        RenderType.CompositeState.CompositeStateBuilder stateBuilder = RenderType.CompositeState.builder();
            ((CompositeStateBuilderInvoker) stateBuilder).evs$invokeSetTextureState(new RenderStateShard.TextureStateShard(location, TriState.FALSE, false));
            ((CompositeStateBuilderInvoker) stateBuilder).evs$invokeSetLightmapState(LIGHTMAP);
        RenderType.CompositeState state = ((CompositeStateBuilderInvoker) stateBuilder).evs$invokeCreateCompositeState(false);
        return RenderTypeInvoker.evs$invokeCreate("translucent_particle_backface", 1536, false, false, ModRenderPipelines.BACKFACE_TRANSLUCENT_PARTICLE, state);
    }

    public static RenderType translucentParticleBackface(ResourceLocation location) {
        return TRANSLUCENT_PARTICLE_BACKFACE.apply(location);
    }
}
