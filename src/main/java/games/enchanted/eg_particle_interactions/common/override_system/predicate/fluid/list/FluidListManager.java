package games.enchanted.eg_particle_interactions.common.override_system.predicate.fluid.list;

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

public class FluidListManager {
    private static final BiMap<Identifier, FluidList> LIST_BY_ID = HashBiMap.create();
    private static final FileToIdConverter FILE_TO_ID_CONVERTER = FileToIdConverter.json(Constants.MOD_ID + "/lists/fluids");

    public static final Codec<FluidList> INLINE_OR_ID_CODEC = FluidList.CODEC.withAlternative(
        ModCodecs.IDENTIFIER.xmap(
            FluidListManager::getOrDefault,
            FluidListManager::getIdOrThrow
        )
    );

    public static final FluidListManager INSTANCE = new FluidListManager();

    public void prepareAndApply(ResourceManager manager, ProfilerFiller profiler) {
        Map<Identifier, List<FluidList.File>> FluidListFiles = new HashMap<>();

        for (Map.Entry<Identifier, List<Resource>> ruleFiles : FILE_TO_ID_CONVERTER.listMatchingResourceStacks(manager).entrySet()) {
            Identifier fileId = ruleFiles.getKey();
            Identifier overrideId = FILE_TO_ID_CONVERTER.fileToId(fileId);

            List<FluidList.File> parsedFiles = new ArrayList<>();
            this.parseListFiles(fileId, ruleFiles.getValue(), parsedFiles);

            FluidListFiles.put(overrideId, parsedFiles);
        }

        LIST_BY_ID.clear();

        for (Map.Entry<Identifier, List<FluidList.File>> entry : FluidListFiles.entrySet()) {
            FluidList combined = FluidList.File.combine(entry.getValue());
            LIST_BY_ID.put(entry.getKey(), combined);
        }
    }

    protected void parseListFiles(Identifier fileId, List<Resource> resources, List<FluidList.File> output) {
        for (Resource resource : resources) {
            try (Reader reader = resource.openAsReader()) {
                JsonElement json = StrictJsonParser.parse(reader);
                output.add(FluidList.File.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(JsonSyntaxException::new));
            } catch (Exception e) {
                Logging.error("Failed to parse fluid list '{}', {}", fileId, e);
            }
        }
    }

    public static FluidList getOrDefault(Identifier id) {
        if(!LIST_BY_ID.containsKey(id)) {
            Logging.warn("Fluid list with id '{}' was not found", id);
            return new FluidList(List.of(), List.of());
        }
        return LIST_BY_ID.get(id);
    }

    public static Identifier getIdOrThrow(FluidList list) {
        if(!LIST_BY_ID.inverse().containsKey(list)) {
            throw new RuntimeException("Block list id not found");
        }
        return LIST_BY_ID.inverse().get(list);
    }
}
