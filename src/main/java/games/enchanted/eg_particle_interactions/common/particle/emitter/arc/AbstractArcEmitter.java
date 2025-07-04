package games.enchanted.eg_particle_interactions.common.particle.emitter.arc;

import games.enchanted.eg_particle_interactions.common.particle.emitter.AbstractEmitterParticle;
import games.enchanted.eg_particle_interactions.common.particle.option.ArcEmitterOptions;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import org.joml.Vector3f;

public abstract class AbstractArcEmitter extends AbstractEmitterParticle {
    Vector3f directionVector;
    protected final int length;
    protected final boolean canSpawnSplits;
    protected final ArcEmitterOptions arcEmitterOptions;

    public AbstractArcEmitter(ClientLevel level, double x, double y, double z, float width, float height, float depth, ArcEmitterOptions options) {
        super(level, x, y, z, width, height, depth);
        calculateRandomDirection();
        this.arcEmitterOptions = options;
        this.length = this.arcEmitterOptions.getLength();
        this.canSpawnSplits = this.arcEmitterOptions.canSpawnSplits();
    }

    private void calculateRandomDirection() {
        float angleXRad = this.level.random.nextFloat() * 360f * (float) (Math.PI / 180);
        float angleYRad = this.level.random.nextFloat() * 360f * (float) (Math.PI / 180);
        this.directionVector = MathHelpers.directionVectorFromPitchYaw(angleXRad, angleYRad).mul(0.1f);
    }

    @Override
    protected void emitterTick() {
        for (int i = 0; i < length; i++) {
            level.addParticle(getParticleToEmit(level, x, y, z), x + (directionVector.x * i), y + (directionVector.y * i), z + (directionVector.z * i), 0, 0, 0);
            if(i == length - 1 && canSpawnSplits && this.age % 50 == 0) {
                calculateRandomDirection();
                level.addParticle(this.arcEmitterOptions.withSplitsDisabled(), x + (directionVector.x * i), y + (directionVector.y * i), z + (directionVector.z * i), 0, 0, 0);
//                level.addParticle(this.arcEmitterOptions.withSplitsDisabled(), x + (directionVector.x * i), y + (directionVector.y * i), z + (directionVector.z * i), 0, 0, 0);
            }
        }
    }

    private record Split(double x, double y, double z, Vector3f directionVector) {}
}
