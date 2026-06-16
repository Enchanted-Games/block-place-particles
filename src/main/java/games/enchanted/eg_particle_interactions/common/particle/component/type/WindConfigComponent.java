package games.enchanted.eg_particle_interactions.common.particle.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.value.RandomFloatProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public record WindConfigComponent(SwirlConfig swirlConfig, Vector3fc flowAcceleration, Vector3fc maxFlowSpeed) {
    private static final Vector3f FLOW_DEFAULT = new Vector3f(0);
    public static final WindConfigComponent NO_WIND = new WindConfigComponent(SwirlConfig.UNSET, FLOW_DEFAULT, FLOW_DEFAULT);

    public static final Codec<WindConfigComponent> CODEC = RecordCodecBuilder.create(i -> i
        .group(
            SwirlConfig.CODEC.optionalFieldOf("swirl", SwirlConfig.UNSET).forGetter(WindConfigComponent::swirlConfig),
            ExtraCodecs.VECTOR3F.optionalFieldOf("flow_acceleration", FLOW_DEFAULT).forGetter(WindConfigComponent::flowAcceleration),
            ExtraCodecs.VECTOR3F.optionalFieldOf("max_flow_speed", FLOW_DEFAULT).forGetter(WindConfigComponent::flowAcceleration)
        ).apply(
            i,
            WindConfigComponent::new
        )
    );

    public static final StreamCodec<FriendlyByteBuf, WindConfigComponent> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.fromCodec(SwirlConfig.CODEC),
        WindConfigComponent::swirlConfig,
        ByteBufCodecs.fromCodec(ExtraCodecs.VECTOR3F),
        WindConfigComponent::flowAcceleration,
        ByteBufCodecs.fromCodec(ExtraCodecs.VECTOR3F),
        WindConfigComponent::maxFlowSpeed,
        WindConfigComponent::new
    );

    public record SwirlConfig(RandomFloatProvider swirlPeriod, RandomFloatProvider swirlStrength, boolean unset) {
        public static final SwirlConfig UNSET = new SwirlConfig(new RandomFloatProvider(0, 0), new RandomFloatProvider(0, 0), true);

        public static final Codec<SwirlConfig> CODEC = RecordCodecBuilder.create(i -> i
            .group(
                RandomFloatProvider.CODEC.fieldOf("period").forGetter(SwirlConfig::swirlPeriod),
                RandomFloatProvider.CODEC.fieldOf("strength").forGetter(SwirlConfig::swirlStrength)
            ).apply(
                i,
                (period, strength) -> new SwirlConfig(period, strength, false)
            )
        );
    }
}
