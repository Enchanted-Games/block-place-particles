package games.enchanted.eg_particle_interactions.common.config;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.config.categories.*;
import games.enchanted.eg_particle_interactions.common.config.option.ConfigOption;
import games.enchanted.eg_particle_interactions.common.config.upgrade.ConfigUpgrader;
import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigOptions {
    private static final Map<ConfigCategory, List<ConfigOption<?>>> OPTIONS;
    private static Map<ConfigCategory, List<ConfigOption<?>>> TEMPORARY_REGISTRATION_MAP;

    public static final String CONFIG_VERSION_KEY = "config_version";
    public static final int CONFIG_VERSION = 2;

    static {
        TEMPORARY_REGISTRATION_MAP = new HashMap<>();
        GeneralOptions.init();
        BlockInteractionOptions.init();
        ItemInteractionOptions.init();
        EntityOptions.init();
        FluidInteractionOptions.init();
        OPTIONS = Map.copyOf(TEMPORARY_REGISTRATION_MAP);
        TEMPORARY_REGISTRATION_MAP = null;
    }

    public static <T> ConfigOption<T> registerOption(ConfigCategory category, ConfigOption<T> option) {
        if(TEMPORARY_REGISTRATION_MAP == null) {
            throw new IllegalStateException("Tried to call registerOption after config has already been initialized. Category: " + category.id() + ", Option: " + option.getJsonKey());
        }
        TEMPORARY_REGISTRATION_MAP.computeIfAbsent(category, c -> new ArrayList<>());
        TEMPORARY_REGISTRATION_MAP.get(category).add(option);
        return option;
    }

    private static final String FILE_NAME = Constants.MOD_ID + ".json";

    private static File getConfigFile() {
        return PlatformHelper.getConfigPath().resolve(FILE_NAME).toFile();
    }

    /**
     * Applies any pending options and saves the config. If no options are pending config is not saved
     */
    public static void applyAndSaveConfig() {
        boolean anyDirty = false;
        for (Map.Entry<ConfigCategory, List<ConfigOption<?>>> entry : OPTIONS.entrySet()) {
            List<ConfigOption<?>> options = entry.getValue();
            if(options.stream().noneMatch(ConfigOption::isDirty)) {
                anyDirty = true;
            }

            for (ConfigOption<?> option : options) {
                if(option.isDirty()) {
                    option.applyPendingValue();
                }
            }
        }
        if(!anyDirty) return;
        saveConfig();
    }

    /**
     * Saves current values of the config. Pending values will not be applied before saving
     */
    public static void saveConfig() {
        JsonObject root = new JsonObject();

        root.add(CONFIG_VERSION_KEY, new JsonPrimitive(CONFIG_VERSION));

        for (Map.Entry<ConfigCategory, List<ConfigOption<?>>> entry : OPTIONS.entrySet()) {
            List<ConfigOption<?>> options = entry.getValue();
            JsonObject categoryRoot = new JsonObject();

            for (ConfigOption<?> option : options) {
                if(option.isDefault()) continue;
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

        int configVer = CONFIG_VERSION;
        try {
            if(decodedConfig.has(CONFIG_VERSION_KEY)) {
                configVer = decodedConfig.getAsJsonPrimitive(CONFIG_VERSION_KEY).getAsInt();
            } else {
                configVer = 0;
            }
        } catch (ClassCastException | NumberFormatException ignored) {}

        ConfigUpgrader.upgrade(decodedConfig, configVer);

        for (Map.Entry<ConfigCategory, List<ConfigOption<?>>> entry : OPTIONS.entrySet()) {
            List<ConfigOption<?>> options = entry.getValue();
            JsonObject categoryRoot = decodedConfig.getAsJsonObject(entry.getKey().id());
            if(categoryRoot == null) continue;

            for (ConfigOption<?> option : options) {
                option.fromJson(categoryRoot);
                option.applyPendingValue();
            }
        }

        saveConfig();
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
