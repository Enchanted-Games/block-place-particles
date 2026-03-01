package games.enchanted.eg_particle_interactions.common.particle.types.emitter.arc;

import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.options.ArcEmitterOptions;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.types.emitter.AbstractEmitterParticle;
import games.enchanted.eg_particle_interactions.common.particle.util.ParticleSpawner;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractArcEmitter extends AbstractEmitterParticle {
    protected static final float DIRECTION_VECTOR_SCALE = 0.0625f;

    List<Split> splits = new ArrayList<>();

    protected final int length;
    protected final int splitAmount;
    protected final int angleVariance;
    protected final int tickInterval;
    protected final int repeat;

    protected final float initialAngleXRad;
    protected final float initialAngleYRad;

    public AbstractArcEmitter(ParticleContext context, ParticleAppearance appearance, ParticleConfig config, double x, double y, double z, float width, float height, float depth, ArcEmitterOptions options) {
        super(context, appearance, config, x, y, z, width, height, depth);

        this.length = options.getLength();
        this.splitAmount = options.getSplits();
        this.angleVariance = options.getAngleVariance();
        this.tickInterval = options.getTickInterval();
        this.repeat = options.getRepeat();
        this.setLifetime(repeat * tickInterval);

        if (options.getInitialAngleXDeg().isPresent()) {
            this.initialAngleXRad = options.getInitialAngleXDeg().get() * (float) (Math.PI / 180);
        } else {
            this.initialAngleXRad = this.level.getRandom().nextFloat() * 360f * (float) (Math.PI / 180);
        }
        if (options.getInitialAngleYDeg().isPresent()) {
            this.initialAngleYRad = options.getInitialAngleYDeg().get() * (float) (Math.PI / 180);
        } else {
            this.initialAngleYRad = this.level.getRandom().nextFloat() * 360f * (float) (Math.PI / 180);
        }

        calculateSplits();
    }

    private float applyRandomDirectionRotation(float startingAngleRad) {
        float randomOffset = (this.level.getRandom().nextFloat() - 0.5f) * angleVariance * 2;
        randomOffset = randomOffset * (float) (Math.PI / 180);
        return startingAngleRad + randomOffset;
    }

    private void calculateSplits() {
        this.splits.clear();
        for (int i = 0; i < splitAmount; i++) {
            Vector3f directionVector = MathHelpers.directionVectorFromPitchYaw(applyRandomDirectionRotation(initialAngleXRad), applyRandomDirectionRotation(initialAngleYRad)).mul(DIRECTION_VECTOR_SCALE);
            @Nullable Split prevSplit = this.splits.isEmpty() ? null : this.splits.getLast();
            this.splits.add(new Split(
                prevSplit == null ? this.x : prevSplit.getEndPos().x,
                prevSplit == null ? this.y : prevSplit.getEndPos().y,
                prevSplit == null ? this.z : prevSplit.getEndPos().z,
                directionVector,
                this.length
            ));
        }
    }

    @Override
    protected void emitterTick() {
        for (Split split : splits) {
            split.tick(this.context, this::getParticleToEmit);
        }
        if (this.age % tickInterval == 0) {
            calculateSplits();
        }
    }

    @FunctionalInterface
    private interface ParticleSupplier {
        PIParticleOptions getParticle(ParticleContext context, double x, double y, double z);
    }

    private record Split(double x, double y, double z, Vector3f directionVector, int length) {
        void tick(ParticleContext context, ParticleSupplier particleSupplier) {
            for (int i = 0; i < length; i++) {
                PIParticleOptions particle = particleSupplier.getParticle(context, x, y, z);
                if (particle == null) continue;
                ParticleSpawner.spawn(
                    particle,
                    context,
                    x + (directionVector.x * i),
                    y + (directionVector.y * i),
                    z + (directionVector.z * i),
                    0,
                    0,
                    0
                );
            }
        }

        Vector3d getEndPos() {
            return new Vector3d(x + (directionVector.x * length), y + (directionVector.y * length), z + (directionVector.z * length));
        }
    }
}
