package games.enchanted.eg_particle_interactions.common.particle.emitter.rule;

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

public class EmitterRuleSetManager extends SimplePreparableReloadListener<EmitterRuleSetManager.Preparation> {
    public static final EmitterRuleSetManager INSTANCE = new EmitterRuleSetManager();

    public static final Codec<EmitterRuleSet> INLINE_OR_ID_CODEC = EmitterRuleSet.CODEC.withAlternative(
        ModCodecs.IDENTIFIER.xmap(
            EmitterRuleSetManager::getRuleSetOrThrow,
            emitterRuleSet -> {
                throw new IllegalStateException("Cannot serialise emitter rule set to id");
            }
        )
    );

    private static final HashMap<Identifier, EmitterRuleSet> RULE_SET_BY_ID = new HashMap<>();
    private static final FileToIdConverter RULE_ID_CONVERTER = FileToIdConverter.json(Constants.MOD_ID + "/emitter_rules");
    private static final List<Identifier> MISSING_LOGGED = new ArrayList<>();

    @Override
    protected Preparation prepare(ResourceManager manager, ProfilerFiller profiler) {
        Map<Identifier, List<EmitterRuleSet.File>> ruleFiles = new HashMap<>();

        for (Map.Entry<Identifier, List<Resource>> ruleResources : RULE_ID_CONVERTER.listMatchingResourceStacks(manager).entrySet()) {
            Identifier fileId = ruleResources.getKey();
            Identifier ruleId = RULE_ID_CONVERTER.fileToId(fileId);

            List<EmitterRuleSet.File> parsedFiles = new ArrayList<>();
            parseRuleFile(fileId, ruleResources.getValue(), parsedFiles);

            ruleFiles.put(ruleId, parsedFiles);
        }

        return new Preparation(ruleFiles);
    }

    protected static void parseRuleFile(Identifier fileId, List<Resource> resources, List<EmitterRuleSet.File> output) {
        for (Resource resource : resources) {
            try (Reader reader = resource.openAsReader()) {
                JsonElement json = StrictJsonParser.parse(reader);
                output.add(EmitterRuleSet.File.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(JsonSyntaxException::new));
            } catch (Exception e) {
                Logging.error("Failed to parse emitter rule '{}'", fileId, e);
            }
        }
    }

    @Override
    protected void apply(Preparation preparations, ResourceManager manager, ProfilerFiller profiler) {
        RULE_SET_BY_ID.clear();
        for (Map.Entry<Identifier, List<EmitterRuleSet.File>> ruleFiles : preparations.filesById().entrySet()) {
            RULE_SET_BY_ID.put(ruleFiles.getKey(), EmitterRuleSet.combineFiles(ruleFiles.getValue()));
        }
        MISSING_LOGGED.clear();
    }

    public static EmitterRuleSet getRuleSet(Identifier ruleId) {
        if(RULE_SET_BY_ID.containsKey(ruleId)) {
            return RULE_SET_BY_ID.get(ruleId);
        }
        if(!MISSING_LOGGED.contains(ruleId)) {
            Logging.warn("Unknown emitter rule '{}'", ruleId);
            MISSING_LOGGED.add(ruleId);
        }
        return EmitterRuleSet.EMPTY;
    }

    public static EmitterRuleSet getRuleSetOrThrow(Identifier ruleId) {
        if(RULE_SET_BY_ID.containsKey(ruleId)) {
            return RULE_SET_BY_ID.get(ruleId);
        }
        throw new IllegalArgumentException("Tried to get non-existent emitter rule '" + ruleId + "'!");
    }

    protected record Preparation(Map<Identifier, List<EmitterRuleSet.File>> filesById) {
    }
}
