package games.enchanted.eg_particle_interactions.common.particle.component;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class ParticleComponentMap implements ParticleComponentGetter {
    public static final ParticleComponentMap EMPTY = new ParticleComponentMap(new Reference2ObjectOpenHashMap<>(0));

    final Reference2ObjectOpenHashMap<ParticleComponent<?>, Optional<?>> defaultComponents;
    final Reference2ObjectOpenHashMap<ParticleComponent<?>, Optional<?>> modifiedComponents;

    ParticleComponentMap(Reference2ObjectOpenHashMap<ParticleComponent<?>, Optional<?>> defaultComponents) {
        this(defaultComponents, new Reference2ObjectOpenHashMap<>());
    }

    ParticleComponentMap(Reference2ObjectOpenHashMap<ParticleComponent<?>, Optional<?>> defaultComponents, Reference2ObjectOpenHashMap<ParticleComponent<?>, Optional<?>> modifiedComponents) {
        this.defaultComponents = defaultComponents;
        this.modifiedComponents = modifiedComponents;
    }

    @Override
    public @Nullable <T> T get(ParticleComponent<? extends T> component) {
        if(this.modifiedComponents.containsKey(component)) {
            return (T) this.modifiedComponents.get(component).orElse(null);
        }
        Optional<?> defaultOptional = this.defaultComponents.get(component);
        if(defaultOptional == null) return null;
        return (T) defaultOptional.orElse(null);
    }

    public static class Builder {
        final Reference2ObjectOpenHashMap<ParticleComponent<?>, Optional<?>> components = new Reference2ObjectOpenHashMap<>();
        final boolean defaultsBuilder;

        Builder(boolean defaultsBuilder) {
            this.defaultsBuilder = defaultsBuilder;
        }

        public static Builder createDefaults() {
            return new Builder(true);
        }

        public static Builder createModifications() {
            return new Builder(false);
        }

        public <T> Builder set(ParticleComponentRegistry.ComponentReference<T> reference, @Nullable T value) {
            return this.set(reference.value(), value);
        }

        public <T> Builder set(ParticleComponent<T> component, @Nullable T value) {
            this.components.put(component, Optional.ofNullable(value));
            return this;
        }

        public ParticleComponentMap build() {
            if(this.defaultsBuilder) {
                return new ParticleComponentMap(this.components);
            }
            return new ParticleComponentMap(new Reference2ObjectOpenHashMap<>(), this.components);
        }

        public static ParticleComponentMap combine(ParticleComponentMap defaults, ParticleComponentMap modified) {
            return new ParticleComponentMap(defaults.defaultComponents, modified.modifiedComponents);
        }
    }
}
