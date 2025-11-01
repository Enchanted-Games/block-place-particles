package games.enchanted.eg_particle_interactions.common.particle.types.splash;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockSplash extends BucketSplash {
    private final BlockPos pos;
    private final float uo;
    private final float vo;

    protected BlockSplash(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockPos blockPos, BlockState blockState) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(blockState));
        this.pos = blockPos;
        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;

        int tintColour = Minecraft.getInstance().getBlockColors().getColor(blockState, level, blockPos, 0);
        this.rCol *= (float)(tintColour >> 16 & 0xFF) / 255.0F;
        this.gCol *= (float)(tintColour >> 8 & 0xFF) / 255.0F;
        this.bCol *= (float)(tintColour & 0xFF) / 255.0F;

        float particleSize = (float) 0.1255 - (this.random.nextBoolean() ? 0.01f : 0.02f);
        this.setScale(particleSize);
        this.setSize(particleSize, particleSize);
    }

    @Override
    protected ParticleLayer getParticleLayer() {
        return ParticleLayer.TERRAIN;
    }

    @Override
    protected float getU0() {
        return this.sprite.getU((this.uo + 1.0F) / 4.0F);
    }

    @Override
    protected float getU1() {
        return this.sprite.getU(this.uo / 4.0F);
    }

    @Override
    protected float getV0() {
        return this.sprite.getV(this.vo / 4.0F);
    }

    @Override
    protected float getV1() {
        return this.sprite.getV((this.vo + 1.0F) / 4.0F);
    }

    @Override
    public int getLightColor(float f) {
        int lightColour = super.getLightColor(f);
        return lightColour == 0 && this.level.hasChunkAt(this.pos) ? LevelRenderer.getLightColor(this.level, this.pos) : lightColour;
    }

    public static class Provider implements ParticleProvider<BlockParticleOption> {
        public Provider(SpriteSet spriteSet) {}

        @Override
        public @Nullable Particle createParticle(
            BlockParticleOption type,
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
            return new BlockSplash(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState());
        }
    }
}
