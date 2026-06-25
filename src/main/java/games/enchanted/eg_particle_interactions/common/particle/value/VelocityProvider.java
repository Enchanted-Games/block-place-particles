package games.enchanted.eg_particle_interactions.common.particle.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import net.minecraft.util.StringRepresentable;
import org.joml.Vector3d;

public record VelocityProvider(Type type, Vector3d value) {
    public static final Codec<VelocityProvider> CODEC = RecordCodecBuilder.create(i -> i
        .group(
            StringRepresentable.fromValues(Type::values).optionalFieldOf("type", Type.STATIC).forGetter(VelocityProvider::type),
            ModCodecs.VECTOR3D.fieldOf("value").forGetter(VelocityProvider::value)
        ).apply(
            i,
            VelocityProvider::new
        )
    );

    public Vector3d getVelocity(Vector3d velocity) {
        if(this.type() == Type.STATIC) return this.value();
        return velocity.mul(this.value());
    }

    public enum Type implements StringRepresentable {
        STATIC("static"),
        RELATIVE("relative");

        final String id;

        Type(String id) {
            this.id = id;
        }

        @Override
        public String getSerializedName() {
            return this.id;
        }
    }
}
