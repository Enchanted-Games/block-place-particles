package games.enchanted.eg_particle_interactions.common.compat.yacl;

import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;
import net.minecraft.client.gui.screens.Screen;

public interface YaclScreenInstanceChecker {
    YaclScreenInstanceChecker INSTANCE = instance();

    default boolean isYaclScreen(Screen screen) {
        return false;
    }

    static YaclScreenInstanceChecker instance() {
        if(PlatformHelper.isModLoaded(Constants.YACL_ID)) {
            return new YaclScreenInstanceCheckerImpl();
        } else {
            return new YaclScreenInstanceChecker() {};
        }
    }
}
