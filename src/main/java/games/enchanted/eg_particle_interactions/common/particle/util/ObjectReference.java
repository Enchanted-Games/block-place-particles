package games.enchanted.eg_particle_interactions.common.particle.util;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public abstract class ObjectReference<T> {
    private final Identifier id;
    @Nullable T object = null;

    public ObjectReference(Identifier id) {
        this.id = id;
    }

    protected abstract T lookupObject();

    public T get() {
        if(this.object == null) {
            this.object = this.lookupObject();
        }
        return this.object;
    }

    public Identifier id() {
        return this.id;
    }
}
