package games.enchanted.eg_particle_interactions.common.config.upgrade;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import games.enchanted.eg_particle_interactions.common.config.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.config.upgrade.rules.UpgradeRule;

import java.util.ArrayList;
import java.util.List;

public class ConfigUpgrader {
    static final List<Rule> upgradeRules = new ArrayList<>();

    public static void upgrade(JsonObject configObject, int configVersion) {
        for (Rule rule : upgradeRules) {
            if(rule.fromVersion() < configVersion) continue;
            rule.rule().upgrade(configObject);
        }

        configObject.add(ConfigOptions.CONFIG_VERSION_KEY, new JsonPrimitive(ConfigOptions.CONFIG_VERSION));
    }

    private record Rule(UpgradeRule rule, int fromVersion) {
    }

    static {
        UpgradeRules.registerRules();
    }

    static void registerUpgradeRule(UpgradeRule rule, int fromVersion) {
        upgradeRules.add(new Rule(rule, fromVersion));
    }
}
