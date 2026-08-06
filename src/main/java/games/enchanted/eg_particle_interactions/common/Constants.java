package games.enchanted.eg_particle_interactions.common;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;
import games.enchanted.eg_particle_interactions.common.resource.version.PackVersion;

import java.io.StringReader;

public class Constants {
    public static final String MOD_NAME = "Particle Interactions";
    public static final String MOD_ID = "eg_particle_interactions";

    public static final String FABRIC_RESOURCE_LOADER_ID = "fabric-resource-loader-v1";
    public static final String MOD_MENU_ID = "modmenu";

    public static final PackVersion CURRENT_PACK_VERSION = readPackVersion();

    public static final String TARGET_PLATFORM =
    //? if fabric {
        "fabric";
    //?}
    //? if neoforge {
        /*"neoforge";
    *///?}

    private static PackVersion readPackVersion() {
        String packVersionPath = "pack_version.json";
        PackVersion version = null;

        try {
            JsonReader jsonReader = new Gson().newJsonReader(
                new StringReader(new String(PlatformHelper.readFileFromJar(packVersionPath)))
            );
            jsonReader.setStrictness(Strictness.LENIENT);
            JsonArray decoded = JsonParser.parseReader(jsonReader).getAsJsonArray();
            DataResult<Pair<PackVersion, JsonElement>> versionDataResult = PackVersion.LIST_CODEC.decode(JsonOps.INSTANCE, decoded);

            if(versionDataResult.isError() || versionDataResult.result().isEmpty()) {
                Logging.error("Could not decode current pack version. {}", versionDataResult.error());
            } else {
                version = versionDataResult.result().get().getFirst();
            }
        } catch (JsonParseException e) {
            Logging.error("Failed to parse current pack version. {}", e);
        } catch (Exception e) {
            Logging.error("Exception occurred while reading pack_version.json. {}", e);
        }

        if(version == null) {
            version = PackVersion.UNSPECIFIED;
        }

        Logging.info("Pack version is: {}", version);

        return version;
    }
}
