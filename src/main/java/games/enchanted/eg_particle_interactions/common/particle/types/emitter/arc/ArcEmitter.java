package games.enchanted.eg_particle_interactions.common.particle.types.emitter.arc;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.EmptyEmitter;
import games.enchanted.eg_particle_interactions.common.particle.types.options.ArcEmitterOptions;
import games.enchanted.eg_particle_interactions.common.particle.types.PIParticleProvider;
import games.enchanted.eg_particle_interactions.common.particle.types.emitter.AbstractEmitterParticle;
import games.enchanted.eg_particle_interactions.common.util.math.MathHelper;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ArcEmitter extends AbstractEmitterParticle {
    protected static final float DIRECTION_VECTOR_SCALE = 0.0625f;

    List<Split> splits = new ArrayList<>();

    protected final int length;
    protected final int splitAmount;
    protected final int angleVariance;
    protected final int tickInterval;
    protected final int repeat;

    protected final float initialAngleXRad;
    protected final float initialAngleYRad;

    public ArcEmitter(ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, double x, double y, double z, ArcEmitterOptions options) {
        super(components, appearance, context, options.config(), x, y, z, 0, 0, 0, options.getEmitterRuleSet().get());

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

    @Override
    protected Emitter getEmitter(ParticleContext context) {
        return this.level.getRandom().nextFloat() > this.getAgePercent() ? super.getEmitter(context) : EmptyEmitter.INSTANCE;
    }

    private float applyRandomDirectionRotation(float startingAngleRad) {
        float randomOffset = (this.level.getRandom().nextFloat() - 0.5f) * angleVariance * 2;
        randomOffset = randomOffset * (float) (Math.PI / 180);
        return startingAngleRad + randomOffset;
    }

    private void calculateSplits() {
        this.splits.clear();
        for (int i = 0; i < splitAmount; i++) {
            Vector3f directionVector = MathHelper.directionVectorFromPitchYaw(applyRandomDirectionRotation(initialAngleXRad), applyRandomDirectionRotation(initialAngleYRad)).mul(DIRECTION_VECTOR_SCALE);
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
            split.tick(this.context, this::getEmitter);
        }
        if (this.age % tickInterval == 0) {
            calculateSplits();
        }
    }

    @FunctionalInterface
    private interface EmitterSupplier {
        Emitter get(ParticleContext context);
    }

    private record Split(double x, double y, double z, Vector3f directionVector, int length) {
        void tick(ParticleContext context, EmitterSupplier emitterSupplier) {
            for (int i = 0; i < this.length; i++) {
                emitterSupplier.get(context).spawnParticle(
                    context,
                    this.x + (this.directionVector.x * i),
                    this.y + (this.directionVector.y * i),
                    this.z + (this.directionVector.z * i),
                    0,
                    0,
                    0
                );
            }
        }

        Vector3d getEndPos() {
            return new Vector3d(
                this.x + (this.directionVector.x * this.length),
                this.y + (this.directionVector.y * this.length),
                this.z + (this.directionVector.z * this.length)
            );
        }
    }

    public static class Provider implements PIParticleProvider<ArcEmitterOptions> {
        public Provider() {
        }

        @Override
        public @Nullable Particle createParticle(
            ArcEmitterOptions options,
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
            return new ArcEmitter(components, appearance, context, x, y, z, options);
        }
    }
}
