package games.enchanted.eg_particle_interactions.common.particle.types.splash;

import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.options.SimpleParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import games.enchanted.eg_particle_interactions.common.particle.render.layer.ParticleLayer;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class BucketSplash extends ParticleInteractionsParticle {
    protected BucketSplash(ParticleContext context, ParticleAppearance appearance, ParticleConfig config, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(context, appearance, config, x, y, z, xSpeed, ySpeed, zSpeed);

        this.friction = 0.999F;
        this.xd = xSpeed + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.yd = ySpeed + (Math.random() - 0.5) * ((this.random.nextFloat() * 0.5f) - 0.3F);
        this.zd = zSpeed + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;

        float particleSize = (float) 0.1355 - (this.random.nextBoolean() ? 0.01f : 0.0f);
        this.setScale(particleSize);
        this.setSize(particleSize, particleSize);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.removed && this.onGround && this.random.nextFloat() > 0.9f) {
            randomOnParticleLand();
        }
    }

    public void randomOnParticleLand() {
        this.remove();
    }

    public static class Provider implements PIParticleProvider<SimpleParticleOptions> {
        public Provider() {
        }

        @Override
        public @Nullable Particle createParticle(
            SimpleParticleOptions options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new BucketSplash(context, appearance, options.config(), x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}