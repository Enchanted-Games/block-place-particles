package games.enchanted.eg_particle_interactions.common.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.options.DustParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.options.value.RandomFloatProvider;
import games.enchanted.eg_particle_interactions.common.particle.options.value.RandomIntProvider;

public class ParticleConfig {
    public static final RandomFloatProvider DEFAULT_GRAVITY = new RandomFloatProvider(0.25F, 0.38F);
    public static final RandomIntProvider DEFAULT_LIFETIME = new RandomIntProvider(20, 40);
    public static final RandomFloatProvider DEFAULT_COLLISION_SIZE = new RandomFloatProvider(0.8f, 0.8f);
    public static final float DEFAULT_INITIAL_VELOCITY_RANDOMNESS = 0.01f;
    public static final RandomFloatProvider DEFAULT_GRAVITY_DECAY = new RandomFloatProvider(0f, 0f);
    public static final RandomFloatProvider DEFAULT_VELOCITY_DECAY = new RandomFloatProvider(0f, 0f);

    public static final ParticleConfig DEFAULT = new ParticleConfig(
        DEFAULT_GRAVITY, DEFAULT_LIFETIME, DEFAULT_COLLISION_SIZE
    );

    final RandomFloatProvider gravityProvider;
    final RandomIntProvider lifetimeProvider;
    final RandomFloatProvider collisionSizeProvider;
    final float initialVelocityRandomness;
    final RandomFloatProvider gravityDecayProvider;
    final RandomFloatProvider velocityDecayProvider;

    public ParticleConfig(
        RandomFloatProvider gravityProvider,
        RandomIntProvider lifetimeProvider,
        RandomFloatProvider collisionSizeProvider,
        float initialVelocityRandomness,
        RandomFloatProvider gravityDecayProvider,
        RandomFloatProvider velocityDecayProvider
    ) {
        this.gravityProvider = gravityProvider;
        this.lifetimeProvider = lifetimeProvider;
        this.collisionSizeProvider = collisionSizeProvider;
        this.initialVelocityRandomness = initialVelocityRandomness;
        this.gravityDecayProvider = gravityDecayProvider;
        this.velocityDecayProvider = velocityDecayProvider;
    }

    public ParticleConfig(RandomFloatProvider gravityProvider, RandomIntProvider lifetimeProvider, RandomFloatProvider collisionSizeProvider) {
        this(gravityProvider, lifetimeProvider, collisionSizeProvider, DEFAULT_INITIAL_VELOCITY_RANDOMNESS, DEFAULT_GRAVITY_DECAY, DEFAULT_VELOCITY_DECAY);
    }

    public ParticleConfig(RandomFloatProvider gravityProvider, RandomIntProvider lifetimeProvider) {
        this(gravityProvider, lifetimeProvider, DEFAULT_COLLISION_SIZE, DEFAULT_INITIAL_VELOCITY_RANDOMNESS, DEFAULT_GRAVITY_DECAY, DEFAULT_VELOCITY_DECAY);
    }

    public ParticleConfig(RandomFloatProvider gravityProvider, RandomIntProvider lifetimeProvider, float initialVelocityRandomness) {
        this(gravityProvider, lifetimeProvider, DEFAULT_COLLISION_SIZE, initialVelocityRandomness, DEFAULT_GRAVITY_DECAY, DEFAULT_VELOCITY_DECAY);
    }

    public ParticleConfig(RandomFloatProvider gravityProvider, RandomIntProvider lifetimeProvider, RandomFloatProvider collisionSizeProvider, float initialVelocityRandomness) {
        this(gravityProvider, lifetimeProvider, collisionSizeProvider, initialVelocityRandomness, DEFAULT_GRAVITY_DECAY, DEFAULT_VELOCITY_DECAY);
    }

    public static MapCodec<ParticleConfig> createCodec(ParticleConfig defaultConfig) {
        return RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                RandomFloatProvider.CODEC.optionalFieldOf("gravity", defaultConfig.getGravityProvider()).forGetter(ParticleConfig::getGravityProvider),
                RandomIntProvider.CODEC.optionalFieldOf("lifetime", defaultConfig.getLifetimeProvider()).forGetter(ParticleConfig::getLifetimeProvider),
                RandomFloatProvider.CODEC.optionalFieldOf("collision_size", defaultConfig.getCollisionSizeProvider()).forGetter(ParticleConfig::getCollisionSizeProvider),
                Codec.FLOAT.optionalFieldOf("initial_velocity_randomness", defaultConfig.getInitialVelocityRandomness()).forGetter(ParticleConfig::getInitialVelocityRandomness),
                RandomFloatProvider.CODEC.optionalFieldOf("gravity_decay", defaultConfig.getGravityDecayProvider()).forGetter(ParticleConfig::getGravityDecayProvider),
                RandomFloatProvider.CODEC.optionalFieldOf("velocity_decay", defaultConfig.getVelocityDecayProvider()).forGetter(ParticleConfig::getVelocityDecayProvider)
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

    public float getInitialVelocityRandomness() {
        return this.initialVelocityRandomness;
    }

    public RandomFloatProvider getGravityDecayProvider() {
        return this.gravityDecayProvider;
    }

    public RandomFloatProvider getVelocityDecayProvider() {
        return this.velocityDecayProvider;
    }
}
