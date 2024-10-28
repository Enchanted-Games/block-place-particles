package games.enchanted.blockplaceparticles.config.adapters;

import com.google.gson.*;
import games.enchanted.blockplaceparticles.util.RegistryHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.lang.reflect.Type;

public class FluidTypeAdapter implements JsonSerializer<Fluid>, JsonDeserializer<Fluid> {
    @Override
    public Fluid deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return RegistryHelper.getDefaultedFluid(jsonElement.getAsString(), Fluids.EMPTY);
    }

    @Override
    public JsonElement serialize(Fluid fluid, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(RegistryHelper.getLocationFromFluid(fluid).toString());
    }
}
