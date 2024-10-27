package games.enchanted.blockplaceparticles.resource;

import games.enchanted.blockplaceparticles.ParticleInteractionsLogging;
import games.enchanted.blockplaceparticles.util.ColourUtil;
import net.minecraft.server.packs.resources.ResourceManager;

public class ClientResourceReload {
    public static void onReload(ResourceManager resourceManager) {
        ColourUtil.invalidateCaches();
        ParticleInteractionsLogging.message("Cleared average block colour cache");
    }
}
