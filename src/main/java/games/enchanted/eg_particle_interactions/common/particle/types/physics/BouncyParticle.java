package games.enchanted.eg_particle_interactions.common.particle.types.physics;

import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public abstract class BouncyParticle extends ParticleInteractionsParticle {
    protected boolean physics_canBounce = true;
    /**
     * How bouncy this particle is. 0 is no bouncing at all, 1 is full bouncing
     */
    protected float physics_bounciness = 0.85f;
    /**
     * How much the particle speed should be affected when travelling through a fluid
     */
    protected float physics_passThroughFluidSpeed = 1f;
    protected boolean isInWater = false;
    protected boolean hasEnteredWater = false;

    /**
     * A single quad particle has simple bouncing physics.
     * Set {@link #physics_bounciness} or {@link #physics_passThroughFluidSpeed} in your particle constructor to adjust the particle physics
     */
    protected BouncyParticle(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    protected float getTotalVelocity() {
        return (float) Math.abs(this.xd + this.yd + this.zd);
    }

    @Override
    public void tick() {
        this.isInWater = this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).is(FluidTags.WATER);
        if (this.isInWater) {
            this.hasEnteredWater = true;
        }

        if (age > 0 && physics_canBounce && this.physics_bounciness > 0 && this.hasPhysics) {
            if (this.isInWater) {
                this.xd *= this.physics_passThroughFluidSpeed;
                this.yd *= this.physics_passThroughFluidSpeed;
                this.zd *= this.physics_passThroughFluidSpeed;
            }
            if (GeneralOptions.ADVANCED_PARTICLE_PHYSICS.getValue()) {
                double xVel = this.xd;
                double yVel = this.yd;
                double zVel = this.zd;
                if (xVel * xVel + yVel * yVel + zVel * zVel < Mth.square(100)) {
                    Vec3 collisionVector = Entity.collideBoundingBox(null, new Vec3(xVel, yVel, zVel), this.getBoundingBox(), this.level, List.of());
                    this.xd = collisionVector.x == 0.0 ? -this.xd * physics_bounciness : collisionVector.x;
                    this.yd = collisionVector.y == 0.0 ? -this.yd * physics_bounciness : collisionVector.y;
                    this.zd = collisionVector.z == 0.0 ? -this.zd * physics_bounciness : collisionVector.z;
                }
            }
        }

        super.tick();
    }
}
