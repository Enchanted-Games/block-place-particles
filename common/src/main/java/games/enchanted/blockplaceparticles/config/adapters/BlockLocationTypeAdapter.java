package games.enchanted.blockplaceparticles.config.adapters;

import com.google.gson.*;
import games.enchanted.blockplaceparticles.registry.BlockLocation;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Type;

public class BlockLocationTypeAdapter implements JsonSerializer<BlockLocation>, JsonDeserializer<BlockLocation> {
    @Override
    public BlockLocation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String rawString = jsonElement.getAsString();
        ResourceLocation parsedLocation = ResourceLocation.parse(rawString.replace("#", ""));
        return new BlockLocation(parsedLocation, rawString.startsWith("#"));
    }

    @Override
    public JsonElement serialize(BlockLocation resourceLocation, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(resourceLocation.toString());
    }
}
