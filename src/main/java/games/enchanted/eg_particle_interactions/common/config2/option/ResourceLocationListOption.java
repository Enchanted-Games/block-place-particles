package games.enchanted.eg_particle_interactions.common.config2.option;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.registry.BlockOrTagLocation;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ResourceLocationListOption extends ConfigOption<List<ResourceLocation>> {
    public static final Codec<List<ResourceLocation>> CODEC = ResourceLocation.CODEC.listOf();

    public ResourceLocationListOption(List<ResourceLocation> initialAndDefaultValue, String jsonKey) {
        super(initialAndDefaultValue, jsonKey);
    }

    @Override
    public @Nullable JsonElement toJson() {
        DataResult<JsonElement> encodedResult = CODEC.encodeStart(JsonOps.INSTANCE, this.getValue());
        if(encodedResult.isError()) {
            Logging.error("Failed to encode config option {}", this.getJsonKey(), encodedResult.error().orElseThrow().message());
            return null;
        }
        return encodedResult.getOrThrow();
    }

    @Override
    public void fromJson(JsonObject json) {
        JsonElement jsonElement = json.getAsJsonArray(this.getJsonKey());
        DataResult<List<ResourceLocation>> parsedResult = CODEC.parse(JsonOps.INSTANCE, jsonElement);
        if(parsedResult.isError()) {
            this.resetToDefault(false);
            return;
        }
        this.setPendingValue(parsedResult.getOrThrow());
    }
}
