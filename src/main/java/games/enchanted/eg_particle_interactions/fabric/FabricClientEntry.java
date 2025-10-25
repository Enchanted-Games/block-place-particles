//? if fabric {
package games.enchanted.eg_particle_interactions.fabric;

import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.particle.ModParticleTypes;
import net.fabricmc.api.ClientModInitializer;

public class FabricClientEntry implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleInteractionsMod.startOfModLoading();

        // register particles
        ModParticleTypes.registerParticles();

        ParticleInteractionsMod.endOfModLoading();
    }
}
//?}
