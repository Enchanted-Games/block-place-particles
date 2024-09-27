package games.enchanted.blockplaceparticles.particle.bubble;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UnderwaterRisingBubble extends TextureSheetParticle {
    protected UnderwaterRisingBubble(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.setSprite(spriteSet.get(this.random));
        this.gravity = -0.35F;
        this.friction = 0.85F;
        this.setSize(0.02F, 0.02F);
        this.quadSize = this.quadSize * (this.random.nextFloat() * 0.6F + 0.2F);
        this.xd = xSpeed * 0.2F + (Math.random() * 2.0 - 1.0) * 0.02F;
        this.yd = ySpeed * 0.2F + (Math.random() * 2.0 - 1.0) * 0.02F;
        this.zd = zSpeed * 0.2F + (Math.random() * 2.0 - 1.0) * 0.02F;
        this.lifetime = (int)(40.0 / (Math.random() * 0.8 + 0.2));
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.age > 2 && !this.removed && !this.level.getFluidState(BlockPos.containing(this.x, this.y - 0.125f, this.z)).is(FluidTags.WATER)) {
            this.popAndRemove();
        }
        else if(this.age >= this.lifetime - 1) {
            this.popAndRemove();
        }
    }

    private void popAndRemove() {
        this.level.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z, 0, 0, 0);
        this.remove();
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
            return new UnderwaterRisingBubble(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
        }
    }
}
