package games.enchanted.eg_particle_interactions.common.particle.types.emitter;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.types.options.RandomDistributionEmitterOptions;
import games.enchanted.eg_particle_interactions.common.particle.types.PIParticleProvider;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class RandomDistributionEmitter extends AbstractEmitterParticle {
    protected double emittedXSpeed;
    protected double emittedYSpeed;
    protected double emittedZSpeed;
    protected int emitterInterval;
    protected int emitterIterations;
    protected int particlesPerEmission;
    protected Vector3f emitterVariance;
    protected boolean emitOnFirstTick;

    protected RandomDistributionEmitter(ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomDistributionEmitterOptions emitterOptions) {
        super(components, appearance, context, x, y, z, emitterOptions.getDimensions().x, emitterOptions.getDimensions().y, emitterOptions.getDimensions().z, emitterOptions.getEmitterRuleSet().get());
        this.emittedXSpeed = xSpeed;
        this.emittedYSpeed = ySpeed;
        this.emittedZSpeed = zSpeed;
        this.emitterInterval = emitterOptions.getTickInterval();
        this.emitterIterations = emitterOptions.getRepeat();
        this.particlesPerEmission = emitterOptions.getParticlesPerEmission();
        this.emitOnFirstTick = emitterOptions.getEmitOnFirstTick();
        this.emitterVariance = emitterOptions.getVelocityVariance();
        this.setLifetime(emitterInterval * emitterIterations);
        this.x -= (this.emitterWidth / 2);
        this.y -= (this.emitterHeight / 2);
        this.z -= (this.emitterDepth / 2);
    }

    @Override
    protected void emitterTick() {
        if ((this.age - (emitOnFirstTick ? 1 : 0)) % emitterInterval == 0) {
            for (int i = 0; i < particlesPerEmission; i++) {
                double[] emitPos = getRandomPositionInsideBounds();
                this.getEmitter(this.context).spawnParticle(
                    this.context,
                    emitPos[0],
                    emitPos[1],
                    emitPos[2],
                    this.emittedXSpeed + ((level.getRandom().nextFloat() * emitterVariance.x) - (emitterVariance.x / 2)),
                    this.emittedYSpeed + ((level.getRandom().nextFloat() * emitterVariance.y) - (emitterVariance.y / 2)),
                    this.emittedZSpeed + ((level.getRandom().nextFloat() * emitterVariance.z) - (emitterVariance.z / 2))
                );
            }
        }
    }

    @Override
    protected Emitter getEmitter(ParticleContext context) {
        return this.emitterRuleSet.getEmitter(this.context);
    }

    protected double[] getRandomPositionInsideBounds() {
        double newX = x + (this.emitterWidth * this.level.getRandom().nextFloat());
        double newY = y + (this.emitterHeight * this.level.getRandom().nextFloat());
        double newZ = z + (this.emitterDepth * this.level.getRandom().nextFloat());
        return new double[]{newX, newY, newZ};
    }

    public static class Provider implements PIParticleProvider<RandomDistributionEmitterOptions> {
        public Provider() {
        }

        @Override
        public @Nullable Particle createParticle(
            RandomDistributionEmitterOptions emitterOptions,
            ParticleComponentMap components,
            ParticleAppearance appearance,
            ParticleContext context,
            double x,
            double z,
            double y,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new RandomDistributionEmitter(components, appearance, context, x, y, z, xSpeed, ySpeed, zSpeed, emitterOptions);
        }
    }
}
