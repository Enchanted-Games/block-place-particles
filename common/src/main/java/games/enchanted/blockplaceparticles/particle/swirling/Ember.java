package games.enchanted.blockplaceparticles.particle.swirling;

import games.enchanted.blockplaceparticles.duck.ParticleAccess;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Ember extends SwirlingParticle {
    protected Ember(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, boolean shouldSwirl) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, shouldSwirl);
        this.setInitialVelocity(xSpeed, ySpeed, zSpeed, 0.015f);

        this.gravity = MathHelpers.randomBetween(-0.03f, -0.08f);

        this.lifetime = MathHelpers.randomBetween(20, 100);

        this.rotSpeed = 0f;
        this.spinAcceleration = 0f;
        this.swirlStrength = MathHelpers.randomBetween(0.7f, 0.9f) * (level.random.nextBoolean() ? -1 : 1);
        this.swirlPeriod = MathHelpers.randomBetween(708, 720);

        this.quadSize = 2.5f/32f;
        this.setSize(1/32f, 1/32f);

        ((ParticleAccess) this).setBypassMovementCollisionCheck(true);
    }

    @Override
    public void applyGravity() {
        this.yd = -this.gravity;
    }

    @Override
    protected void renderTick(float partialTicks) {
        float percentageAge = (float) this.age / this.lifetime;
        if(percentageAge > 0.8) {
            float finalA = 1 - (((Mth.lerp(partialTicks, this.age, this.age + 1f) / this.lifetime) - 0.8f) * 5f);
            if(finalA < 0) {
                this.alpha = 0f;
                return;
            }
            this.alpha = finalA;
        }
    }

    @Override
    public int getLightColor(float partialTicks) {
        return LightTexture.FULL_BRIGHT;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class EmberProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public EmberProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new Ember(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, true);
        }
    }
}
