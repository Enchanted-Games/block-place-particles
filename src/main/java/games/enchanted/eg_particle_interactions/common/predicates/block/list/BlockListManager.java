package games.enchanted.eg_particle_interactions.common.predicates.block.list;

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
    private static final Map<Identifier, BlockList> LIST_BY_ID = new HashMap<>();
    private static final FileToIdConverter FILE_TO_ID_CONVERTER = FileToIdConverter.json(Constants.MOD_ID + "/lists/blocks");

    public static final Codec<BlockList> INLINE_OR_ID_CODEC = BlockList.CODEC.withAlternative(
        ModCodecs.IDENTIFIER.xmap(
            BlockListManager::getOrDefault,
            blockList -> {
                throw new IllegalStateException("Cannot serialise block list to id");
            }
        )
    );

    public static final BlockListManager INSTANCE = new BlockListManager();

    public void prepareAndApply(ResourceManager manager, ProfilerFiller profiler) {
        Map<Identifier, List<BlockList.File>> blockListFiles = new HashMap<>();

        for (Map.Entry<Identifier, List<Resource>> listResources : FILE_TO_ID_CONVERTER.listMatchingResourceStacks(manager).entrySet()) {
            Identifier fileId = listResources.getKey();
            Identifier listId = FILE_TO_ID_CONVERTER.fileToId(fileId);

            List<BlockList.File> parsedFiles = new ArrayList<>();
            this.parseListFiles(fileId, listResources.getValue(), parsedFiles);

            blockListFiles.put(listId, parsedFiles);
        }

        LIST_BY_ID.clear();

        for (Map.Entry<Identifier, List<BlockList.File>> entry : blockListFiles.entrySet()) {
            BlockList combined = BlockList.File.combine(entry.getValue());
            LIST_BY_ID.put(entry.getKey(), combined);
        }
    }

    protected void parseListFiles(Identifier fileId, List<Resource> resources, List<BlockList.File> output) {
        for (Resource resource : resources) {
            try (Reader reader = resource.openAsReader()) {
                JsonElement json = StrictJsonParser.parse(reader);
                output.add(BlockList.File.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(JsonSyntaxException::new));
            } catch (Exception e) {
                Logging.error("Failed to parse block list '{}', {}", fileId, e);
            }
        }
    }

    public static BlockList getOrDefault(Identifier id) {
        if(!LIST_BY_ID.containsKey(id)) {
            Logging.warn("Block list with id '{}' was not found", id);
            return new BlockList(List.of(), List.of());
        }
        return LIST_BY_ID.get(id);
    }
}
