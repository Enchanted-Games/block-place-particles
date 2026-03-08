package games.enchanted.eg_particle_interactions.common.mixin.client.resource;

import games.enchanted.eg_particle_interactions.common.duck.AtlasManagerAdditions;
import games.enchanted.eg_particle_interactions.common.util.MapUtil;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(AtlasManager.class)
public class AtlasManagerMixin implements AtlasManagerAdditions {
    @Shadow @Final private Map<Identifier, AtlasManager.AtlasEntry> atlasByTexture;
    @Shadow @Final private Map<Identifier, AtlasManager.AtlasEntry> atlasById;

    @Unique private Map<AtlasManager.AtlasEntry, Identifier> eg_particle_interactions$inverseAtlasByTexture = null;
    @Unique private Map<AtlasManager.AtlasEntry, Identifier> eg_particle_interactions$inverseAtlasById = null;

    @Override
    public Identifier eg_particle_interactions$atlasIdFromTexturePath(Identifier texturePath) {
        AtlasManager.AtlasEntry entry = this.atlasByTexture.get(texturePath);
        if(this.eg_particle_interactions$inverseAtlasById == null) {
            this.eg_particle_interactions$inverseAtlasById = MapUtil.inverseMapEntries(this.atlasById);
        }
        return this.eg_particle_interactions$inverseAtlasById.get(entry);
    }

    @Override
    public Identifier eg_particle_interactions$texturePathFromAtlasId(Identifier atlasId) {
        AtlasManager.AtlasEntry entry = this.atlasById.get(atlasId);
        if(this.eg_particle_interactions$inverseAtlasByTexture == null) {
            this.eg_particle_interactions$inverseAtlasByTexture = MapUtil.inverseMapEntries(this.atlasByTexture);
        }
        return this.eg_particle_interactions$inverseAtlasByTexture.get(entry);
    }

    @Inject(
        method = "close",
        at = @At("HEAD")
    )
    private void eg_particle_interactions$close(CallbackInfo ci) {
        this.eg_particle_interactions$inverseAtlasById = null;
        this.eg_particle_interactions$inverseAtlasByTexture = null;
    }
}
