package games.enchanted.blockplaceparticles.particle.spark;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SparkFlash extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float originalQuadSize;
    protected int prevAge;

    SparkFlash(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.speedUpWhenYMotionIsBlocked = true;
        this.friction = 0.96F;

        this.xd = (xSpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.07 * (this.random.nextFloat() > 0.95 ? 2 : 1);
        this.yd = (ySpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.07 * (this.random.nextFloat() > 0.95 ? 2 : 1);
        this.zd = (zSpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.07 * (this.random.nextFloat() > 0.95 ? 2 : 1);

        this.lifetime = this.random.nextInt(4) + 3;

        this.sprites = sprites;
        this.setSpriteFromAge(sprites);

        this.quadSize = 2/16f;
        originalQuadSize = quadSize;
        prevAge = age;
    }

    @Override
    public void tick() {
        prevAge = age;
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public int getLightColor(float f) {
        return 240;
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, @NotNull Camera renderInfo, float partialTicks) {
        this.quadSize = this.originalQuadSize * (0.5f + (Math.abs(1 - Mth.lerp(partialTicks, this.prevAge, this.age) / this.lifetime) * 0.5f));
        super.render(buffer, renderInfo, partialTicks);
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
            return new SparkFlash(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
        }
    }
}
