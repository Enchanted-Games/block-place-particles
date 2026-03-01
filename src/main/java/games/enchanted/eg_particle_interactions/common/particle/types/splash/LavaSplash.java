package games.enchanted.eg_particle_interactions.common.particle.types.splash;

import games.enchanted.eg_particle_interactions.common.particle.ParticleConfig;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.options.SimpleParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleTypes;
import org.jetbrains.annotations.Nullable;

public class LavaSplash extends BucketSplash {
    public LavaSplash(ParticleContext context, ParticleAppearance appearance, ParticleConfig config, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(context, appearance, config, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.removed) {
            float percentAge = (float) this.age / this.lifetime;
            if (this.random.nextFloat() < percentAge * 1.5 && this.random.nextFloat() > 0.5f) {
                this.level.addParticle(ParticleTypes.SMOKE, this.x, this.y, this.z, this.xd, this.yd, this.zd);
            }
        }
    }

    @Override
    public void randomOnParticleLand() {
        super.randomOnParticleLand();
        this.level.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0.03, 0);
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
            return new LavaSplash(context, appearance, options.config(), x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
