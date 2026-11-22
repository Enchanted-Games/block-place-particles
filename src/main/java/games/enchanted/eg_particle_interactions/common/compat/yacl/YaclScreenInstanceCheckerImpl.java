package games.enchanted.eg_particle_interactions.common.compat.yacl;

import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.client.gui.screens.Screen;

public class YaclScreenInstanceCheckerImpl implements YaclScreenInstanceChecker {
    @Override
    public boolean isYaclScreen(Screen screen) {
        return screen instanceof YACLScreen;
    }
}
