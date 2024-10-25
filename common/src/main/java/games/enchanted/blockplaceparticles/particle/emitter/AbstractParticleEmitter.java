package games.enchanted.blockplaceparticles.particle.emitter;

import com.mojang.blaze3d.vertex.VertexConsumer;
import games.enchanted.blockplaceparticles.particle.option.ParticleEmitterOptions;
import games.enchanted.blockplaceparticles.shapes.ShapeDefinitions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public abstract class AbstractParticleEmitter extends Particle {
    protected double emittedXSpeed;
    protected double emittedYSpeed;
    protected double emittedZSpeed;
    protected int emitterInterval;
    protected int emitterIterations;
    protected int particlesPerEmission;
    protected float emitterWidth;
    protected float emitterHeight;
    protected float emitterDepth;
    protected boolean emitOnFirstTick;

    protected AbstractParticleEmitter(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ParticleEmitterOptions emitterOptions) {
        super(level, x, y, z);
        this.emittedXSpeed = xSpeed;
        this.emittedYSpeed = ySpeed;
        this.emittedZSpeed = zSpeed;
        this.emitterInterval = emitterOptions.getTickInterval();
        this.emitterIterations = emitterOptions.getTickIterations();
        this.particlesPerEmission = emitterOptions.getParticlesPerEmission();
        this.emitterWidth = emitterOptions.getWidth();
        this.emitterHeight = emitterOptions.getHeight();
        this.emitterDepth = emitterOptions.getDepth();
        this.emitOnFirstTick = emitterOptions.getEmitOnFirstTick();
        this.setLifetime(emitterInterval * emitterIterations);
        this.x -= (this.emitterWidth / 2);
        this.y -= (this.emitterHeight / 2);
        this.z -= (this.emitterDepth / 2);
    }

    @Override
    public void tick() {
        if(this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0, 0, 0);
        if((this.age - (emitOnFirstTick ? 1 : 0)) % emitterInterval == 0) {
            for (int i = 0; i < particlesPerEmission; i++) {
                double[] emitPos = getRandomPositionInsideBounds();
                level.addParticle(this.getParticleToEmit(level, emitPos[0], emitPos[1], emitPos[2]), emitPos[0], emitPos[1], emitPos[2], this.emittedXSpeed, this.emittedYSpeed, this.emittedZSpeed);
            }
        }
    }

    protected double[] getRandomPositionInsideBounds() {
        double newX = x + (this.emitterWidth * this.level.random.nextFloat());
        double newY = y + (this.emitterHeight * this.level.random.nextFloat());
        double newZ = z + (this.emitterDepth * this.level.random.nextFloat());
        return new double[]{newX, newY, newZ};
    }

    /**
     * Called every time before spawning the next particle
     *
     * @param level the level
     * @param x     the x
     * @param y     the y
     * @param z     the z
     * @return the particle to emit
     */
    protected abstract ParticleOptions getParticleToEmit(ClientLevel level, double x, double y, double z);

    @Override
    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float v) {}

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.NO_RENDER;
    }
}
