package games.enchanted.eg_particle_interactions.common.particle.component;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.util.Unit;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class ParticleComponentMap implements ParticleComponentGetter {
    public static final ParticleComponentMap EMPTY = new ParticleComponentMap(new Reference2ObjectArrayMap<>(0));

    public static final Codec<ParticleComponentMap> CODEC = Codec.dispatchedMap(
        ParticleComponent.ComponentKeyAndValueCodec.CODEC,
        ParticleComponent.ComponentKeyAndValueCodec::valueCodec
    ).xmap(
        keyAndValueMap -> {
            if(keyAndValueMap.isEmpty()) return EMPTY;
            Reference2ObjectArrayMap<ParticleComponent<?>, Optional<?>> parsed = new Reference2ObjectArrayMap<>(keyAndValueMap.size());

            for (Map.Entry<ParticleComponent.ComponentKeyAndValueCodec, ?> entry : keyAndValueMap.entrySet()) {
                ParticleComponent.ComponentKeyAndValueCodec componentKey = entry.getKey();
                if (componentKey.remove()) {
                    parsed.put(componentKey.componentType(), Optional.empty());
                    continue;
                }
                parsed.put(componentKey.componentType(), Optional.of(entry.getValue()));
            }

            return new ParticleComponentMap(parsed);
        },
        componentMap -> {
            Map<ParticleComponent.ComponentKeyAndValueCodec, Object> encoded = new Reference2ObjectArrayMap<>(componentMap.components.size());

            for (Map.Entry<ParticleComponent<?>, Optional<?>> entry : componentMap.components.entrySet()) {
                if(entry.getValue().isEmpty()) {
                    encoded.put(new ParticleComponent.ComponentKeyAndValueCodec(entry.getKey(), true), Unit.INSTANCE);
                    continue;
                }
                encoded.put(new ParticleComponent.ComponentKeyAndValueCodec(entry.getKey(), false), entry.getValue().get());
            }

            // this is horrible but its the only way to get the compiler to shut up
            //noinspection unchecked, rawtypes
            return (Map) encoded;
        }
    );

    final Reference2ObjectArrayMap<ParticleComponent<?>, Optional<?>> components;

    ParticleComponentMap(Reference2ObjectArrayMap<ParticleComponent<?>, Optional<?>> components) {
        this.components = components;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable <T> T get(ParticleComponent<? extends T> component) {
        Optional<?> defaultOptional = this.components.get(component);
        if(defaultOptional == null) return null;
        return (T) defaultOptional.orElse(null);
    }

    public static class Builder {
        final Reference2ObjectArrayMap<ParticleComponent<?>, Optional<?>> components = new Reference2ObjectArrayMap<>();

        public static Builder create() {
            return new Builder();
        }

        public <T> Builder set(ParticleComponentRegistry.ComponentReference<T> reference, @Nullable T value) {
            return this.set(reference.componentType(), value);
        }

        public <T> Builder set(ParticleComponent<T> component, @Nullable T value) {
            this.components.put(component, Optional.ofNullable(value));
            return this;
        }

        public ParticleComponentMap build() {
            return new ParticleComponentMap(this.components);
        }

        public static ParticleComponentMap combine(ParticleComponentMap defaults, ParticleComponentMap modified) {
            Reference2ObjectArrayMap<ParticleComponent<?>, Optional<?>> combined = new Reference2ObjectArrayMap<>();
            combined.putAll(defaults.components);
            combined.putAll(modified.components);
            return new ParticleComponentMap(combined);
        }
    }
}
