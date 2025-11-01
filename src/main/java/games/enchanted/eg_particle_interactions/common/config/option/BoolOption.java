package games.enchanted.eg_particle_interactions.common.config.option;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.Nullable;

public class BoolOption extends ConfigOption<Boolean> {
    private boolean serializable = true;

    public BoolOption(Boolean initialAndDefaultValue, String jsonKey) {
        super(initialAndDefaultValue, jsonKey);
    }
    public BoolOption(Boolean initialAndDefaultValue, boolean serializable) {
        super(initialAndDefaultValue, null);
        this.serializable = serializable;
    }

    @Override
    public @Nullable JsonElement toJson() {
        if(!serializable) return null;
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonObject json) {
        Boolean value = json.has(getJsonKey()) ? json.get(getJsonKey()).getAsBoolean() : getDefaultValue();
        this.setValueOrPending(value);
    }
}
