package games.enchanted.eg_particle_interactions.common.particle.types.splash;

import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.options.SimpleParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;

public class BlockSplash extends BucketSplash {
    private final float uo;
    private final float vo;

    protected BlockSplash(ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, ParticleConfig config, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(components, appearance, context, config, x, y, z, xSpeed, ySpeed, zSpeed);

        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;

        float particleSize = (float) 0.1255 - (this.random.nextBoolean() ? 0.01f : 0.02f);
        this.setScale(particleSize);
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


    public static class Provider implements PIParticleProvider<SimpleParticleOptions> {
        public Provider() {
        }

        @Override
        public @Nullable Particle createParticle(
            SimpleParticleOptions options,
            ParticleComponentMap components,
            ParticleAppearance appearance,
            ParticleContext context,
            double x,
            double z,
            double y,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new BlockSplash(components, appearance, context, options.config(), x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
