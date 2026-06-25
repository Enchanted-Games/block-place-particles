package games.enchanted.eg_particle_interactions.common.particle.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public abstract class ParticleComponent<T> {
    public abstract Codec<T> codec();

    public abstract StreamCodec<? extends FriendlyByteBuf, T> streamCodec();

    static <T> ParticleComponent<T> create(Codec<T> codec, StreamCodec<? extends FriendlyByteBuf, T> streamCodec) {
        return new ParticleComponent<>() {
            final Codec<T> componentCodec = codec;
            final StreamCodec<? extends FriendlyByteBuf, T> componentStreamCodec = streamCodec;

            @Override
            public Codec<T> codec() {
                return this.componentCodec;
            }

            @Override
            public StreamCodec<? extends FriendlyByteBuf, T> streamCodec() {
                return this.componentStreamCodec;
            }
        };
    }

    record ComponentKeyAndValueCodec(ParticleComponent<?> componentType, boolean remove) {
        public static final Codec<ComponentKeyAndValueCodec> CODEC = Codec.STRING.flatXmap(
            string -> {
                boolean remove = string.startsWith("!");
                if (remove) {
                    string = string.substring("!".length());
                }

                Identifier id = ModCodecs.tryParseIdentifier(string);
                if(id == null) {
                    String message = "Invalid identifier '" + string + "'";
                    return DataResult.error(() -> message);
                }

                var ref = ParticleComponentRegistry.fromId(id);
                if (ref == null) {
                    return DataResult.error(() -> "Component '" + id + "' does not exist");
                } else {
                    return DataResult.success(new ComponentKeyAndValueCodec(ref.componentType(), remove));
                }
            },
            componentKeyAndValueCodec -> {
                ParticleComponent<?> component = componentKeyAndValueCodec.componentType();
                Identifier id = ParticleComponentRegistry.lookupId(component);
                return id == null ? DataResult.error(() -> "Unregistered component: " + component) : DataResult.success(componentKeyAndValueCodec.remove() ? "!" + id : id.toString());
            }
        );

        public Codec<?> valueCodec() {
            return this.remove ? Codec.EMPTY.codec() : this.componentType.codec();
        }
    }
}
