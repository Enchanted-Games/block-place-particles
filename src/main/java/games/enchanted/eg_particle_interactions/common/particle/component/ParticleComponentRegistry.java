package games.enchanted.eg_particle_interactions.common.particle.component;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ParticleComponentRegistry {
    private static final BiMap<Identifier, ComponentReference<?>> BY_ID = HashBiMap.create();
    private static final Map<ParticleComponent<?>, ComponentReference<?>> BY_VALUE = new HashMap<>();

    public static final Codec<ComponentReference<?>> BY_NAME_REFERENCE_CODEC = ModCodecs.IDENTIFIER.flatXmap(
        identifier -> {
            if(BY_ID.containsKey(identifier)) {
                return DataResult.success(BY_ID.get(identifier));
            }
            return DataResult.error(() -> "Unregistered particle component '" + identifier + "'");
        },
        reference -> {
            if(BY_ID.inverse().containsKey(reference)) {
                return DataResult.success(BY_ID.inverse().get(reference));
            }
            return DataResult.error(() -> "Failed to get id for unregistered particle component");
        }
    );

    public static <T> ComponentReference<T> register(
        Identifier id,
        ParticleComponent<T> component
    ) {
        if(BY_ID.containsKey(id)) {
            throw new IllegalStateException("Particle component '" + id + "' has already been registered!");
        }
        ComponentReference<T> ref = new ComponentReference<>(id, component);
        BY_ID.put(id, ref);
        BY_VALUE.put(component, ref);
        return ref;
    }

    public static @Nullable ComponentReference<?> fromId(Identifier id) {
        if(!BY_ID.containsKey(id)) {
            return null;
        }
        return BY_ID.get(id);
    }

    public static ComponentReference<?> fromValue(ParticleComponent<?> value) {
        if(!BY_VALUE.containsKey(value)) {
            throw new IllegalStateException("Tried to get id for unregistered particle component");
        }
        return BY_VALUE.get(value);
    }

    public static @Nullable Identifier lookupId(ParticleComponent<?> component) {
        if(!BY_VALUE.containsKey(component)) {
            return null;
        }
        return BY_VALUE.get(component).id();
    }

    public record ComponentReference<T>(Identifier id, ParticleComponent<T> componentType) {
    }
}
