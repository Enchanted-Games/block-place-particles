package games.enchanted.eg_particle_interactions.common.config2.option.enums;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.config2.option.ConfigOption;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Nullable;

public abstract class EnumOption<T extends Enum<T> & StringRepresentable> extends ConfigOption<T> {
    protected EnumOption(T initialAndDefaultValue, String jsonKey) {
        super(initialAndDefaultValue, jsonKey);
    }

    @Override
    public @Nullable JsonElement toJson() {
        DataResult<JsonElement> encodedResult = getCodec().encodeStart(JsonOps.INSTANCE, this.getValue());
        if(encodedResult.isError()) {
            Logging.error("Failed to encode config option {}", this.getJsonKey(), encodedResult.error().orElseThrow().message());
        }
        return encodedResult.getOrThrow();
    }

    @Override
    public void fromJson(JsonObject json) {
        JsonElement jsonElement = json.getAsJsonPrimitive(this.getJsonKey());
        DataResult<T> parsedResult = getCodec().parse(JsonOps.INSTANCE, jsonElement);
        if(parsedResult.isError()) {
            this.resetToDefault(false);
            return;
        }
        T result = parsedResult.getOrThrow();
        this.setPendingValue(result);
    }

     protected abstract StringRepresentable.EnumCodec<T> getCodec();
}
