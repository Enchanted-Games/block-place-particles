package games.enchanted.eg_particle_interactions.common.config.upgrade.rules;

import com.google.gson.JsonObject;

@FunctionalInterface
public interface UpgradeRule {
    void upgrade(JsonObject config);
}
