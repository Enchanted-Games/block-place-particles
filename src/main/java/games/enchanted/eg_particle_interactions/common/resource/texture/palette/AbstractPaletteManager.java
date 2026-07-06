package games.enchanted.eg_particle_interactions.common.resource.texture.palette;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.predicates.ObjectPredicate;
import games.enchanted.eg_particle_interactions.common.util.ExceptionReporter;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jspecify.annotations.Nullable;

import java.io.Reader;
import java.util.*;

public abstract class AbstractPaletteManager<T, P extends ObjectPredicate<T>> extends SimplePreparableReloadListener<AbstractPaletteManager.Preparation<T, P>> {
    final FileToIdConverter fileToIdConverter;
    final Codec<PaletteDefinition.File<T, P>> fileCodec;
    final String typeName;
    final ExceptionReporter exceptionReporter;

    final Map<Identifier, PaletteDefinition<T, P>> idToPalette = new HashMap<>();

    public AbstractPaletteManager(FileToIdConverter fileToIdConverter, Codec<PaletteDefinition.File<T, P>> fileCodec, String typeName) {
        this.fileToIdConverter = fileToIdConverter;
        this.fileCodec = fileCodec;
        this.typeName = typeName;
        this.exceptionReporter = new ExceptionReporter(typeName.substring(0, 1).toUpperCase(Locale.ROOT) + typeName.substring(1) + " Palettes");
    }

    @Override
    protected Preparation<T, P> prepare(ResourceManager manager, ProfilerFiller profiler) {
        Map<Identifier, List<PaletteDefinition.File<T, P>>> idToFiles = new HashMap<>();

        for (Map.Entry<Identifier, List<Resource>> listResources : this.fileToIdConverter.listMatchingResourceStacks(manager).entrySet()) {
            Identifier fileId = listResources.getKey();
            Identifier objectId = this.fileToIdConverter.fileToId(fileId);

            List<PaletteDefinition.File<T, P>> parsedFiles = new ArrayList<>();
            this.parseFiles(fileId, listResources.getValue(), parsedFiles);
            if(parsedFiles.isEmpty()) continue;

            idToFiles.put(objectId, parsedFiles);
        }

        return new Preparation<>(idToFiles);
    }

    protected void parseFiles(Identifier fileId, List<Resource> resources, List<PaletteDefinition.File<T, P>> output) {
        for (Resource resource : resources) {
            try (Reader reader = resource.openAsReader()) {
                JsonElement json = StrictJsonParser.parse(reader);
                output.add(this.fileCodec.parse(JsonOps.INSTANCE, json).getOrThrow(JsonSyntaxException::new));
            } catch (Exception e) {
                this.exceptionReporter.consumeException(fileId, e);
            }
        }
    }

    @Override
    protected void apply(Preparation<T, P> preparations, ResourceManager manager, ProfilerFiller profiler) {
        this.idToPalette.clear();
        for (Map.Entry<Identifier, List<PaletteDefinition.File<T, P>>> entry : preparations.idToFiles().entrySet()) {
            this.idToPalette.put(entry.getKey(), PaletteDefinition.combineFiles(entry.getValue()));
        }
        this.exceptionReporter.logExceptions();
    }

    public @Nullable PaletteDefinition<T, P> getOrNull(T object) {
        Identifier id = this.lookupId(object);
        if(this.idToPalette.containsKey(id)) {
            return this.idToPalette.get(id);
        }
        return null;
    }

    protected abstract Identifier lookupId(T object);

    protected record Preparation<T, P extends ObjectPredicate<T>>(Map<Identifier, List<PaletteDefinition.File<T, P>>> idToFiles) {
    }
}
