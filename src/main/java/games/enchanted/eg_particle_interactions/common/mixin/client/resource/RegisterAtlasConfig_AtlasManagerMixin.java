package games.enchanted.eg_particle_interactions.common.mixin.client.resource;

import net.minecraft.client.resources.model.sprite.AtlasManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;

import games.enchanted.eg_particle_interactions.common.resource.ParticlePaletteAtlasManager;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(AtlasManager.class)
public class RegisterAtlasConfig_AtlasManagerMixin {
    @Shadow @Final private static List<AtlasManager.AtlasConfig> KNOWN_ATLASES;

    static {
        KNOWN_ATLASES = new ArrayList<>(KNOWN_ATLASES);
        KNOWN_ATLASES.add(new AtlasManager.AtlasConfig(ParticlePaletteAtlasManager.ATLAS_LOCATION, ParticlePaletteAtlasManager.ATLAS_ID, false, ParticlePaletteAtlasManager.METADATA_SECTIONS));
        KNOWN_ATLASES = List.copyOf(KNOWN_ATLASES);
    }
}