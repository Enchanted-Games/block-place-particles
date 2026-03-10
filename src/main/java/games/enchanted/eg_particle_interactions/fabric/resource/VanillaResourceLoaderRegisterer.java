package games.enchanted.eg_particle_interactions.fabric.resource;

import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.MinecraftAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public class VanillaResourceLoaderRegisterer implements FabricResourceLoaderRegisterer {
    @Override
    public void registerResourceLoader(PreparableReloadListener listener, Identifier id) {
        //noinspection resource
        ((MinecraftAccessor) Minecraft.getInstance()).eg_particle_interactions$getResourceManager().registerReloadListener(listener);
    }
}
