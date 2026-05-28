package games.enchanted.eg_particle_interactions.common.particle.definition;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jspecify.annotations.Nullable;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class ParticleDefinitionManager extends SimplePreparableReloadListener<ParticleDefinitionManager.Preparation> {
    public static final ParticleDefinitionManager INSTANCE = new ParticleDefinitionManager();

    public static final Codec<ParticleDefinition.Reference> REFERENCE_CODEC = ModCodecs.IDENTIFIER.xmap(
        ParticleDefinition.Reference::new,
        ParticleDefinition.Reference::id
    );

    private static final FileToIdConverter FILE_TO_ID_CONVERTER = FileToIdConverter.json(Constants.MOD_ID + "/particles");

    private final BiMap<Identifier, ParticleDefinition> definitionById = HashBiMap.create();

    @Override
    protected Preparation prepare(ResourceManager manager, ProfilerFiller profiler) {
        Map<Identifier, ParticleDefinition> preparedDefinitions = new HashMap<>();

        for (Map.Entry<Identifier, Resource> overrideResource : FILE_TO_ID_CONVERTER.listMatchingResources(manager).entrySet()) {
            Identifier fileId = overrideResource.getKey();
            parseDefinition(fileId, overrideResource.getValue(), preparedDefinitions);
        }

        return new Preparation(preparedDefinitions);
    }

    protected static void parseDefinition(Identifier fileId, Resource resource, Map<Identifier, ParticleDefinition> output) {
        try (Reader reader = resource.openAsReader()) {
            JsonElement json = StrictJsonParser.parse(reader);
            output.put(FILE_TO_ID_CONVERTER.fileToId(fileId), ParticleDefinition.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(JsonSyntaxException::new));
        } catch (Exception e) {
            Logging.error("Failed to parse particle definition '{}'", fileId, e);
        }
    }

    @Override
    protected void apply(Preparation preparations, ResourceManager manager, ProfilerFiller profiler) {
        this.clear();
        this.definitionById.putAll(preparations.preparedDefinitionsById);
    }

    private void clear() {
        this.definitionById.clear();
    }

    public ParticleDefinition getOrFallback(Identifier definitionId) {
        if(!(this.definitionById.containsKey(definitionId))) {
            return ParticleDefinition.FALLBACK;
        }
        return this.definitionById.get(definitionId);
    }

    public ParticleDefinition getOrThrow(Identifier definitionId) {
        if(!(this.definitionById.containsKey(definitionId))) {
            throw new IllegalArgumentException("Unknown particle definition '" + definitionId + "'");
        }
        return this.definitionById.get(definitionId);
    }

    public @Nullable Identifier getIdOrNull(ParticleDefinition definition) {
        if(!(this.definitionById.inverse().containsKey(definition))) {
            return null;
        }
        return this.definitionById.inverse().get(definition);
    }

    public Identifier getIdOrThrow(ParticleDefinition definition) {
        if(!(this.definitionById.inverse().containsKey(definition))) {
            throw new IllegalArgumentException("Tried to get id for unregistered particle definition '" + definition + "'");
        }
        return this.definitionById.inverse().get(definition);
    }

    protected record Preparation(Map<Identifier, ParticleDefinition> preparedDefinitionsById) {
    }
}
