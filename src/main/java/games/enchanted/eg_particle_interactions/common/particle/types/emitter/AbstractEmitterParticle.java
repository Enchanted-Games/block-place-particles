package games.enchanted.eg_particle_interactions.common.particle.types.emitter;

import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.EmitterRuleSet;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.DustParticleOptions;
import org.jetbrains.annotations.NotNull;


public abstract class AbstractEmitterParticle extends ParticleInteractionsParticle {
    protected float emitterWidth;
    protected float emitterHeight;
    protected float emitterDepth;
    protected final EmitterRuleSet emitterRuleSet;

    public AbstractEmitterParticle(ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, ParticleConfig config, double x, double y, double z, float width, float height, float depth, EmitterRuleSet emitterRuleSet) {
        super(components, appearance, context, config, x, y, z);
        this.emitterWidth = width;
        this.emitterHeight = height;
        this.emitterDepth = depth;
        this.emitterRuleSet = emitterRuleSet;
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

    protected Emitter getEmitter(ParticleContext context) {
        return this.emitterRuleSet.getEmitter(context);
    }

    @Override
    public @NotNull ParticleRenderType getGroup() {
        return ParticleRenderType.NO_RENDER;
    }
}
