package games.enchanted.eg_particle_interactions.common.particle.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public record IntangibleLayersComponent(boolean terrain, boolean fluids) {
    public static final IntangibleLayersComponent COLLIDE_WITH_ALL = new IntangibleLayersComponent(false, false);

    public static final Codec<IntangibleLayersComponent> CODEC = RecordCodecBuilder.create(i -> i
        .group(
            Codec.BOOL.optionalFieldOf("terrain", false).forGetter(IntangibleLayersComponent::terrain),
            Codec.BOOL.optionalFieldOf("fluids", false).forGetter(IntangibleLayersComponent::fluids)
        ).apply(
            i,
            IntangibleLayersComponent::new
        )
    );

    public static final StreamCodec<FriendlyByteBuf, IntangibleLayersComponent> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.fromCodec(Codec.BOOL),
        IntangibleLayersComponent::terrain,
        ByteBufCodecs.fromCodec(Codec.BOOL),
        IntangibleLayersComponent::fluids,
        IntangibleLayersComponent::new
    );
}
