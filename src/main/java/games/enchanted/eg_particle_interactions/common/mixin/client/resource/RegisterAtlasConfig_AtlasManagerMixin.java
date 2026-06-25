package games.enchanted.eg_particle_interactions.common.mixin.client.resource;

import games.enchanted.eg_particle_interactions.common.resource.ParticlePaletteAtlasManager;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import org.spongepowered.asm.mixin.*;

import java.util.ArrayList;
import java.util.List;

@Mixin(AtlasManager.class)
public class RegisterAtlasConfig_AtlasManagerMixin {
    @Shadow @Final private static List<AtlasManager.AtlasConfig> KNOWN_ATLASES;

    static {
        List<AtlasManager.AtlasConfig> configs = new ArrayList<>(KNOWN_ATLASES);
        configs.add(new AtlasManager.AtlasConfig(ParticlePaletteAtlasManager.ATLAS_TEXTURE_ID, ParticlePaletteAtlasManager.ATLAS_ID, false, ParticlePaletteAtlasManager.METADATA_SECTIONS));
        KNOWN_ATLASES = List.copyOf(configs);
    }
}