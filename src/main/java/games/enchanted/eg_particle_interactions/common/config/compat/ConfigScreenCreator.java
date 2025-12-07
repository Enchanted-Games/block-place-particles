package games.enchanted.eg_particle_interactions.common.config.compat;

import games.enchanted.eg_particle_interactions.common.config.screen.yacl.YaclConfigScreen;
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
            return new YaclConfigScreen();
        }
        return new ConfigScreenCreator() {
            @Override
            public @Nullable Screen createScreen(Screen parent) {
                return null;
            }

            @Override
            public boolean canCreateScreen() {
                return false;
            }
        };
    }
}
