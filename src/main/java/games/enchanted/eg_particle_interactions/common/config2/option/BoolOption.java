package games.enchanted.eg_particle_interactions.common.config2.option;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.Nullable;

public class BoolOption extends ConfigOption<Boolean> {
    public BoolOption(Boolean initialAndDefaultValue, String jsonKey) {
        super(initialAndDefaultValue, jsonKey);
    }

    @Override
    public @Nullable JsonElement toJson() {
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonObject json) {
        Boolean value = json.has(getJsonKey()) ? json.get(getJsonKey()).getAsBoolean() : getDefaultValue();
        this.setValueOrPending(value);
    }
}
