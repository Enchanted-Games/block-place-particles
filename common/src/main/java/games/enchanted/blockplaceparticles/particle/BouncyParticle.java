package games.enchanted.blockplaceparticles.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public abstract class BouncyParticle extends TextureSheetParticle {
    protected boolean physics_canBounce = true;
    protected float physics_bounceDecay = 0.85f;
    protected int ticksAlive = 0;

    /**
     * A single quad particle has simple bouncing physics.
     * Set {@link #physics_bounceDecay} in your particle constructor to adjust how much the particle should bounce off of things
     *
     * @param level  level
     * @param x      x pos
     * @param y      y pos
     * @param z      z pos
     * @param xSpeed x velocity
     * @param ySpeed y velocity
     * @param zSpeed z velocity
     */
    protected BouncyParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    protected float getTotalVelocity() {
        return (float) Math.abs(this.xd + this.yd + this.zd);
    }

    @Override
    public void tick() {
        ticksAlive++;

        if(age > 0 && physics_canBounce && this.hasPhysics) {
            double xVel = this.xd;
            double yVel = this.yd;
            double zVel = this.zd;
            if (xVel * xVel + yVel * yVel + zVel * zVel < Mth.square(100)) {
                Vec3 vec3 = Entity.collideBoundingBox(null, new Vec3(xVel, yVel, zVel), this.getBoundingBox(), this.level, List.of());
                this.xd = vec3.x == 0.0 ? -this.xd * physics_bounceDecay : vec3.x;
                this.yd = vec3.y == 0.0 ? -this.yd * physics_bounceDecay : vec3.y;
                this.zd = vec3.z == 0.0 ? -this.zd * physics_bounceDecay : vec3.z;
            }
        }

        super.tick();
    }
}
