package games.enchanted.eg_particle_interactions.common.mixin.resource;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.resource.ParticlePaletteAtlasManager;
import net.minecraft.client.resources.model.AtlasManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(AtlasManager.class)
public class RegisterAtlasConfig_AtlasManagerMixin {

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Ljava/util/List;of([Ljava/lang/Object;)Ljava/util/List;"),
        method = "<clinit>"
    )
    private static List<AtlasManager.AtlasConfig> block_place_particle$registerAtlases(Object[] list, Operation<List<AtlasManager.AtlasConfig>> original) {
        List<AtlasManager.AtlasConfig> atlasConfigs = original.call((Object) list);
        ArrayList<AtlasManager.AtlasConfig> newConfigs = new ArrayList<>(atlasConfigs);
        newConfigs.add(new AtlasManager.AtlasConfig(ParticlePaletteAtlasManager.ATLAS_LOCATION, ParticlePaletteAtlasManager.ATLAS_ID, false, ParticlePaletteAtlasManager.METADATA_SECTIONS));
        return newConfigs;
    }
}
