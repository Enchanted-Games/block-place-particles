package games.enchanted.eg_particle_interactions.common.util;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public abstract class ObjectReference<T> {
    private final Identifier id;
    @Nullable T object = null;

    public ObjectReference(Identifier id) {
        this.id = id;
    }

    /**
     * Lookup the object for this references' id. See {@link  ObjectReference#id()}
     *
     * @return the object, or a suitable fallback if one does not exist. Should log a warning if a fallback was returned
     */
    protected abstract T lookupObject();

    public T get() {
        if(this.object == null) {
            this.object = this.lookupObject();
        }
        return this.object;
    }

    /**
     * Identifier to the object this reference is pointing to
     *
     * @return the identifier
     */
    public Identifier id() {
        return this.id;
    }
}
