package games.enchanted.eg_particle_interactions.common.particle.dust;

import games.enchanted.eg_particle_interactions.common.particle.ModParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BasicDust extends AbstractDust {
    private final Supplier<@Nullable ParticleOptions> speckGetter;

    protected BasicDust(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, float gravityMultiplier, boolean spawnSpecks, boolean spriteFromAge, Supplier<@Nullable ParticleOptions> speckGetter) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, gravityMultiplier, spawnSpecks);
        this.spriteFromAge = spriteFromAge;
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

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            BasicDust particle = new BasicDust(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 1.1f, true, true, () -> ModParticleTypes.SNOWFLAKE_SPECK);
            particle.emissive = true;
            return particle;
        }
    }
    public static class SnowflakeSpeckProvider implements ParticleProvider<SimpleParticleType>  {
        private final SpriteSet spriteSet;

        public SnowflakeSpeckProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            BasicDust particle = new BasicDust(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, 0.7f, false, false, () -> null);
            particle.emissive = true;
            return particle;
        }
    }
}
