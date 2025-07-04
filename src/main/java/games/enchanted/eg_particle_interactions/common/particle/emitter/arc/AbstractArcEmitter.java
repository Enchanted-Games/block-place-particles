package games.enchanted.eg_particle_interactions.common.particle.emitter.arc;

import games.enchanted.eg_particle_interactions.common.particle.emitter.AbstractEmitterParticle;
import games.enchanted.eg_particle_interactions.common.particle.option.ArcEmitterOptions;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractArcEmitter extends AbstractEmitterParticle {
    List<Split> splits = new ArrayList<>();
    protected final int length;
    protected final int splitAmount;
    protected final ArcEmitterOptions arcEmitterOptions;

    public AbstractArcEmitter(ClientLevel level, double x, double y, double z, float width, float height, float depth, ArcEmitterOptions options) {
        super(level, x, y, z, width, height, depth);
        this.arcEmitterOptions = options;
        this.length = this.arcEmitterOptions.getLength();
        this.splitAmount = this.arcEmitterOptions.getSplits();
        calculateSplits();
    }

    private void calculateSplits() {
        this.splits.clear();
        for (int i = 0; i < splitAmount; i++) {
            float angleXRad = this.level.random.nextFloat() * 360f * (float) (Math.PI / 180);
            float angleYRad = this.level.random.nextFloat() * 360f * (float) (Math.PI / 180);
            Vector3f directionVector = MathHelpers.directionVectorFromPitchYaw(angleXRad, angleYRad).mul(0.0625f);
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
            split.tick(this.level, this::getParticleToEmit);
        }
    }

    @FunctionalInterface
    private interface ParticleSupplier{
        ParticleOptions getParticle(ClientLevel level, double x, double y, double z);
    }

    private record Split(double x, double y, double z, Vector3f directionVector, int length) {
        void tick(ClientLevel level, ParticleSupplier particleSupplier) {
            for (int i = 0; i < length; i++) {
                level.addParticle(particleSupplier.getParticle(level, x, y, z), x + (directionVector.x * i), y + (directionVector.y * i), z + (directionVector.z * i), 0, 0, 0);
            }
            level.addParticle(new DustParticleOptions(0xFFFF0000, 0.5f), x, y, z, 0, 0, 0);
        }

        Vector3d getEndPos() {
            return new Vector3d(x + (directionVector.x * length), y + (directionVector.y * length), z + (directionVector.z * length));
        }
    }
}
