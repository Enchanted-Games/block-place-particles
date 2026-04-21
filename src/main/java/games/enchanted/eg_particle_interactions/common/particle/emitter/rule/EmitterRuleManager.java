package games.enchanted.eg_particle_interactions.common.particle.emitter.rule;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
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

public class EmitterRuleManager extends SimplePreparableReloadListener<EmitterRuleManager.Preparation> {
    public static final EmitterRuleManager INSTANCE = new EmitterRuleManager();

    private static final HashMap<Identifier, EmitterRule> RULE_BY_ID = new HashMap<>();
    private static final FileToIdConverter RULE_ID_CONVERTER = FileToIdConverter.json(Constants.MOD_ID + "/emitter_rules");

    @Override
    protected Preparation prepare(ResourceManager manager, ProfilerFiller profiler) {
        Map<Identifier, List<EmitterRule.File>> ruleFiles = new HashMap<>();

        for (Map.Entry<Identifier, List<Resource>> ruleResources : RULE_ID_CONVERTER.listMatchingResourceStacks(manager).entrySet()) {
            Identifier fileId = ruleResources.getKey();
            Identifier ruleId = RULE_ID_CONVERTER.fileToId(fileId);

            List<EmitterRule.File> parsedFiles = new ArrayList<>();
            parseRuleFile(fileId, ruleResources.getValue(), parsedFiles);

            ruleFiles.put(ruleId, parsedFiles);
        }

        return new Preparation(ruleFiles);
    }

    protected static void parseRuleFile(Identifier fileId, List<Resource> resources, List<EmitterRule.File> output) {
        for (Resource resource : resources) {
            try (Reader reader = resource.openAsReader()) {
                JsonElement json = StrictJsonParser.parse(reader);
                output.add(EmitterRule.File.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(JsonSyntaxException::new));
            } catch (Exception e) {
                Logging.error("Failed to parse emitter rule '{}'", fileId, e);
            }
        }
    }

    @Override
    protected void apply(Preparation preparations, ResourceManager manager, ProfilerFiller profiler) {
        RULE_BY_ID.clear();
        for (Map.Entry<Identifier, List<EmitterRule.File>> ruleFiles : preparations.filesById().entrySet()) {
            RULE_BY_ID.put(ruleFiles.getKey(), EmitterRule.combineFiles(ruleFiles.getValue()));
        }
    }

    public static EmitterRule getRuleById(Identifier ruleId) {
        if(RULE_BY_ID.containsKey(ruleId)) {
            return RULE_BY_ID.get(ruleId);
        }
        Logging.warn("Tried to get non-existent emitter rule! {}", ruleId);
        return EmitterRule.EMPTY;
    }

    protected record Preparation(Map<Identifier, List<EmitterRule.File>> filesById) {
    }
}
