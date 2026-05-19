package games.enchanted.eg_particle_interactions.common.particle.component;

import org.jspecify.annotations.Nullable;

public interface ParticleComponentGetter {
    @Nullable
    <T> T get(ParticleComponent<? extends T> component);

    @Nullable
    default <T> T get(ParticleComponentRegistry.ComponentReference<? extends T> component) {
        return this.get(component.componentType());
    }

    default <T> T getOrFallback(ParticleComponentRegistry.ComponentReference<? extends T> component, T fallback) {
        var value = this.get(component.componentType());
        return value == null ? fallback : value;
    }
}
