package games.enchanted.eg_particle_interactions.common.resource;

import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;
import net.minecraft.server.packs.resources.ResourceManager;

public class CacheClearer {
    public static void clear() {
        ColourUtil.invalidateCaches();
        Logging.info("Cleared particle palette cache");
    }
}
