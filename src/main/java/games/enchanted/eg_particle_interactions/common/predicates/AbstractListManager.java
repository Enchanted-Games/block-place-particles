package games.enchanted.eg_particle_interactions.common.predicates;


import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.registry.ObjectOrTagLocation;
import games.enchanted.eg_particle_interactions.common.util.ExceptionReporter;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.Reader;
import java.util.*;

public abstract class AbstractListManager<F extends ObjectListFile, T extends ObjectList<F>> extends SimplePreparableReloadListener<AbstractListManager.Preparation<F>> {
    private final Map<Identifier, T> listById = new HashMap<>();
    private final FileToIdConverter fileToIdConverter;
    private final String typeName;
    private final ExceptionReporter exceptionReporter;

    public AbstractListManager(FileToIdConverter fileToIdConverter, String typeName) {
        this.fileToIdConverter = fileToIdConverter;
        this.typeName = typeName;
        this.exceptionReporter = new ExceptionReporter(typeName.substring(0, 1).toUpperCase(Locale.ROOT) + typeName.substring(1) + " Lists");
    }

    @Override
    protected Preparation<F> prepare(ResourceManager manager, ProfilerFiller profiler) {
        Map<Identifier, List<F>> biomeListFiles = new HashMap<>();

        for (Map.Entry<Identifier, List<Resource>> listResources : this.fileToIdConverter.listMatchingResourceStacks(manager).entrySet()) {
            Identifier fileId = listResources.getKey();
            Identifier listId = this.fileToIdConverter.fileToId(fileId);

            List<F> parsedFiles = new ArrayList<>();
            this.parseListFiles(fileId, listResources.getValue(), parsedFiles);

            biomeListFiles.put(listId, parsedFiles);
        }

        return new Preparation<>(biomeListFiles);
    }

    protected void parseListFiles(Identifier fileId, List<Resource> resources, List<F> output) {
        for (Resource resource : resources) {
            try (Reader reader = resource.openAsReader()) {
                JsonElement json = StrictJsonParser.parse(reader);
                output.add(this.fileCodec().parse(JsonOps.INSTANCE, json).getOrThrow(JsonSyntaxException::new));
            } catch (Exception e) {
                this.exceptionReporter.consumeException(fileId, e);
            }
        }
    }

    protected abstract Codec<F> fileCodec();

    protected abstract T listMaker(List<ObjectOrTagLocation> objectOrTagLocations);

    protected abstract T combineFiles(List<F> files);

    @Override
    protected void apply(Preparation<F> preparations, ResourceManager manager, ProfilerFiller profiler) {
        listById.clear();

        for (Map.Entry<Identifier, List<F>> entry : preparations.listFiles().entrySet()) {
            T combined = this.combineFiles(entry.getValue());
            listById.put(entry.getKey(), combined);
        }

        this.exceptionReporter.logExceptions();
    }

    public T getOrDefault(Identifier id) {
        if(!this.listById.containsKey(id)) {
            Logging.warn("{} list with id '{}' was not found", this.typeName, id);
            return this.listMaker(List.of());
        }
        return this.listById.get(id);
    }

    protected record Preparation<F extends ObjectListFile>(Map<Identifier, List<F>> listFiles) {
    }
}
