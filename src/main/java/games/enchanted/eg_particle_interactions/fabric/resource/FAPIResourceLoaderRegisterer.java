package games.enchanted.eg_particle_interactions.fabric.resource;

import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public class FAPIResourceLoaderRegisterer implements FabricResourceLoaderRegisterer {
    @Override
    public void registerResourceLoader(PreparableReloadListener listener, Identifier id) {
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(id, listener);
    }
}
