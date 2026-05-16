package games.enchanted.eg_particle_interactions.common.particle.component;

import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ParticleComponentRegistry {
    public static final ParticleComponentRegistry INSTANCE = new ParticleComponentRegistry();

    private final Map<Identifier, ComponentReference<?>> byId = new HashMap<>();
    private final Map<ParticleComponent<?>, ComponentReference<?>> byValue = new HashMap<>();

    public <T> ComponentReference<T> register(
        Identifier id,
        ParticleComponent<T> component
    ) {
        if(this.byId.containsKey(id)) {
            throw new IllegalStateException("Particle component '" + id + "' has already been registered!");
        }
        ComponentReference<T> ref = new ComponentReference<>(id, component);
        this.byId.put(id, ref);
        this.byValue.put(component, ref);
        return ref;
    }

    public ComponentReference<?> fromId(Identifier id) {
        if(!this.byId.containsKey(id)) {
            throw new IllegalStateException("Tried to get non-existent particle component '" + id + "', not registered");
        }
        return this.byId.get(id);
    }

    public ComponentReference<?> fromValue(ParticleComponent<?> value) {
        if(!this.byValue.containsKey(value)) {
            throw new IllegalStateException("Tried to get id for unregistered particle component");
        }
        return this.byValue.get(value);
    }

    public record ComponentReference<T>(Identifier id, ParticleComponent<T> value) {
    }
}
