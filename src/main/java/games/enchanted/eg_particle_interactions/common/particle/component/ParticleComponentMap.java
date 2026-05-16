package games.enchanted.eg_particle_interactions.common.particle.component;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class ParticleComponentMap implements ParticleComponentGetter {
    public static final ParticleComponentMap EMPTY = new ParticleComponentMap(new Reference2ObjectOpenHashMap<>(0));

    final Reference2ObjectOpenHashMap<ParticleComponent<?>, Optional<?>> defaultComponents;
    final Reference2ObjectOpenHashMap<ParticleComponent<?>, Optional<?>> components;

    ParticleComponentMap(Reference2ObjectOpenHashMap<ParticleComponent<?>, Optional<?>> defaultComponents) {
        this.defaultComponents = defaultComponents;
        this.components = new Reference2ObjectOpenHashMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable <T> T get(ParticleComponent<? extends T> component) {
        if(this.components.containsKey(component)) {
            return (T) this.defaultComponents.get(component);
        }
        Optional<T> defaultOptional = (Optional<T>) this.defaultComponents.get(component);
        if(defaultOptional == null) return null;
        return defaultOptional.orElse(null);
    }

    public static class Builder {
        final Reference2ObjectOpenHashMap<ParticleComponent<?>, Optional<?>> components = new Reference2ObjectOpenHashMap<>();

        public static Builder create() {
            return new Builder();
        }

        public <T> Builder set(ParticleComponentRegistry.ComponentReference<T> reference, @Nullable T value) {
            return this.set(reference.value(), value);
        }

        public <T> Builder set(ParticleComponent<T> component, @Nullable T value) {
            this.components.put(component, Optional.ofNullable(value));
            return this;
        }

        public ParticleComponentMap build() {
            return new ParticleComponentMap(this.components);
        }
    }
}
