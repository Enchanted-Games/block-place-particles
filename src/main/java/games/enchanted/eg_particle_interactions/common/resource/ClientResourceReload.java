package games.enchanted.eg_particle_interactions.common.resource;

import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;
import net.minecraft.server.packs.resources.ResourceManager;

public class ClientResourceReload {
    public static void onReload(ResourceManager resourceManager) {
        ColourUtil.invalidateCaches();
        Logging.info("Cleared average texture colour cache and opaque pixels cache");
    }
}
