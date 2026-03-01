package games.enchanted.eg_particle_interactions.common.particle;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.options.value.RandomFloatProvider;
import games.enchanted.eg_particle_interactions.common.particle.options.value.RandomIntProvider;

public class ParticleConfig {
    public static final RandomFloatProvider DEFAULT_GRAVITY = new RandomFloatProvider(0.25F, 0.38F);
    public static final RandomIntProvider DEFAULT_LIFETIME = new RandomIntProvider(20, 40);
    public static final RandomFloatProvider DEFAULT_COLLISION_SIZE = new RandomFloatProvider(0.8f, 0.8f);

    public static final ParticleConfig DEFAULT = new ParticleConfig(
        DEFAULT_GRAVITY, DEFAULT_LIFETIME, DEFAULT_COLLISION_SIZE
    );

    final RandomFloatProvider gravityProvider;
    final RandomIntProvider lifetimeProvider;
    final RandomFloatProvider collisionSizeProvider;

    public ParticleConfig(RandomFloatProvider gravityProvider, RandomIntProvider lifetimeProvider, RandomFloatProvider collisionSizeProvider) {
        this.gravityProvider = gravityProvider;
        this.lifetimeProvider = lifetimeProvider;
        this.collisionSizeProvider = collisionSizeProvider;
    }

    public ParticleConfig(RandomFloatProvider gravityProvider, RandomIntProvider lifetimeProvider) {
        this(gravityProvider, lifetimeProvider, DEFAULT_COLLISION_SIZE);
    }

    public static MapCodec<ParticleConfig> createCodec(ParticleConfig defaultConfig) {
        return RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                RandomFloatProvider.CODEC.optionalFieldOf("gravity", defaultConfig.getGravityProvider()).forGetter(ParticleConfig::getGravityProvider),
                RandomIntProvider.CODEC.optionalFieldOf("lifetime", defaultConfig.getLifetimeProvider()).forGetter(ParticleConfig::getLifetimeProvider),
                RandomFloatProvider.CODEC.optionalFieldOf("collision_size", defaultConfig.getCollisionSizeProvider()).forGetter(ParticleConfig::getCollisionSizeProvider)
            ).apply(
                instance,
                ParticleConfig::new
            )
        );
    }

    public RandomFloatProvider getGravityProvider() {
        return this.gravityProvider;
    }

    public RandomIntProvider getLifetimeProvider() {
        return this.lifetimeProvider;
    }

    public RandomFloatProvider getCollisionSizeProvider() {
        return this.collisionSizeProvider;
    }
}
