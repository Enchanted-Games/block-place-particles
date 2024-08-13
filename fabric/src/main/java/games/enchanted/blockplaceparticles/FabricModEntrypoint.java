package games.enchanted.blockplaceparticles;

import net.fabricmc.api.ModInitializer;

public class FabricModEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonEntrypoint.initBeforeRegistration();
    }
}
