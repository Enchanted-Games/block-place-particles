package games.enchanted.eg_particle_interactions.common.config2.option;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.Nullable;

public class IntOption extends ConfigOption<Integer> {
    public IntOption(Integer initialAndDefaultValue, String jsonKey) {
        super(initialAndDefaultValue, jsonKey);
    }

    @Override
    public @Nullable JsonElement toJson() {
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonObject json) {
        Integer value = json.has(getJsonKey()) ? json.get(getJsonKey()).getAsInt() : getDefaultValue();
        this.setValueOrPending(value);
    }
}
