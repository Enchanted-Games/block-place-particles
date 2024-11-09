package games.enchanted.blockplaceparticles.particle.dust;

import games.enchanted.blockplaceparticles.util.ColourUtil;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FallingDust extends TextureSheetParticle {
    protected FallingDust(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, float gravityMultiplier) {
        super(level, x, y, z);
        this.setSprite(spriteSet.get(this.random.nextInt(12), 12));
        this.gravity = Mth.randomBetween(this.random, 0.25F, 0.38F);;
        this.friction = 1.0F;
        this.xd = xSpeed + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.yd = ySpeed + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.zd = zSpeed + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.lifetime = (int)(16.0 / ((double)this.random.nextFloat() * 0.8 + 0.2)) + 2;
        this.roll = (float) Math.toRadians(this.random.nextIntBetweenInclusive(0, 360));
        this.oRoll = this.roll;

        float particleSize = this.random.nextBoolean() ? 0.07F : 0.08F;
        this.quadSize = particleSize;
        this.setSize(particleSize, particleSize);
        this.gravity *= gravityMultiplier;
    }

    @Override
    public void tick() {
        this.xd *= 0.949999988079071;
        this.yd *= 0.8999999761581421;
        this.zd *= 0.949999988079071;

        super.tick();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FallingDust(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, 1);
        }
    }

    public static class RandomisedSizeProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public RandomisedSizeProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FallingDust particle = new FallingDust(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, 1);
            float particleSize = MathHelpers.randomBetween(0.08f, 0.12f);
            particle.quadSize = particleSize;
            particle.setSize(particleSize, particleSize);
            return particle;
        }
    }
}