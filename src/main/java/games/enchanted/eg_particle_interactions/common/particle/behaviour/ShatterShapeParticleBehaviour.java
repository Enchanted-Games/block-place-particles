package games.enchanted.eg_particle_interactions.common.particle.behaviour;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.appearance.billboard.FacingCameraMode;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.QuadConsumer;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.util.math.MathHelper;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import org.jspecify.annotations.Nullable;

public class ShatterShapeParticleBehaviour extends ParticleInteractionsParticle {
    protected final float slice0X;
    protected final float slice0Y;
    protected final float slice1X;
    protected final float slice1Y;
    protected final float uvScale;
    protected final float uvOffset;
    protected final boolean inverseSlicePositions;
    protected final @Nullable Direction facingDirection;

    protected ShatterShapeParticleBehaviour(ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(components, appearance, context, x, y, z, ySpeed, zSpeed, xSpeed);

        int spriteWidth = this.currentSprite.contents().width();
        int randomSize = MathHelper.randomBetween(3, 5);
        int randomSizeThird = randomSize / 3;
        this.uvOffset = (float) MathHelper.randomBetween(0, spriteWidth - randomSize) / spriteWidth;
        this.uvScale = (float) randomSize / spriteWidth;
        this.slice0X = (float) MathHelper.randomBetween(1, randomSizeThird) / randomSize;
        this.slice0Y = (float) MathHelper.randomBetween(2, randomSizeThird) / randomSize;
        this.slice1X = (float) MathHelper.randomBetween((randomSizeThird * 2) + 1, randomSize - 1) / randomSize;
        this.slice1Y = (float) MathHelper.randomBetween((randomSizeThird * 2) - 1, randomSize - 1) / randomSize;

        this.inverseSlicePositions = level.getRandom().nextBoolean();

        if (context.blockContext() != null) {
            BlockState state = context.blockContext().state();
            this.facingDirection = state.hasProperty(NetherPortalBlock.AXIS) ? state.getValue(NetherPortalBlock.AXIS).getPositive() : null;
        } else {
            this.facingDirection = null;
        }
    }

    @Override
    public void tick() {
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
        return this.facingDirection;
    }

    @Override
    public FacingCameraMode getBillboardMode() {
        Direction facingDirection = this.getParticleFacingDirection();
        switch (facingDirection) {
            case NORTH -> {
                return (quaternion, camera, pos) -> quaternion.set(MathHelper.eulerAnglesToQuaternion(0f, (float) Math.toRadians(270), 0f));
            }
            case EAST -> {
                return (quaternion, camera, pos) -> quaternion.set(MathHelper.eulerAnglesToQuaternion(0f, (float) Math.toRadians(180), 0f));
            }
            case SOUTH -> {
                return (quaternion, camera, pos) -> quaternion.set(MathHelper.eulerAnglesToQuaternion(0f, (float) Math.toRadians(90), 0f));
            }
            case WEST -> {
                return (quaternion, camera, pos) -> quaternion.set(MathHelper.eulerAnglesToQuaternion(0f, 0f, 0f));
            }
            case UP -> {
                return (quaternion, camera, pos) -> quaternion.set(MathHelper.eulerAnglesToQuaternion(0f, (float) Math.toRadians(90), (float) Math.toRadians(90)));
            }
            case DOWN -> {
                return (quaternion, camera, pos) -> quaternion.set(MathHelper.eulerAnglesToQuaternion(0f, (float) Math.toRadians(90), (float) Math.toRadians(-90)));
            }
            case null, default -> {
                return FacingCameraMode.XYZ;
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

    public static class Provider implements ParticleBehaviourProvider {
        @Override
        public @Nullable Particle createParticle(ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, double x, double z, double y, double xSpeed, double ySpeed, double zSpeed) {
            return new ShatterShapeParticleBehaviour(components, appearance, context, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
