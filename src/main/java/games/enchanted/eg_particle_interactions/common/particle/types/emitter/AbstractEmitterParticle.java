package games.enchanted.eg_particle_interactions.common.particle.types.emitter;

import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.DustParticleOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public abstract class AbstractEmitterParticle extends ParticleInteractionsParticle {
    protected float emitterWidth;
    protected float emitterHeight;
    protected float emitterDepth;

    public AbstractEmitterParticle(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, float width, float height, float depth) {
        super(context, appearance, x, y, z);
        this.emitterWidth = width;
        this.emitterHeight = height;
        this.emitterDepth = depth;
    }

    @Override
    public void tick() {
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        if (GeneralOptions.DEBUG_EMITTER_BOUNDS.getValue()) {
            level.addParticle(new DustParticleOptions(0xFFFF0000, 0.5f), x, y, z, 0, 0, 0);
            level.addParticle(new DustParticleOptions(0xFF00FF00, 0.5f), x + this.emitterWidth, y + this.emitterHeight, z + this.emitterDepth, 0, 0, 0);
        }
        emitterTick();
    }

    protected abstract void emitterTick();

    /**
     * Called every time before spawning the next particle
     *
     * @return the particle to emit
     */
    protected abstract @Nullable PIParticleOptions getParticleToEmit(ParticleContext context, double x, double y, double z);

    @Override
    public @NotNull ParticleRenderType getGroup() {
        return ParticleRenderType.NO_RENDER;
    }
}
