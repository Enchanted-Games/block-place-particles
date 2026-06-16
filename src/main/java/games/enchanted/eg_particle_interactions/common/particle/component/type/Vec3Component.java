package games.enchanted.eg_particle_interactions.common.particle.component.type;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public record Vec3Component(Vec3 vec3) {
    public static final Vec3Component ONE = new Vec3Component(new Vec3(1, 1, 1));
    public static final Vec3Component ZERO = new Vec3Component(new Vec3(0, 0, 0));

    private static final Codec<Vec3Component> DOUBLE_CODEC = Codec.DOUBLE.xmap(
        val -> new Vec3Component(new Vec3(val, val, val)),
        vec3Component -> vec3Component.vec3().x()
    );
    public static final Codec<Vec3Component> CODEC = Vec3.CODEC.xmap(
        Vec3Component::new,
        Vec3Component::vec3
    ).withAlternative(DOUBLE_CODEC);

    public static final StreamCodec<FriendlyByteBuf, Vec3Component> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.fromCodec(Vec3.CODEC),
        Vec3Component::vec3,
        Vec3Component::new
    );

    public static Vec3Component scalar(float value) {
        return new Vec3Component(new Vec3(value, value, value));
    }
}
