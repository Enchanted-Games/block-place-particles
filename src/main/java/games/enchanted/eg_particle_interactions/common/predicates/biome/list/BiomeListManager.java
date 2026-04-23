package games.enchanted.eg_particle_interactions.common.predicates.biome.list;

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

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiomeListManager extends SimplePreparableReloadListener<BiomeListManager.Preparation> {
    private static final Map<Identifier, BiomeList> LIST_BY_ID = new HashMap<>();
    private static final FileToIdConverter FILE_TO_ID_CONVERTER = FileToIdConverter.json(Constants.MOD_ID + "/lists/biomes");

    public static final Codec<BiomeList> INLINE_OR_ID_CODEC = BiomeList.CODEC.withAlternative(
        ModCodecs.IDENTIFIER.xmap(
            BiomeListManager::getOrDefault,
            biomeList -> {
                throw new IllegalStateException("Cannot serialise biome list to id");
            }
        )
    );

    public static final BiomeListManager INSTANCE = new BiomeListManager();

    @Override
    protected Preparation prepare(ResourceManager manager, ProfilerFiller profiler) {
        Map<Identifier, List<BiomeList.File>> biomeListFiles = new HashMap<>();

        for (Map.Entry<Identifier, List<Resource>> listResources : FILE_TO_ID_CONVERTER.listMatchingResourceStacks(manager).entrySet()) {
            Identifier fileId = listResources.getKey();
            Identifier listId = FILE_TO_ID_CONVERTER.fileToId(fileId);

            List<BiomeList.File> parsedFiles = new ArrayList<>();
            this.parseListFiles(fileId, listResources.getValue(), parsedFiles);

            biomeListFiles.put(listId, parsedFiles);
        }

        return new Preparation(biomeListFiles);
    }

    protected void parseListFiles(Identifier fileId, List<Resource> resources, List<BiomeList.File> output) {
        for (Resource resource : resources) {
            try (Reader reader = resource.openAsReader()) {
                JsonElement json = StrictJsonParser.parse(reader);
                output.add(BiomeList.File.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(JsonSyntaxException::new));
            } catch (Exception e) {
                Logging.error("Failed to parse biome list '{}', {}", fileId, e);
            }
        }
    }

    @Override
    protected void apply(Preparation preparations, ResourceManager manager, ProfilerFiller profiler) {
        LIST_BY_ID.clear();

        for (Map.Entry<Identifier, List<BiomeList.File>> entry : preparations.biomeListFiles().entrySet()) {
            BiomeList combined = BiomeList.File.combine(entry.getValue());
            LIST_BY_ID.put(entry.getKey(), combined);
        }
    }

    public static BiomeList getOrDefault(Identifier id) {
        if(!LIST_BY_ID.containsKey(id)) {
            Logging.warn("Biome list with id '{}' was not found", id);
            return new BiomeList(List.of());
        }
        return LIST_BY_ID.get(id);
    }

    protected record Preparation(Map<Identifier, List<BiomeList.File>> biomeListFiles) {
    }
}
