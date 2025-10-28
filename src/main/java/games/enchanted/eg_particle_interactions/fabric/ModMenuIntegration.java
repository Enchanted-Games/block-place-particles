//? if fabric && modmenu {
package games.enchanted.eg_particle_interactions.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import games.enchanted.eg_particle_interactions.common.config.ConfigScreen;
import net.minecraft.client.Minecraft;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return games.enchanted.eg_particle_interactions.common.config2.screen.ConfigScreen::createConfigScreen;
    }
}
//?}