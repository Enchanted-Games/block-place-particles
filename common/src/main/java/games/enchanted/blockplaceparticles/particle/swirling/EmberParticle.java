package games.enchanted.blockplaceparticles.particle.swirling;

import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EmberParticle extends SwirlingParticle {
    protected EmberParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, boolean shouldSwirl) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, shouldSwirl);
        this.gravity = MathHelpers.randomBetween(-0.03f, -0.08f);

        this.lifetime = MathHelpers.randomBetween(30, 100);

        this.rotSpeed = 0f;
        this.spinAcceleration = 0f;
        this.swirlStrength = MathHelpers.randomBetween(-0.9f, 0.9f);
        this.swirlPeriod = MathHelpers.randomBetween(650,750);

        this.quadSize = 2.5f/16f;
    }

    @Override
    public void applyGravity() {
        this.yd = -this.gravity;
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

    public static class EmberProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public EmberProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new EmberParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, true);
        }
    }
}
