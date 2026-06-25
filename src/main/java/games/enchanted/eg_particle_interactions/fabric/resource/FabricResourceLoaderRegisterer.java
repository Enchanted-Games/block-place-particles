package games.enchanted.eg_particle_interactions.fabric.resource;

import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public interface FabricResourceLoaderRegisterer {
    static FabricResourceLoaderRegisterer getInstance() {
        //? if fabric {
        if(ParticleInteractionsMod.isFabricResourceLoaderPresent()) {
            return new FAPIResourceLoaderRegisterer();
        }
        //? }
        return new VanillaResourceLoaderRegisterer();
    }

    /**
     * Register resource loader through fabric resource loader api if present, otherwise through vanilla. No-op on neoforge
     *
     * @param listener the reload listener
     * @param id       the id
     */
    void registerResourceLoader(PreparableReloadListener listener, Identifier id);
}
