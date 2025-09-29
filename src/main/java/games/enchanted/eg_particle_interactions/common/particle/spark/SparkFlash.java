package games.enchanted.eg_particle_interactions.common.particle.spark;

import games.enchanted.eg_particle_interactions.common.particle.compat.CustomGeometryParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class SparkFlash extends CustomGeometryParticle {
    private final SpriteSet sprites;
    private final float originalQuadSize;
    protected int prevAge;
    protected final boolean useRandomAnimation;

    SparkFlash(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites, boolean useRandomAnimation) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites.get(0, 1));
        this.speedUpWhenYMotionIsBlocked = true;
        this.friction = 0.96F;

        this.xd = (xSpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.07 * (this.random.nextFloat() > 0.95 ? 2 : 1);
        this.yd = (ySpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.07 * (this.random.nextFloat() > 0.95 ? 2 : 1);
        this.zd = (zSpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.07 * (this.random.nextFloat() > 0.95 ? 2 : 1);
        this.useRandomAnimation = useRandomAnimation;
        if(useRandomAnimation) {
            int rot = this.random.nextIntBetweenInclusive(0, 3);
            this.roll = rot * 90;
            this.oRoll = roll;
        }

        this.lifetime = this.random.nextInt(4) + 3;

        this.sprites = sprites;
        this.setSpriteFromAge(sprites);

        this.setScale(2/16f);
        originalQuadSize = this.getScale();
        prevAge = age;
    }

    @Override
    public void tick() {
        prevAge = age;
        super.tick();
        if(useRandomAnimation) {
            //? if minecraft: <= 1.21.8 {
            /*this.pickSprite(this.sprites);
            *///?} else {
            this.setSprite(this.sprites.get(this.random));
            //?}
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    public int getLightColor(float f) {
        return 240;
    }

    @Override
    protected void renderTick(float partialTicks) {
        this.setScale(this.originalQuadSize * (0.5f + (Math.abs(1 - Mth.lerp(partialTicks, this.prevAge, this.age) / this.lifetime) * 0.5f)));
    }

    @Override
    protected ParticleLayer getParticleLayer() {
        return ParticleLayer.OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(
            SimpleParticleType options,
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
            //? if minecraft: > 1.21.8 {
            , RandomSource random
            //?}
        ) {
            return new SparkFlash(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, false);
        }
    }

    public static class RandomAnimationProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public RandomAnimationProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(
            SimpleParticleType options,
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
            //? if minecraft: > 1.21.8 {
            , RandomSource random
            //?}
        ) {
            return new SparkFlash(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, true);
        }
    }
}
