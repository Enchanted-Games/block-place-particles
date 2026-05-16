package games.enchanted.eg_particle_interactions.common.particle.component;

import org.jspecify.annotations.Nullable;

public interface ParticleComponentGetter {
    @Nullable
    <T> T get(ParticleComponent<? extends T> component);

    @Nullable
    default <T> T get(ParticleComponentRegistry.ComponentReference<? extends T> component) {
        return this.get(component.value());
    }

    default <T> T getOrDefault(final ParticleComponent<? extends T> component, final T defaultValue) {
        T value = this.get(component);
        return value != null ? value : defaultValue;
    }
}
