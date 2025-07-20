//? if fabric {
package games.enchanted.eg_particle_interactions.fabric;

import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.particle.ModParticleTypes;
import games.enchanted.eg_particle_interactions.common.resource.ClientResourceReload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public class FabricClientEntry implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleInteractionsMod.startOfModLoading();

        // register particles
        ModParticleTypes.registerParticles();

        // register client resource reload listener
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(
            new SimpleSynchronousResourceReloadListener() {
                @Override
                public ResourceLocation getFabricId() {
                    return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "client_resource_reloader");
                }

                @Override
                public void onResourceManagerReload(ResourceManager resourceManager) {
                    ClientResourceReload.onReload(resourceManager);
                }
            }
        );

        ParticleInteractionsMod.endOfModLoading();
    }
}
//?}
