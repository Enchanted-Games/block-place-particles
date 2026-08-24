package games.enchanted.eg_particle_interactions.common.compat.iris;

import games.enchanted.eg_particle_interactions.common.particle.render.PIRenderPipelines;
import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.api.v0.IrisProgram;

public class IrisApiUserImpl implements IrisApiUser {
    @Override
    public void doIrisStuff() {
        var api = IrisApi.getInstance();
        api.assignPipeline(PIRenderPipelines.BACKFACE_TRANSLUCENT_PARTICLE, IrisProgram.PARTICLES);
        api.assignPipeline(PIRenderPipelines.BACKFACE_CUTOUT_PARTICLE, IrisProgram.PARTICLES);
        api.assignPipeline(PIRenderPipelines.MASK_BACKFACE_TRANSLUCENT_PARTICLE, IrisProgram.PARTICLES);
        api.assignPipeline(PIRenderPipelines.MASK_BACKFACE_CUTOUT_PARTICLE, IrisProgram.PARTICLES);
        api.assignPipeline(PIRenderPipelines.MASK_TRANSLUCENT_PARTICLE, IrisProgram.PARTICLES);
        api.assignPipeline(PIRenderPipelines.MASK_CUTOUT_PARTICLE, IrisProgram.PARTICLES);
    }

    @Override
    public boolean shaderpackActive() {
        return IrisApi.getInstance().isShaderPackInUse();
    }
}
