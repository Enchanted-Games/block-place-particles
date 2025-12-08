package games.enchanted.eg_particle_interactions.common.particle.types.dust;

import games.enchanted.eg_particle_interactions.common.particle.ModParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BasicDust extends AbstractDust {
    private final Supplier<@Nullable ParticleOptions> speckGetter;

    protected BasicDust(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, float gravityMultiplier, boolean spawnSpecks, boolean spriteFromAge, Supplier<@Nullable ParticleOptions> speckGetter) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, gravityMultiplier, spawnSpecks, spriteFromAge);
        this.speckGetter = speckGetter;
    }

    @Override
    protected @Nullable ParticleOptions getSpeckParticle() {
        return speckGetter.get();
    }

    public static class SnowflakeProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public SnowflakeProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(
            SimpleParticleType options,
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
            return new BasicDust(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 1.1f, true, true, () -> ModParticleTypes.SNOWFLAKE_SPECK);
        }
    }
    public static class SnowflakeSpeckProvider implements ParticleProvider<SimpleParticleType>  {
        private final SpriteSet spriteSet;

        public SnowflakeSpeckProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(
            SimpleParticleType options,
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
            return new BasicDust(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 0.7f, false, false, () -> null);
        }
    }
}
