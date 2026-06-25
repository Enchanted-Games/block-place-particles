//? if fabric {
package games.enchanted.eg_particle_interactions.fabric;

import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import net.fabricmc.api.ClientModInitializer;

public class FabricClientEntry implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleInteractionsMod.startOfModLoading();
        ParticleInteractionsMod.endOfModLoading();
    }
}
//?}
