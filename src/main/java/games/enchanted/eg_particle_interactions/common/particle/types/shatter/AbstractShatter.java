package games.enchanted.eg_particle_interactions.common.particle.types.shatter;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.QuadConsumer;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.core.Direction;
import org.joml.Quaternionf;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public abstract class AbstractShatter extends ParticleInteractionsParticle {
    protected final float slice0X;
    protected final float slice0Y;
    protected final float slice1X;
    protected final float slice1Y;
    protected final float uvScale;
    protected final float uvOffset;
    protected final boolean inverseSlicePositions;

    protected AbstractShatter(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed);

        int spriteWidth = this.currentSprite.contents().width();
        int randomSize = MathHelpers.randomBetween(3, 5);
        int randomSizeThird = randomSize / 3;
        this.uvOffset = (float) MathHelpers.randomBetween(0, spriteWidth - randomSize) / spriteWidth;
        this.uvScale = (float) randomSize / spriteWidth;
        this.setScale(this.uvScale);
        this.slice0X = (float) MathHelpers.randomBetween(1, randomSizeThird) / randomSize;
        this.slice0Y = (float) MathHelpers.randomBetween(2, randomSizeThird) / randomSize;
        this.slice1X = (float) MathHelpers.randomBetween((randomSizeThird * 2) + 1, randomSize - 1) / randomSize;
        this.slice1Y = (float) MathHelpers.randomBetween((randomSizeThird * 2) - 1, randomSize - 1) / randomSize;

        this.inverseSlicePositions = level.getRandom().nextBoolean();
        this.roll = (float) Math.toRadians(MathHelpers.randomBetween(0, 3) * 90);
        this.prevRoll = this.roll;

        setInitialVelocity(xSpeed, ySpeed, zSpeed, 0.1f);

        this.gravity = MathHelpers.randomBetween(0.75f, 0.9f);
        this.lifetime = MathHelpers.randomBetween(5, 25);
    }

    protected void setInitialVelocity(double xSpeed, double ySpeed, double zSpeed, float variance) {
        this.xd = xSpeed + ((level.getRandom().nextFloat() * variance) - (variance / 2));
        this.yd = ySpeed + ((level.getRandom().nextFloat() * variance) - (variance / 2));
        this.zd = zSpeed + ((level.getRandom().nextFloat() * variance) - (variance / 2));
    }

    @Override
    public void tick() {
        this.xd *= 0.95f;
        this.yd *= 0.95f;
        this.zd *= 0.95f;
        super.tick();

        float percentageAge = (float) this.age / this.lifetime;
        if (percentageAge > 0.8) {
            float finalA = 1 - ((percentageAge - 0.8f) * 5f);
            if (finalA < 0) {
                this.setAlpha(0, true);
                return;
            }
            this.setAlpha(finalA, true);
        }
    }

    protected @Nullable Direction getParticleFacingDirection() {
        return null;
    }

    @Override
    public @NonNull BillboardMode getBillboardMode() {
        Direction facingDirection = this.getParticleFacingDirection();
        switch (facingDirection) {
            case NORTH -> {
                return (quaternion, camera, partialTicks) -> quaternion.set(MathHelpers.eulerAnglesToQuaternion(0f, (float) Math.toRadians(270), 0f));
            }
            case EAST -> {
                return (quaternion, camera, partialTicks) -> quaternion.set(MathHelpers.eulerAnglesToQuaternion(0f, (float) Math.toRadians(180), 0f));
            }
            case SOUTH -> {
                return (quaternion, camera, partialTicks) -> quaternion.set(MathHelpers.eulerAnglesToQuaternion(0f, (float) Math.toRadians(90), 0f));
            }
            case WEST -> {
                return (quaternion, camera, partialTicks) -> quaternion.set(MathHelpers.eulerAnglesToQuaternion(0f, 0f, 0f));
            }
            case UP -> {
                return (quaternion, camera, partialTicks) -> quaternion.set(MathHelpers.eulerAnglesToQuaternion(0f, (float) Math.toRadians(90), (float) Math.toRadians(90)));
            }
            case DOWN -> {
                return (quaternion, camera, partialTicks) -> quaternion.set(MathHelpers.eulerAnglesToQuaternion(0f, (float) Math.toRadians(90), (float) Math.toRadians(-90)));
            }
            case null, default -> {
                return BillboardMode.XYZ;
            }
        }
    }

    protected float getScaledUVCoord(float uv) {
        return uv * this.uvScale + this.uvOffset;
    }

    @Override
    protected void extractGeometry(QuadConsumer consumer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        float scale = this.getLerpedScale(partialTicks);
        int lightColour = this.getLightmapCoords(partialTicks);

        float u0 = this.currentSprite.getU(this.getScaledUVCoord(0));
        float u1 = this.currentSprite.getU(this.getScaledUVCoord(this.slice0X));
        float v0 = this.currentSprite.getV(this.getScaledUVCoord(this.inverseSlicePositions ? 1 - this.slice0Y : 0));
        float v1 = this.currentSprite.getV(this.getScaledUVCoord(this.inverseSlicePositions ? 1 : this.slice0Y));

        float r = this.getLerpedRed(partialTicks);
        float g = this.getLerpedGreen(partialTicks);
        float b = this.getLerpedBlue(partialTicks);
        float a = this.getLerpedAlpha(partialTicks);

        consumer.startQuad();
        consumer.addVertex(quaternion, x, y, z, this.slice0X, this.inverseSlicePositions ? 0 : 1 - this.slice0Y, scale, u1, v1, lightColour, r, g, b, a);
        consumer.addVertex(quaternion, x, y, z, this.slice0X, this.inverseSlicePositions ? this.slice0Y : 1, scale, u1, v0, lightColour, r, g, b, a);
        consumer.addVertex(quaternion, x, y, z, 0, this.inverseSlicePositions ? this.slice0Y : 1, scale, u0, v0, lightColour, r, g, b, a);
        consumer.addVertex(quaternion, x, y, z, 0, this.inverseSlicePositions ? 0 : 1 - this.slice0Y, scale, u0, v1, lightColour, r, g, b, a);
        consumer.finishQuad();

        scale = this.getLerpedScale(partialTicks);
        u0 = this.currentSprite.getU(this.getScaledUVCoord(this.slice0X));
        u1 = this.currentSprite.getU(this.getScaledUVCoord(this.slice1X));
        v0 = this.currentSprite.getV(this.getScaledUVCoord(0));
        v1 = this.currentSprite.getV(this.getScaledUVCoord(1));

        consumer.startQuad();
        consumer.addVertex(quaternion, x, y, z, this.slice1X, 0, scale, u1, v1, lightColour, r, g, b, a);
        consumer.addVertex(quaternion, x, y, z, this.slice1X, 1, scale, u1, v0, lightColour, r, g, b, a);
        consumer.addVertex(quaternion, x, y, z, this.slice0X, 1, scale, u0, v0, lightColour, r, g, b, a);
        consumer.addVertex(quaternion, x, y, z, this.slice0X, 0, scale, u0, v1, lightColour, r, g, b, a);
        consumer.finishQuad();

        scale = this.getLerpedScale(partialTicks);
        u0 = this.currentSprite.getU(this.getScaledUVCoord(this.slice1X));
        u1 = this.currentSprite.getU(this.getScaledUVCoord(1));
        v0 = this.currentSprite.getV(this.getScaledUVCoord(this.inverseSlicePositions ? 0 : this.slice1Y));
        v1 = this.currentSprite.getV(this.getScaledUVCoord(this.inverseSlicePositions ? 1 - this.slice1Y : 1));

        consumer.startQuad();
        consumer.addVertex(quaternion, x, y, z, 1, this.inverseSlicePositions ? this.slice1Y : 0, scale, u1, v1, lightColour, r, g, b, a);
        consumer.addVertex(quaternion, x, y, z, 1, this.inverseSlicePositions ? 1 : 1 - this.slice1Y, scale, u1, v0, lightColour, r, g, b, a);
        consumer.addVertex(quaternion, x, y, z, this.slice1X, this.inverseSlicePositions ? 1 : 1 - this.slice1Y, scale, u0, v0, lightColour, r, g, b, a);
        consumer.addVertex(quaternion, x, y, z, this.slice1X, this.inverseSlicePositions ? this.slice1Y : 0, scale, u0, v1, lightColour, r, g, b, a);
        consumer.finishQuad();
    }

    @Override
    protected @NonNull ParticleLayer getParticleLayer() {
        return ParticleLayer.BACKFACE_TERRAIN;
    }
}
