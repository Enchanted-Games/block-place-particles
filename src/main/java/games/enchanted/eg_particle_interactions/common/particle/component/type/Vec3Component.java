package games.enchanted.eg_particle_interactions.common.particle.component.type;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.particle.options.value.RandomIntProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public record Vec3Component(Vec3 vec3) {
    public static final Vec3Component ONE = new Vec3Component(new Vec3(1, 1, 1));
    public static final Vec3Component ZERO = new Vec3Component(new Vec3(0, 0, 0));

    public static final Codec<Vec3Component> CODEC = Vec3.CODEC.xmap(
        Vec3Component::new,
        Vec3Component::vec3
    );
    public static final StreamCodec<FriendlyByteBuf, Vec3Component> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.fromCodec(Vec3.CODEC),
        Vec3Component::vec3,
        Vec3Component::new
    );
}
