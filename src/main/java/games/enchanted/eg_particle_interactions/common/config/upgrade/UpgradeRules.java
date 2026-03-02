package games.enchanted.eg_particle_interactions.common.config.upgrade;

import com.google.gson.JsonElement;
import games.enchanted.eg_particle_interactions.common.config.ConfigCategory;
import games.enchanted.eg_particle_interactions.common.config.upgrade.rules.UnderwaterBubble1to2Upgrade;

public class UpgradeRules {
    public static void registerRules() {
        ConfigUpgrader.registerUpgradeRule(new UnderwaterBubble1to2Upgrade(), 1);
    }
}
