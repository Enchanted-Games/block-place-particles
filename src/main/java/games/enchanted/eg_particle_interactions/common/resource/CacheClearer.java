package games.enchanted.eg_particle_interactions.common.resource;

import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.util.texture.TexturePalettes;

public class CacheClearer {
    public static void clear() {
        TexturePalettes.invalidateCaches();
        Logging.info("Cleared particle palette cache");
    }
}
