package games.enchanted.eg_particle_interactions.common.particle.types.emitter.random_distribution;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.options.RandomDistributionEmitterOptions;
import games.enchanted.eg_particle_interactions.common.particle.types.emitter.AbstractEmitterParticle;
import games.enchanted.eg_particle_interactions.common.particle.util.ParticleSpawner;
import org.joml.Vector3f;

public abstract class AbstractRandomDistributionEmitter extends AbstractEmitterParticle {
    protected double emittedXSpeed;
    protected double emittedYSpeed;
    protected double emittedZSpeed;
    protected int emitterInterval;
    protected int emitterIterations;
    protected int particlesPerEmission;
    protected Vector3f emitterVariance;
    protected boolean emitOnFirstTick;

    protected AbstractRandomDistributionEmitter(ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomDistributionEmitterOptions emitterOptions) {
        super(components, appearance, context, emitterOptions.config(), x, y, z, emitterOptions.getDimensions().x, emitterOptions.getDimensions().y, emitterOptions.getDimensions().z);
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
                PIParticleOptions particle = this.getParticleToEmit(context, emitPos[0], emitPos[1], emitPos[2]);
                if (particle == null) continue;
                ParticleSpawner.spawnWithDefaultAppearance(
                    particle,
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

    protected double[] getRandomPositionInsideBounds() {
        double newX = x + (this.emitterWidth * this.level.getRandom().nextFloat());
        double newY = y + (this.emitterHeight * this.level.getRandom().nextFloat());
        double newZ = z + (this.emitterDepth * this.level.getRandom().nextFloat());
        return new double[]{newX, newY, newZ};
    }
}
