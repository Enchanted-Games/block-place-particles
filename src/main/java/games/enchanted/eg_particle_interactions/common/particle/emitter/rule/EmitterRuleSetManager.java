package games.enchanted.eg_particle_interactions.common.particle.emitter.rule;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.util.ExceptionReporter;
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

public class EmitterRuleSetManager extends SimplePreparableReloadListener<EmitterRuleSetManager.Preparation> {
    public static final EmitterRuleSetManager INSTANCE = new EmitterRuleSetManager();

    public static final Codec<EmitterRuleSet.Reference> INLINE_OR_REFERENCE_CODEC = EmitterRuleSet.INLINE_REFERENCE_CODEC.withAlternative(
        ModCodecs.IDENTIFIER.xmap(
            EmitterRuleSet.Reference::new,
            emitterRuleSet -> {
                throw new IllegalStateException("Cannot serialise emitter rule set to id");
            }
        )
    );

    private final HashMap<Identifier, EmitterRuleSet> ruleSetById = new HashMap<>();
    private final FileToIdConverter fileToIdConverter = FileToIdConverter.json(Constants.MOD_ID + "/emitter_rules");
    private final List<Identifier> missingLogged = new ArrayList<>();
    private final ExceptionReporter exceptionReporter = new ExceptionReporter("Emitter Rules");

    @Override
    protected Preparation prepare(ResourceManager manager, ProfilerFiller profiler) {
        Map<Identifier, List<EmitterRuleSet.File>> ruleFiles = new HashMap<>();

        for (Map.Entry<Identifier, List<Resource>> ruleResources : fileToIdConverter.listMatchingResourceStacks(manager).entrySet()) {
            Identifier fileId = ruleResources.getKey();
            Identifier ruleId = fileToIdConverter.fileToId(fileId);

            List<EmitterRuleSet.File> parsedFiles = new ArrayList<>();
            this.parseRuleFile(fileId, ruleResources.getValue(), parsedFiles);

            ruleFiles.put(ruleId, parsedFiles);
        }

        return new Preparation(ruleFiles);
    }

    protected void parseRuleFile(Identifier fileId, List<Resource> resources, List<EmitterRuleSet.File> output) {
        for (Resource resource : resources) {
            try (Reader reader = resource.openAsReader()) {
                JsonElement json = StrictJsonParser.parse(reader);
                output.add(EmitterRuleSet.File.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(JsonSyntaxException::new));
            } catch (Exception e) {
                this.exceptionReporter.consumeException(fileId, e);
            }
        }
    }

    @Override
    protected void apply(Preparation preparations, ResourceManager manager, ProfilerFiller profiler) {
        this.ruleSetById.clear();
        for (Map.Entry<Identifier, List<EmitterRuleSet.File>> ruleFiles : preparations.filesById().entrySet()) {
            this.ruleSetById.put(ruleFiles.getKey(), EmitterRuleSet.combineFiles(ruleFiles.getValue()));
        }
        this.missingLogged.clear();

        this.exceptionReporter.logExceptions();
    }

    public EmitterRuleSet getById(Identifier ruleId) {
        if(this.ruleSetById.containsKey(ruleId)) {
            return this.ruleSetById.get(ruleId);
        }
        if(!this.missingLogged.contains(ruleId)) {
            Logging.warn("Unknown emitter rule '{}'", ruleId);
            this.missingLogged.add(ruleId);
        }
        return EmitterRuleSet.EMPTY.get();
    }

    public EmitterRuleSet getByIdSetOrThrow(Identifier ruleId) {
        if(this.ruleSetById.containsKey(ruleId)) {
            return this.ruleSetById.get(ruleId);
        }
        throw new IllegalArgumentException("Tried to get non-existent emitter rule '" + ruleId + "'!");
    }

    public static EmitterRuleSet get(Identifier ruleId) {
        return INSTANCE.getById(ruleId);
    }

    public static EmitterRuleSet getOrThrow(Identifier ruleId) {
        return INSTANCE.getByIdSetOrThrow(ruleId);
    }

    protected record Preparation(Map<Identifier, List<EmitterRuleSet.File>> filesById) {
    }
}
