package games.enchanted.eg_particle_interactions.common.particle.types.dust;

import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.options.DustParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import net.minecraft.client.particle.Particle;
import org.jspecify.annotations.Nullable;

public class Dust extends ParticleInteractionsParticle {
    public static float MIN_SIZE = 0.095f;
    public static float MAX_SIZE = 0.125f;

    protected @Nullable Emitter speckEmitter;
    protected boolean spawnSpecks;

    protected Dust(ParticleComponentMap components, ParticleAppearance appearance, ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, DustParticleOptions dustParticleOptions) {
        super(components, appearance, context, dustParticleOptions.config(), x, y, z, ySpeed, zSpeed, xSpeed);

        this.speckEmitter = dustParticleOptions.getSpeckEmitter();
        this.spawnSpecks = this.speckEmitter != null;

        this.spin = (float) Math.toRadians(this.random.nextIntBetweenInclusive(0, 360));
        this.prevSpin = this.spin;

        float particleSize = this.random.nextBoolean() ? MIN_SIZE : MAX_SIZE;
        this.setScale(particleSize);
        this.setSize(particleSize, particleSize);
    }

    @Override
    public void tick() {
        this.pickSpriteForAppearance();
        super.tick();

        if (!this.spawnSpecks || this.removed || !this.hasPhysics || this.onGround) {
            return;
        }
        if (!GeneralOptions.DUST_SPECKS.getValue()) {
            return;
        }
        if ((this.age < 3 && this.random.nextFloat() < 0.23f) || this.random.nextFloat() < 0.01f) {
            Emitter speckEmitter = this.speckEmitter();
            if (speckEmitter != null) {
                speckEmitter.spawnParticle(this.context, this.x, this.y, this.z, this.xd / 2, (this.yd / 2) + 0.05, this.zd / 2);
            }
        }
    }

    protected @Nullable Emitter speckEmitter() {
        return this.speckEmitter;
    }


    public static class SnowflakeProvider implements PIParticleProvider<DustParticleOptions> {
        public SnowflakeProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
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
            return new Dust(components, appearance, context, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }

    public static class SnowflakeSpeckProvider implements PIParticleProvider<DustParticleOptions> {
        public SnowflakeSpeckProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
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
            return new Dust(components, appearance, context, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }

    public static class TintedDustProvider implements PIParticleProvider<DustParticleOptions> {
        public TintedDustProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
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
            return new Dust(components, appearance, context, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }

    public static class TintedDustSpeckProvider implements PIParticleProvider<DustParticleOptions> {
        public TintedDustSpeckProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
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
            return new Dust(components, appearance, context, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }

    public static class RedstoneProvider implements PIParticleProvider<DustParticleOptions> {
        public RedstoneProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
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
            Dust particle = new Dust(components, appearance, context, x, y, z, xSpeed, ySpeed, zSpeed, options);
            particle.spin = 0;
            particle.prevSpin = 0;
            particle.lifetime = (int) (particle.lifetime * 0.4f);
            particle.friction = 0.9f;
            return particle;
        }
    }


    public static class BrushProvider implements PIParticleProvider<DustParticleOptions> {
        public BrushProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
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
            return new Dust(components, appearance, context, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }

    public static class BrushSpeckProvider implements PIParticleProvider<DustParticleOptions> {
        public BrushSpeckProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
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
            return new Dust(components, appearance, context, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }

    public static class ItemFrameProvider implements PIParticleProvider<DustParticleOptions> {
        public ItemFrameProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
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
            return new Dust(components, appearance, context, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }

    public static class ItemFrameSpeckProvider implements PIParticleProvider<DustParticleOptions> {
        public ItemFrameSpeckProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
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
            return new Dust(components, appearance, context, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }

    public static class GlowItemFrameProvider implements PIParticleProvider<DustParticleOptions> {
        public GlowItemFrameProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
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
            return new Dust(components, appearance, context, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }

    public static class GlowItemFrameSpeckProvider implements PIParticleProvider<DustParticleOptions> {
        public GlowItemFrameSpeckProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
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
            return new Dust(components, appearance, context, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }
}