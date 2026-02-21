package games.enchanted.eg_particle_interactions.common.particle.types.splash;

import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class BlockSplash extends BucketSplash {
    private final float uo;
    private final float vo;

    protected BlockSplash(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed);

        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;

        int[] colour = appearance.colourSource().getARGB(context);
        this.setRGBA(
            this.getRed() * (float) colour[1] / 255f,
            this.getGreen() * (float) colour[2] / 255f,
            this.getBlue() * (float) colour[3] / 255f,
            this.getAlpha() * (float) colour[0] / 255f
        );

        float particleSize = (float) 0.1255 - (this.random.nextBoolean() ? 0.01f : 0.02f);
        this.setScale(particleSize);
        this.setSize(particleSize, particleSize);
    }

    @Override
    protected @NonNull ParticleLayer getParticleLayer() {
        return ParticleLayer.TERRAIN;
    }

    @Override
    protected float getU0() {
        return this.currentSprite.getU((this.uo + 1.0F) / 4.0F);
    }

    @Override
    protected float getU1() {
        return this.currentSprite.getU(this.uo / 4.0F);
    }

    @Override
    protected float getV0() {
        return this.currentSprite.getV(this.vo / 4.0F);
    }

    @Override
    protected float getV1() {
        return this.currentSprite.getV((this.vo + 1.0F) / 4.0F);
    }

    // TODO: check if this custom light method is needed
//    @Override
//    public int getLightmapCoords(float f) {
//        int lightColour = super.getLightmapCoords(f);
//        return lightColour == 0 && this.level.hasChunkAt(this.pos) ?
//            LevelRenderer./*? if minecraft: < 26.1 {*/ /*getLightColor *//*?} else {*/ getLightCoords /*?}*/ (this.level, this.pos) :
//            lightColour
//        ;
//    }

    public static class Provider implements PIParticleProvider<PIParticleType.Simple> {
        public Provider() {
        }

        @Override
        public @Nullable Particle createParticle(
            PIParticleType.Simple type,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new BlockSplash(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
