package games.enchanted.eg_particle_interactions.common.particle.emitter;

import com.mojang.blaze3d.vertex.VertexConsumer;
import games.enchanted.eg_particle_interactions.common.config.ConfigHandler;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractEmitterParticle extends Particle {
    protected float emitterWidth;
    protected float emitterHeight;
    protected float emitterDepth;

    public AbstractEmitterParticle(ClientLevel level, double x, double y, double z, float width, float height, float depth) {
        super(level, x, y, z);
        this.emitterWidth = width;
        this.emitterHeight = height;
        this.emitterDepth = depth;
    }

    @Override
    public void tick() {
        if(this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        if(ConfigHandler.debug_showEmitterBounds) {
            level.addParticle(new DustParticleOptions(0xFFFF0000, 0.5f), x, y, z, 0, 0, 0);
            level.addParticle(new DustParticleOptions(0xFF00FF00, 0.5f), x + this.emitterWidth, y + this.emitterHeight, z + this.emitterDepth, 0, 0, 0);
        }
        emitterTick();
    }

    protected abstract void emitterTick();

    /**
     * Called every time before spawning the next particle
     *
     * @param level the level
     * @param x     the x
     * @param y     the y
     * @param z     the z
     * @return the particle to emit
     */
    protected abstract @Nullable ParticleOptions getParticleToEmit(ClientLevel level, double x, double y, double z);

    // TODO: Rendering
//    @Override
//    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float v) {}

    //? if minecraft: <= 1.21.8 {
    /*@Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.NO_RENDER;
    }
    *///?} else {
    @Override
    public @NotNull ParticleRenderType getGroup() {
        return ParticleRenderType.NO_RENDER;
    }
    //?}
}
