package games.enchanted.blockplaceparticles.config.adapters;

import com.google.gson.*;
import games.enchanted.blockplaceparticles.registry.BlockOrTagLocation;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Type;

public class BlockLocationTypeAdapter implements JsonSerializer<BlockOrTagLocation>, JsonDeserializer<BlockOrTagLocation> {
    @Override
    public BlockOrTagLocation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String rawString = jsonElement.getAsString();
        ResourceLocation parsedLocation = ResourceLocation.parse(rawString.replace("#", ""));
        return new BlockOrTagLocation(parsedLocation, rawString.startsWith("#"));
    }

    @Override
    public JsonElement serialize(BlockOrTagLocation resourceLocation, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(resourceLocation.toString());
    }
}
