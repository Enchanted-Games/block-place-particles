package games.enchanted.eg_particle_interactions.common.override_system.override.rule;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.override_system.preset.OverridePreset;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractOverrideRuleLoader<T> extends SimplePreparableReloadListener<AbstractOverrideRuleLoader.Preparation<T>> {
    private final List<OverrideRule<T>> OVERRIDE_RULES = new ArrayList<>();
    private final Map<T, OverridePreset> OBJECT_TO_OVERRIDE = new HashMap<>();

    private final FileToIdConverter fileToIdConverter;

    protected AbstractOverrideRuleLoader(FileToIdConverter fileToIdConverter) {
        this.fileToIdConverter = fileToIdConverter;
    }

    @Override
    protected Preparation<T> prepare(ResourceManager manager, ProfilerFiller profiler) {
        Map<Identifier, List<OverrideRuleFile<T>>> parsedOverrideRules = new HashMap<>();

        for (Map.Entry<Identifier, List<Resource>> ruleFiles : this.fileToIdConverter.listMatchingResourceStacks(manager).entrySet()) {
            Identifier fileId = ruleFiles.getKey();
            Identifier overrideId = this.fileToIdConverter.fileToId(fileId);

            List<OverrideRuleFile<T>> parsedRuleFiles = new ArrayList<>();
            this.parseRuleFiles(fileId, ruleFiles.getValue(), parsedRuleFiles);

            parsedOverrideRules.put(overrideId, parsedRuleFiles);
        }

        return new Preparation<>(parsedOverrideRules);
    }

    protected void parseRuleFiles(Identifier fileId, List<Resource> resources, List<OverrideRuleFile<T>> output) {
        for (Resource resource : resources) {
            try (Reader reader = resource.openAsReader()) {
                JsonElement json = StrictJsonParser.parse(reader);
                output.add(this.fileCodec().parse(JsonOps.INSTANCE, json).getOrThrow(JsonSyntaxException::new));
            } catch (JsonParseException | IOException e) {
                Logging.error("Failed to parse particle override '{}'", fileId, e);
            }
        }
    }

    @Override
    protected void apply(Preparation<T> preparation, ResourceManager manager, ProfilerFiller profiler) {
        clearOverrideRules();
        for (Map.Entry<Identifier, List<OverrideRuleFile<T>>> preparedRules : preparation.ruleFilesByOverrideId().entrySet()) {
            this.OVERRIDE_RULES.add(new OverrideRule<>(preparedRules.getValue(), preparedRules.getKey()));
        }
    }

    void clearOverrideRules() {
        this.OVERRIDE_RULES.clear();
        this.OBJECT_TO_OVERRIDE.clear();
    }

    protected abstract Codec<OverrideRuleFile<T>> fileCodec();


    public OverridePreset getOverrideFor(T state) {
        if(this.OBJECT_TO_OVERRIDE.containsKey(state)) {
            return this.OBJECT_TO_OVERRIDE.get(state);
        }
        OverridePreset preset = buildPresetForObject(state, this.OVERRIDE_RULES);
        this.OBJECT_TO_OVERRIDE.put(state, preset);
        return preset;
    }


    private OverridePreset buildPresetForObject(T object, List<OverrideRule<T>> rulesList) {
        List<OverridePreset.OverrideAndWeight> weights = new ArrayList<>();
        for (OverrideRule<T> overrideRule : rulesList) {
            OverridePreset.OverrideAndWeight overrideAndWeight = overrideRule.getOverrideWeightForObject(object);
            if(overrideAndWeight.weight() == 0) continue;
            weights.add(overrideAndWeight);
        }

        if(weights.isEmpty()) {
            return OverridePreset.DEFAULT;
        }

        return OverridePreset.getOrCreate(weights);
    }

    protected record Preparation<T>(Map<Identifier, List<OverrideRuleFile<T>>> ruleFilesByOverrideId) {
    }
}
