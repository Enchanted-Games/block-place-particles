package games.enchanted.blockplaceparticles.particle.spark;

import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import games.enchanted.blockplaceparticles.particle.StretchyBouncyShapeParticle;
import games.enchanted.blockplaceparticles.shapes.ShapeDefinitions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlyingSpark extends StretchyBouncyShapeParticle {
    private final SpriteSet sprites;

    protected FlyingSpark(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float gravity, int lifetime, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.setSprite(spriteSet.get(this.random.nextInt(12), 12));
        this.gravity = gravity;
        this.friction = 1.0F;

        this.xd = (xSpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.05000000074505806 * (this.random.nextFloat() > 0.95 ? 2 : 1);
        this.yd = (ySpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.05000000074505806 * (this.random.nextFloat() > 0.95 ? 2 : 1);
        this.zd = (zSpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.05000000074505806 * (this.random.nextFloat() > 0.95 ? 2 : 1);

        this.physics_bounceDecay = 0.6f;

        this.rCol = this.gCol = 1;
        this.bCol = 0.9f;

        this.lifetime = lifetime;

        float particleSize = (this.random.nextBoolean() ? 0.025F : 0.03F);
        this.setSize(particleSize, particleSize);
        this.quadSize = particleSize;

        this.sprites = spriteSet;
        this.setSpriteFromAge(this.sprites);

        this.setShape(ShapeDefinitions.VERTICAL_CROSS);
        this.particleShapeScale.x = Mth.randomBetween(level.random, 0.4f, 1.1f);
        this.particleShapeScale.z = Mth.randomBetween(level.random, 0.4f, 1.1f);
    }

    @Override
    public void tick() {
        super.tick();
        if(age < 0 || this.removed) {
            return;
        }

        this.setSpriteFromAge(this.sprites);

        float percentageTimeUntilDeath = (float) this.age / this.lifetime;

        // spawn random spark flashes
        if(
            this.random.nextFloat() > percentageTimeUntilDeath + 0.85f ||
            (this.random.nextFloat() < 0.008f && this.isParticleMoving())
        ) {
            this.level.addParticle(ModParticleTypes.SPARK_FLASH, this.prevPrevX, this.prevPrevY, this.prevPrevZ, 0, 0, 0);
        }

    }

    @Override
    public int getLightColor(float partialTicks) {
        float percentageTimeAlive = Math.abs(1 - ((float) this.age / this.lifetime));
        int sparkLight = (int) (percentageTimeAlive * 15f);

        BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
        int blockLight = this.level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = this.level.getBrightness(LightLayer.SKY, pos);

        return LightTexture.pack(Math.max(blockLight, sparkLight), skyLight);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class LongLifeSparkProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public LongLifeSparkProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FlyingSpark(level, x, y, z, xSpeed, ySpeed, zSpeed, Mth.randomBetween(level.random, 0.8F, 0.9F), Mth.randomBetweenInclusive(level.random, 20, 60), spriteSet);
        }
    }

    public static class ShortLifeSparkProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public ShortLifeSparkProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FlyingSpark(level, x, y, z, xSpeed, ySpeed, zSpeed, Mth.randomBetween(level.random, 0.2F, 0.3F), Mth.randomBetweenInclusive(level.random, 4, 12), spriteSet);
        }
    }

    public static class ShortestLifeSparkProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public ShortestLifeSparkProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FlyingSpark(level, x, y, z, xSpeed, ySpeed, zSpeed, Mth.randomBetween(level.random, 0.2F, 0.3F), Mth.randomBetweenInclusive(level.random, 1, 5), spriteSet);
        }
    }
}
