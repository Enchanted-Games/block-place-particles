package games.enchanted.eg_particle_interactions.common.particle.types.spark;

import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.override_system.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.ParticleTypesRegistry;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.options.SparkParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import games.enchanted.eg_particle_interactions.common.particle.types.physics.StretchyBouncyShapeParticle;
import games.enchanted.eg_particle_interactions.common.particle.util.ParticleSpawner;
import games.enchanted.eg_particle_interactions.common.shapes.ShapeDefinitions;
import games.enchanted.eg_particle_interactions.common.util.LightUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import org.jspecify.annotations.Nullable;

public class FlyingSpark extends StretchyBouncyShapeParticle {
    protected boolean hasSpawnedSmokeParticle = false;
    private static final int SPARK_UNDERWATER_DECAY_SPEED = 3;

    protected final @Nullable Emitter flashEmitter;

    protected FlyingSpark(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float gravity, int lifetime, SparkParticleOptions options) {
        super(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed);
        this.gravity = gravity;
        this.friction = 1.0f;

        this.xd = (xSpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.05 * (this.random.nextFloat() > 0.95 ? 2 : 1);
        this.yd = (ySpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.05 * (this.random.nextFloat() > 0.95 ? 2 : 1);
        this.zd = (zSpeed / 2) + (Math.random() * 3.0 - 1.5) * 0.05 * (this.random.nextFloat() > 0.95 ? 2 : 1);

        this.physics_bounciness = 0.8f;
        this.physics_passThroughFluidSpeed = 0.93f;

        this.lifetime = lifetime;

        float particleSize = (this.random.nextBoolean() ? 0.025f : 0.03f);
        this.setSize(particleSize, particleSize);
        this.setScale(particleSize);

        this.setShape(ShapeDefinitions.VERTICAL_CROSS);
        this.particleShapeScale.x = Mth.randomBetween(level.getRandom(), 0.4f, 1.1f);
        this.particleShapeScale.z = Mth.randomBetween(level.getRandom(), 0.4f, 1.1f);

        this.flashEmitter = options.getFlashEmitter();
    }

    @Override
    public void tick() {
        super.tick();
        if (age < 0 || this.removed) {
            return;
        }

        float percentageTimeUntilDeath = (float) this.age / this.lifetime;

        // spawn random spark flashes
        if (
            GeneralOptions.ADDITIONAL_SPARK_FLASH_EFFECT.getValue() && !this.hasEnteredWater &&
            (this.random.nextFloat() > percentageTimeUntilDeath + 0.8f || (this.random.nextFloat() < 0.01f && this.isParticleMoving()))
        ) {
            if(this.flashEmitter != null) {
                this.flashEmitter.spawnParticle(
                    this.context,
                    this.prevPrevX,
                    this.prevPrevY,
                    this.prevPrevZ,
                    0,
                    0,
                    0
                );
            }
        }

        if (!GeneralOptions.SPARK_WATER_EVAPORATION.getValue()) {
            this.hasSpawnedSmokeParticle = true;
        }
        if (this.hasEnteredWater && !this.hasSpawnedSmokeParticle) {
            ParticleSpawner.spawn(
                ParticleTypesRegistry.WATER_VAPOUR,
                this.context,
                this.xo,
                this.yo,
                this.zo,
                this.xd / 6,
                -this.yd / 2,
                this.zd / 6
            );
            this.level.playLocalSound(this.xo, this.yo, this.zo, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.AMBIENT, 0.15f, 1.2f, false);
            this.hasSpawnedSmokeParticle = true;
        }
    }

    @Override
    protected int getAgeForSprite() {
        return Math.clamp((long) this.age * (this.hasEnteredWater ? SPARK_UNDERWATER_DECAY_SPEED : 1), 0, this.lifetime);
    }

    @Override
    public int getLightmapCoords(float partialTicks) {
        int adjustedAge = this.getAgeForSprite();
        float percentageTimeAlive = Math.abs(1 - ((float) adjustedAge / this.lifetime));
        int sparkLight = (int) (percentageTimeAlive * 15f);

        BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
        int blockLight = this.level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = this.level.getBrightness(LightLayer.SKY, pos);

        return LightUtil.pack(Math.max(blockLight, sparkLight), skyLight);
    }

    public static class FlyingSparkProvider implements PIParticleProvider<SparkParticleOptions> {
        public FlyingSparkProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            SparkParticleOptions options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            ClientLevel level = context.level();
            return new FlyingSpark(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, Mth.randomBetween(level.getRandom(), 0.8F, 0.9F), Mth.randomBetweenInclusive(level.getRandom(), 20, 60), options);
        }
    }

    public static class FloatingSparkProvider implements PIParticleProvider<SparkParticleOptions> {
        public FloatingSparkProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            SparkParticleOptions options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            ClientLevel level = context.level();
            return new FlyingSpark(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, Mth.randomBetween(level.getRandom(), 0.2F, 0.3F), Mth.randomBetweenInclusive(level.getRandom(), 4, 12), options);
        }
    }
}
