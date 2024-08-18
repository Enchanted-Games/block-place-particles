package games.enchanted.blockplaceparticles;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import games.enchanted.blockplaceparticles.config.PI_ConfigScreen;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return PI_ConfigScreen::createConfigScreen;
    }
}
