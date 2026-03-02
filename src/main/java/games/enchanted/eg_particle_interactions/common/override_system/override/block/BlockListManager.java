package games.enchanted.eg_particle_interactions.common.override_system.override.block;

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
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockListManager {
    private static final BiMap<Identifier, BlockList> LIST_BY_ID = HashBiMap.create();
    private static final FileToIdConverter FILE_TO_ID_CONVERTER = FileToIdConverter.json(Constants.MOD_ID + "/block_lists");

    public static final Codec<BlockList> INLINE_OR_ID_CODEC = BlockList.CODEC.withAlternative(
        ModCodecs.IDENTIFIER.xmap(
            BlockListManager::getOrDefault,
            BlockListManager::getIdOrThrow
        )
    );

    public static final BlockListManager INSTANCE = new BlockListManager();

    Preparation prepare(ResourceManager manager, ProfilerFiller profiler) {
        Map<Identifier, List<BlockList.File>> blockListFiles = new HashMap<>();

        for (Map.Entry<Identifier, List<Resource>> ruleFiles : FILE_TO_ID_CONVERTER.listMatchingResourceStacks(manager).entrySet()) {
            Identifier fileId = ruleFiles.getKey();
            Identifier overrideId = FILE_TO_ID_CONVERTER.fileToId(fileId);

            List<BlockList.File> parsedFiles = new ArrayList<>();
            this.parseListFiles(fileId, ruleFiles.getValue(), parsedFiles);

            blockListFiles.put(overrideId, parsedFiles);
        }

        return new Preparation(blockListFiles);
    }

    protected void parseListFiles(Identifier fileId, List<Resource> resources, List<BlockList.File> output) {
        for (Resource resource : resources) {
            try (Reader reader = resource.openAsReader()) {
                JsonElement json = StrictJsonParser.parse(reader);
                output.add(BlockList.File.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(JsonSyntaxException::new));
            } catch (Exception e) {
                Logging.error("Failed to parse override rule '{}'", fileId, e);
            }
        }
    }

    void apply(Preparation preparations) {
        LIST_BY_ID.clear();

        for (Map.Entry<Identifier, List<BlockList.File>> entry : preparations.blockLists().entrySet()) {
            BlockList combined = BlockList.File.combine(entry.getValue());
            LIST_BY_ID.put(entry.getKey(), combined);
        }
    }

    public static BlockList getOrDefault(Identifier id) {
        if(!LIST_BY_ID.containsKey(id)) {
            Logging.warn("Block list with id '{}' was not found", id);
            return new BlockList(List.of());
        }
        return LIST_BY_ID.get(id);
    }

    public static Identifier getIdOrThrow(BlockList list) {
        if(!LIST_BY_ID.inverse().containsKey(list)) {
            throw new RuntimeException("Block list id not found");
        }
        return LIST_BY_ID.inverse().get(list);
    }

    protected record Preparation(Map<Identifier, List<BlockList.File>> blockLists) {
    }
}
