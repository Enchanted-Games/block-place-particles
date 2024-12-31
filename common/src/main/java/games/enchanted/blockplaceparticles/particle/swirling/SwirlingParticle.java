package games.enchanted.blockplaceparticles.particle.swirling;

import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SwirlingParticle extends TextureSheetParticle {
    protected float rotSpeed;
    protected float spinAcceleration;
    protected double swirlPeriod;
    protected float swirlStrength;
    protected final boolean shouldSwirl;

    protected SwirlingParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, boolean shouldSwirl) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.gravity = 0.5f;

        this.sprite = spriteSet.get(level.random);

        this.lifetime = 100;
        this.quadSize = 3/16f;

        this.shouldSwirl = shouldSwirl;
        this.rotSpeed = 0f;
        this.spinAcceleration = 0f;
        this.swirlStrength = MathHelpers.randomBetween(5, 5);
        this.swirlPeriod = MathHelpers.randomBetween(100, 300);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void applyGravity() {
        this.yd -= (0.04f * this.gravity);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
        }

        if (this.removed) return;

        double swirlX = 0.0d;
        double swirlZ = 0.0d;
        if(shouldSwirl) {
            float swirlMultiplier = (this.age - this.lifetime) * 0.05f;

            swirlX += (double) swirlMultiplier * Math.cos((double) swirlMultiplier * this.swirlPeriod) * (double) this.swirlStrength;
            swirlZ += (double) swirlMultiplier * Math.sin((double) swirlMultiplier * this.swirlPeriod) * (double) this.swirlStrength;
        }

        this.xd += swirlX * 0.0025f;
        this.zd += swirlZ * 0.0025f;
        this.rotSpeed += this.spinAcceleration / 20.0f;
        this.oRoll = this.roll;
        this.roll += this.rotSpeed / 20.0f;
        this.applyGravity();
        this.move(this.xd, this.yd, this.zd);

        this.xd *= this.friction;
        this.yd *= this.friction;
        this.zd *= this.friction;
    }
}