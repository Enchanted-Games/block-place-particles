package games.enchanted.eg_particle_interactions.common.override_system.override;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class ParticleOverrides extends SimplePreparableReloadListener<ParticleOverrides.Preparation> {
    public static final Identifier VANILLA_OVERRIDE_ID = ParticleInteractionsMod.id("vanilla");
    public static final Identifier EMPTY_OVERRIDE_ID = ParticleInteractionsMod.id("empty");

    private static final BiMap<Identifier, ParticleOverride> OVERRIDE_BY_ID = HashBiMap.create();

    private static final FileToIdConverter OVERRIDE_ID_CONVERTER = FileToIdConverter.json(Constants.MOD_ID + "/particle_overrides");

    public static final ParticleOverrides INSTANCE = new ParticleOverrides();

    @Override
    protected Preparation prepare(ResourceManager manager, ProfilerFiller profiler) {
        Map<Identifier, ParticleOverride> overrideList = new HashMap<>();

        for (Map.Entry<Identifier, Resource> overrideResource : OVERRIDE_ID_CONVERTER.listMatchingResources(manager).entrySet()) {
            Identifier overrideId = overrideResource.getKey();
            parseOverride(overrideId, overrideResource.getValue(), overrideList);
        }

        return new Preparation(overrideList);
    }

    protected static void parseOverride(Identifier fileId, Resource resource, Map<Identifier, ParticleOverride> output) {
        try (Reader reader = resource.openAsReader()) {
            JsonElement json = StrictJsonParser.parse(reader);
            output.put(OVERRIDE_ID_CONVERTER.fileToId(fileId), ParticleOverride.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(JsonSyntaxException::new));
        } catch (JsonParseException | IOException e) {
            Logging.error("Failed to parse particle override '{}'", fileId, e);
        }
    }

    @Override
    protected void apply(Preparation preparations, ResourceManager manager, ProfilerFiller profiler) {
        clearOverrides();
        Map<Identifier, ParticleOverride> preparedOverrides = preparations.overrideList();
        for (Map.Entry<Identifier, ParticleOverride> overrideEntry : preparedOverrides.entrySet()) {
            registerOverride(overrideEntry.getKey(), overrideEntry.getValue());
        }
    }

    static void registerOverride(Identifier id, ParticleOverride override) {
        OVERRIDE_BY_ID.put(id, override);
    }

    public static ParticleOverride getOverrideFromId(Identifier id) {
        ParticleOverride override = OVERRIDE_BY_ID.get(id);
        if(override == null) {
            throw new IllegalStateException("Tried to get non-existent particle override '" + id + "'");
        }
        return override;
    }

    public static Identifier getIdFromOverride(ParticleOverride override) {
        Identifier id = OVERRIDE_BY_ID.inverse().get(override);
        if(id == null) {
            throw new IllegalStateException("Tried to get id for unregistered particle override '" + override + "'");
        }
        return id;
    }

    static void clearOverrides() {
        OVERRIDE_BY_ID.clear();
    }

    protected record Preparation(Map<Identifier, ParticleOverride> overrideList) {
    }
}
