package games.enchanted.eg_particle_interactions.common.particle.appearance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.options.value.RandomFloatProvider;

public record SpinConfig(RandomFloatProvider startingRotation, RandomFloatProvider startingSpeed, RandomFloatProvider maxSpeed, RandomFloatProvider acceleration) {
    private static final RandomFloatProvider ZERO = new RandomFloatProvider(0, 0);

    public static final SpinConfig NO_SPIN = new SpinConfig(ZERO, ZERO, ZERO, ZERO);

    public static final Codec<SpinConfig> CODEC = RecordCodecBuilder.create(i -> i
        .group(
            RandomFloatProvider.CODEC.optionalFieldOf("initial_rotation", ZERO).forGetter(SpinConfig::startingRotation),
            RandomFloatProvider.CODEC.optionalFieldOf("starting_speed", ZERO).forGetter(SpinConfig::startingSpeed),
            RandomFloatProvider.CODEC.optionalFieldOf("max_speed", new RandomFloatProvider(1, 1)).forGetter(SpinConfig::maxSpeed),
            RandomFloatProvider.CODEC.optionalFieldOf("acceleration", ZERO).forGetter(SpinConfig::acceleration)
        ).apply(
            i,
            SpinConfig::new
        )
    );
}
