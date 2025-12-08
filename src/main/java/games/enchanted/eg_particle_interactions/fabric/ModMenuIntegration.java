//? if fabric && modmenu {
package games.enchanted.eg_particle_interactions.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import games.enchanted.eg_particle_interactions.common.config.compat.ConfigScreenCreator;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ConfigScreenCreator.getScreenCreator().createScreen(parent);
    }


}
//?}