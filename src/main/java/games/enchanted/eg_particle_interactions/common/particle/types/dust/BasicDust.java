package games.enchanted.eg_particle_interactions.common.particle.types.dust;

import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.ParticleTypesRegistry;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import net.minecraft.client.particle.Particle;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public class BasicDust extends AbstractDust {
    private final Supplier<@Nullable PIParticleOptions> speckGetter;

    protected BasicDust(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float gravityMultiplier, boolean spawnSpecks, Supplier<@Nullable PIParticleOptions> speckGetter) {
        super(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, gravityMultiplier, spawnSpecks);
        this.speckGetter = speckGetter;
    }

    @Override
    protected @Nullable PIParticleOptions getSpeckParticle() {
        return speckGetter.get();
    }

    public static class SnowflakeProvider implements PIParticleProvider<PIParticleType.Simple> {
        public SnowflakeProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            PIParticleType.Simple options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new BasicDust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 1.1f, true, () -> ParticleTypesRegistry.SNOWFLAKE_SPECK);
        }
    }

    public static class SnowflakeSpeckProvider implements PIParticleProvider<PIParticleType.Simple> {
        public SnowflakeSpeckProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            PIParticleType.Simple options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new BasicDust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 0.7f, false, () -> null);
        }
    }
}
