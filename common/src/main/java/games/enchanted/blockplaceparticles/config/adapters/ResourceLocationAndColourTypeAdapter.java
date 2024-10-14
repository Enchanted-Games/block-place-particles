package games.enchanted.blockplaceparticles.config.adapters;

import com.google.gson.*;
import games.enchanted.blockplaceparticles.config.type.ResourceLocationAndColour;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.lang.reflect.Type;

public class ResourceLocationAndColourTypeAdapter implements JsonSerializer<ResourceLocationAndColour>, JsonDeserializer<ResourceLocationAndColour> {
    @Override
    public ResourceLocationAndColour deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        ResourceLocation location = jsonDeserializationContext.deserialize(object.get("resource_location"), ResourceLocation.class);
        Color color = jsonDeserializationContext.deserialize(object.get("color"), Color.class);
        return new ResourceLocationAndColour(location, color);
    }

    @Override
    public JsonElement serialize(ResourceLocationAndColour locationAndColour, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.add("resource_location", jsonSerializationContext.serialize(locationAndColour.location()));
        object.add("color", jsonSerializationContext.serialize(locationAndColour.colour()));
        return object;
    }
}
