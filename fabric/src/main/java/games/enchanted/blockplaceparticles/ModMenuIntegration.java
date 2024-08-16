package games.enchanted.blockplaceparticles;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import games.enchanted.blockplaceparticles.config.ParticleInteractionsConfigScreen;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ParticleInteractionsConfigScreen::createConfigScreen;
    }
}