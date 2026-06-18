package games.enchanted.eg_particle_interactions.common.particle.types.vanilla;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.ParticleOptions;
import org.jspecify.annotations.Nullable;

public abstract class ParticleOptionWrapper extends Particle {
    private final ParticleContext context;

    protected ParticleOptionWrapper(ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(context.level(), x, y, z);
        this.context = context;
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
    }

    @Override
    public ParticleRenderType getGroup() {
        return ParticleRenderType.NO_RENDER;
    }

    protected abstract @Nullable ParticleOptions makeParticle(ParticleContext context);

    @Override
    public void tick() {
        if(this.removed) return;
        ParticleOptions options = this.makeParticle(this.context);
        if(options != null) {
            this.level.addParticle(options, this.x, this.y, this.z, this.xd, this.yd, this.zd);
        }
        this.remove();
    }
}
