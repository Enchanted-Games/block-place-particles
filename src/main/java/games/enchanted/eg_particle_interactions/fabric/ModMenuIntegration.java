//? if fabric && modmenu {
package games.enchanted.eg_particle_interactions.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import games.enchanted.eg_particle_interactions.common.config.compat.ConfigScreenCreator;
import games.enchanted.eg_particle_interactions.common.config.screen.yacl.YaclConfigScreen;
import net.minecraft.client.gui.screens.Screen;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ConfigScreenCreator.getScreenCreator().createScreen(parent);
    }


}
//?}