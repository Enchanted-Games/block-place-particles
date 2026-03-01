package games.enchanted.eg_particle_interactions.common.particle.types.dust;

import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.override_system.emitter.Emitter;
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

    protected Dust(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, DustParticleOptions dustParticleOptions) {
        super(context, appearance, dustParticleOptions.config(), x, y, z);

        this.speckEmitter = dustParticleOptions.getSpeckEmitter();
        this.spawnSpecks = this.speckEmitter != null;

        this.friction = 1.0F;
        this.xd = xSpeed + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.yd = ySpeed + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.zd = zSpeed + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.roll = (float) Math.toRadians(this.random.nextIntBetweenInclusive(0, 360));
        this.prevRoll = this.roll;

        float particleSize = this.random.nextBoolean() ? MIN_SIZE : MAX_SIZE;
        this.setScale(particleSize);
        this.setSize(particleSize, particleSize);
    }

    @Override
    public void tick() {
        this.pickSpriteForAppearance();

        this.xd *= 0.949999988079071;
        this.yd *= 0.8999999761581421;
        this.zd *= 0.949999988079071;

        this.gravity = 0.98F * this.gravity;
        this.friction = 0.995F * this.friction;

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
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new Dust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }

    public static class SnowflakeSpeckProvider implements PIParticleProvider<DustParticleOptions> {
        public SnowflakeSpeckProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new Dust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }

    public static class TintedDustProvider implements PIParticleProvider<DustParticleOptions> {
        public TintedDustProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new Dust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }

    public static class TintedDustSpeckProvider implements PIParticleProvider<DustParticleOptions> {
        public TintedDustSpeckProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new Dust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }

    public static class RedstoneProvider implements PIParticleProvider<DustParticleOptions> {
        public RedstoneProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            // TODO: replace this with better particle palette system
//            BlockState state = type.getState();
//            int powerLevel = 15;
//            if(state.hasProperty(RedstoneTorchBlock.LIT)) {
//                powerLevel = state.getValue(RedstoneTorchBlock.LIT) ? 15 : 0;
//            }
//            else if (state.hasProperty(ComparatorBlock.MODE)) {
//                powerLevel = state.getValue(ComparatorBlock.MODE) == ComparatorMode.SUBTRACT ? 15 : 0;
//            }
//            else if (state.hasProperty(RedStoneWireBlock.POWER)) {
//                powerLevel = Math.clamp(state.getValue(RedStoneWireBlock.POWER), 0, 15);
//            }
//            else if (state.hasProperty(RepeaterBlock.POWERED)) {
//                powerLevel = state.getValue(RepeaterBlock.POWERED) ? 15 : 0;
//            }
//            state = Blocks.REDSTONE_WIRE.defaultBlockState().setValue(RedStoneWireBlock.POWER, powerLevel);

            Dust particle = new Dust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, options);
            particle.roll = 0;
            particle.prevRoll = 0;
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
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new Dust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }

    public static class BrushSpeckProvider implements PIParticleProvider<DustParticleOptions> {
        public BrushSpeckProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new Dust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }

    public static class ItemFrameProvider implements PIParticleProvider<DustParticleOptions> {
        public ItemFrameProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new Dust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }

    public static class ItemFrameSpeckProvider implements PIParticleProvider<DustParticleOptions> {
        public ItemFrameSpeckProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new Dust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }

    public static class GlowItemFrameProvider implements PIParticleProvider<DustParticleOptions> {
        public GlowItemFrameProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new Dust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }

    public static class GlowItemFrameSpeckProvider implements PIParticleProvider<DustParticleOptions> {
        public GlowItemFrameSpeckProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            DustParticleOptions options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new Dust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }
}