package games.enchanted.eg_particle_interactions.common.config.compat;

import games.enchanted.eg_particle_interactions.common.config.screen.fallback.FallbackConfigScreenCreator;
import games.enchanted.eg_particle_interactions.common.config.screen.yacl.YaclConfigScreenCreator;
import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public interface ConfigScreenCreator {
    @Nullable Screen createScreen(Screen parent);
    default boolean canCreateScreen() {
        return true;
    }

    static ConfigScreenCreator getScreenCreator() {
        if(PlatformHelper.isModLoaded("yet_another_config_lib_v3")) {
            return new YaclConfigScreenCreator();
        }
        return new FallbackConfigScreenCreator();
    }
}
