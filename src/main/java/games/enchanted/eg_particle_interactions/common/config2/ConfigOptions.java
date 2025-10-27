package games.enchanted.eg_particle_interactions.common.config2;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.config.ConfigHandler;
import games.enchanted.eg_particle_interactions.common.config.type.BrushParticleBehaviour;
import games.enchanted.eg_particle_interactions.common.config2.option.BlockOrTagLocationListOption;
import games.enchanted.eg_particle_interactions.common.config2.option.BoolOption;
import games.enchanted.eg_particle_interactions.common.config2.option.ConfigOption;
import games.enchanted.eg_particle_interactions.common.config2.option.enums.BrushParticleBehaviourOption;
import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;
import games.enchanted.eg_particle_interactions.common.registry.BlockOrTagLocation;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigOptions {
    public static final ConfigOption<Boolean> TEST_BOOLEAN;
    public static final ConfigOption<BrushParticleBehaviour> TEST_BRUSH_BEHAVIOUR;
    public static final ConfigOption<List<BlockOrTagLocation>> TEST_LOCATIONS;

    private static final Map<ConfigCategory, List<ConfigOption<?>>> OPTIONS = new HashMap<>();

    static {
        TEST_BOOLEAN = registerOption(ConfigCategory.TEST, new BoolOption(
            true,
            "test_boolean"
        ));

        TEST_BRUSH_BEHAVIOUR = registerOption(ConfigCategory.TEST, new BrushParticleBehaviourOption(
            BrushParticleBehaviour.BLOCK_OVERRIDE_OR_DUST,
            "test_brush_behaviour"
        ));

        TEST_LOCATIONS = registerOption(ConfigCategory.TEST_2, new BlockOrTagLocationListOption(
            ConfigHandler.azaleaLeaf_Blocks_DEFAULT,
            "test_locations"
        ));
    }

    private static <T> ConfigOption<T> registerOption(ConfigCategory category, ConfigOption<T> option) {
        OPTIONS.computeIfAbsent(category, c -> new ArrayList<>());
        OPTIONS.get(category).add(option);
        return option;
    }

    private static final String FILE_NAME = Constants.MOD_ID + ".json";

    private static File getConfigFile() {
        return PlatformHelper.getConfigPath().resolve(FILE_NAME).toFile();
    }

    public static void saveIfAnyDirtyOptions() {
        for (Map.Entry<ConfigCategory, List<ConfigOption<?>>> entry : OPTIONS.entrySet()) {
            List<ConfigOption<?>> options = entry.getValue();
            if(options.stream().noneMatch(ConfigOption::isDirty)) return;

            for (ConfigOption<?> option : options) {
                if(option.isDirty()) option.applyPendingValue();
            }
        }
        saveConfig();
    }

    public static void saveConfig() {
        JsonObject root = new JsonObject();

        for (Map.Entry<ConfigCategory, List<ConfigOption<?>>> entry : OPTIONS.entrySet()) {
            List<ConfigOption<?>> options = entry.getValue();
            JsonObject categoryRoot = new JsonObject();

            for (ConfigOption<?> option : options) {
                JsonElement encodedOption = option.toJson();
                if(encodedOption == null) continue;
                categoryRoot.add(option.getJsonKey(), encodedOption);
            }

            root.add(entry.getKey().id(), categoryRoot);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String encodedJson = gson.toJson(root);

        try (FileWriter writer = new FileWriter(getConfigFile())) {
            writer.write(encodedJson);
        } catch (IOException e) {
            Logging.error("Failed to write config file '{}', {}", FILE_NAME, e);
        }
    }

    public static void readConfig() {
        Gson gson = new Gson();
        JsonObject decodedConfig = new JsonObject();

        try {
            JsonReader jsonReader = gson.newJsonReader(new FileReader(getConfigFile()));
            jsonReader.setStrictness(Strictness.LENIENT);
            decodedConfig = JsonParser.parseReader(jsonReader).getAsJsonObject();
        } catch (JsonParseException e) {
            Logging.error("Failed to parse config file '{}', {}", FILE_NAME, e);
        } catch (FileNotFoundException e) {
            Logging.info("Config file '{}' not found", FILE_NAME);
            saveConfig();
        }

        for (Map.Entry<ConfigCategory, List<ConfigOption<?>>> entry : OPTIONS.entrySet()) {
            List<ConfigOption<?>> options = entry.getValue();
            JsonObject categoryRoot = decodedConfig.getAsJsonObject(entry.getKey().id());
            if(categoryRoot == null) continue;

            for (ConfigOption<?> option : options) {
                option.fromJson(categoryRoot);
            }
        }
    }

    public static void resetAndSaveAllOptions() {
        iterateOptions((category, option) -> {
            option.resetToDefault(true);
        });
        saveConfig();
    }

    public static void clearAllPendingValues() {
        iterateOptions((category, option) -> {
            option.clearPendingValue();
        });
    }

    public static void iterateOptions(OptionAcceptor optionAcceptor) {
        for (Map.Entry<ConfigCategory, List<ConfigOption<?>>> entry : OPTIONS.entrySet()) {
            ConfigCategory category = entry.getKey();
            for (ConfigOption<?> option : entry.getValue()) {
                optionAcceptor.accept(category, option);
            }
        }
    }

    @FunctionalInterface
    public interface OptionAcceptor {
        void accept(ConfigCategory category, ConfigOption<?> option);
    }
}
