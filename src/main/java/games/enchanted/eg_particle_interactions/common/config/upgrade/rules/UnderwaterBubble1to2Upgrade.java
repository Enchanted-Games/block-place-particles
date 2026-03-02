package games.enchanted.eg_particle_interactions.common.config.upgrade.rules;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import games.enchanted.eg_particle_interactions.common.config.ConfigCategory;

public class UnderwaterBubble1to2Upgrade implements UpgradeRule {
    private static final String UNDERWATER_BUBBLES_ON_PLACE_ENABLED = "underwater_bubbles_on_place_enabled";
    private static final String UNDERWATER_BUBBLES_MAX_ON_PLACE = "underwater_bubbles_max_on_place";
    private static final String UNDERWATER_BUBBLES_ON_BREAK_ENABLED = "underwater_bubbles_on_break_enabled";
    private static final String UNDERWATER_BUBBLES_MAX_ON_BREAK = "underwater_bubbles_max_on_break";

    @Override
    public void upgrade(JsonObject config) {
        if(!config.has(ConfigCategory.BLOCK_INTERACTIONS.id())) return;
        JsonElement interactionsElm = config.get(ConfigCategory.BLOCK_INTERACTIONS.id());
        if(!interactionsElm.isJsonObject()) return;
        JsonObject interactionsObj = interactionsElm.getAsJsonObject();

        if(interactionsObj.has(UNDERWATER_BUBBLES_ON_PLACE_ENABLED)) {
            JsonElement onPlaceEnabledElm = interactionsObj.get(UNDERWATER_BUBBLES_ON_PLACE_ENABLED);
            if(!onPlaceEnabledElm.isJsonPrimitive()) return;
            if(!onPlaceEnabledElm.getAsBoolean()) {
                interactionsObj.remove(UNDERWATER_BUBBLES_ON_PLACE_ENABLED);
                interactionsObj.remove(UNDERWATER_BUBBLES_MAX_ON_PLACE);
                interactionsObj.addProperty(UNDERWATER_BUBBLES_MAX_ON_PLACE, 0);
            }
        }

        if(interactionsObj.has(UNDERWATER_BUBBLES_ON_BREAK_ENABLED)) {
            JsonElement onBreakEnabledElm = interactionsObj.get(UNDERWATER_BUBBLES_ON_BREAK_ENABLED);
            if(!onBreakEnabledElm.isJsonPrimitive()) return;
            if(!onBreakEnabledElm.getAsBoolean()) {
                interactionsObj.remove(UNDERWATER_BUBBLES_ON_BREAK_ENABLED);
                interactionsObj.remove(UNDERWATER_BUBBLES_MAX_ON_BREAK);
                interactionsObj.addProperty(UNDERWATER_BUBBLES_MAX_ON_BREAK, 0);
            }
        }
    }
}
