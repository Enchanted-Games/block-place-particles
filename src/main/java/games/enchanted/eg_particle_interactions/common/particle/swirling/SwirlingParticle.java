package games.enchanted.eg_particle_interactions.common.particle.swirling;

import com.mojang.blaze3d.vertex.VertexConsumer;
import games.enchanted.eg_particle_interactions.common.particle.compat.CustomGeometryParticle;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import org.jetbrains.annotations.NotNull;

//? if minecraft: > 1.21.8 {
import games.enchanted.eg_particle_interactions.common.rendering.state.CustomParticleGeometryRenderState;
//?}

public class SwirlingParticle extends CustomGeometryParticle {
    protected float rotSpeed;
    protected float spinAcceleration;
    protected double swirlPeriod;
    protected float swirlStrength;
    protected final boolean shouldSwirl;

    protected SwirlingParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, boolean shouldSwirl) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet.get(level.random));
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.gravity = 0.5f;

        this.sprite = spriteSet.get(level.random);

        this.lifetime = 100;
        this.setScale(3/16f);

        this.shouldSwirl = shouldSwirl;
        this.rotSpeed = 0f;
        this.spinAcceleration = 0f;
        this.swirlStrength = MathHelpers.randomBetween(5, 5);
        this.swirlPeriod = MathHelpers.randomBetween(100, 300);
    }

    protected void setInitialVelocity(double xSpeed, double ySpeed, double zSpeed, float variance) {
        this.xd = xSpeed + ((level.random.nextFloat() * variance) - (variance / 2));
        this.yd = ySpeed + ((level.random.nextFloat() * variance) - (variance / 2));
        this.zd = zSpeed + ((level.random.nextFloat() * variance) - (variance / 2));
    }

    public void applyGravity() {
        this.yd -= (0.04f * this.gravity);
    }

    protected void renderTick(float partialTicks) {}

    @Override
    protected ParticleLayer getParticleLayer() {
        return ParticleLayer.OPAQUE;
    }

    //? if minecraft: <= 1.21.8 {
    /*@Override
    public void render(@NotNull VertexConsumer buffer, @NotNull Camera renderInfo, float partialTicks) {
        this.renderTick(partialTicks);
        super.render(buffer, renderInfo, partialTicks);
    }
    *///?} else {
    @Override
    public void extract(@NotNull CustomParticleGeometryRenderState state, @NotNull Camera renderInfo, float partialTicks) {
        this.renderTick(partialTicks);
        super.extract(state, renderInfo, partialTicks);
    }
    //?}

    @Override
    public void tick() {
        if (this.removed) return;

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        double swirlX = 0.0d;
        double swirlZ = 0.0d;
        if(shouldSwirl) {
            float swirlMultiplier = this.age * 0.08f;

            swirlX += (double) swirlMultiplier * Math.cos((double) swirlMultiplier * this.swirlPeriod) * (double) this.swirlStrength;
            swirlZ += (double) swirlMultiplier * Math.sin((double) swirlMultiplier * this.swirlPeriod) * (double) this.swirlStrength;
        }

        this.xd += swirlX * 0.0025f;
        this.zd += swirlZ * 0.0025f;
        this.applyGravity();
        this.move(this.xd, this.yd, this.zd);

        this.rotSpeed += this.spinAcceleration / 20.0f;
        this.oRoll = this.roll;
        this.roll += this.rotSpeed / 20.0f;

        this.xd *= this.friction;
        this.yd *= this.friction;
        this.zd *= this.friction;
    }
}