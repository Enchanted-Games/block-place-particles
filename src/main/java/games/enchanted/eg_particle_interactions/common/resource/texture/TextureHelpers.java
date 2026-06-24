package games.enchanted.eg_particle_interactions.common.resource.texture;

import games.enchanted.eg_particle_interactions.common.duck.AtlasManagerAdditions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.data.AtlasIds;
import games.enchanted.eg_particle_interactions.common.resource.ParticlePaletteAtlasManager;
import org.jspecify.annotations.NonNull;

public class TextureHelpers {
    public static @NonNull TextureAtlas getTextureAtlasOrThrow(Identifier atlasId) {
        return Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(atlasId);
    }

    public static Identifier getAtlasIdFromTexturePath(Identifier atlasTexturePath) {
        return ((AtlasManagerAdditions) Minecraft.getInstance().getAtlasManager()).eg_particle_interactions$atlasIdFromTexturePath(atlasTexturePath);
    }

    public static Identifier getTexturePathFromAtlasId(Identifier atlasId) {
        return ((AtlasManagerAdditions) Minecraft.getInstance().getAtlasManager()).eg_particle_interactions$texturePathFromAtlasId(atlasId);
    }

    public static AtlasIdAndTexture getAtlasIdAndTextureFromTexturePath(Identifier atlasTexturePath) {
        Identifier atlasId = getAtlasIdFromTexturePath(atlasTexturePath);
        return new AtlasIdAndTexture(atlasId, getTextureAtlasOrThrow(atlasId).location());
    }

    public static TextureAtlasSprite getSpriteFromAtlas(Identifier spriteId, Identifier atlasId) {
        return getTextureAtlasOrThrow(atlasId).getSprite(spriteId);
    }

    public static TextureAtlasSprite getParticlePaletteSprite(Identifier spriteId) {
        return getTextureAtlasOrThrow(ParticlePaletteAtlasManager.ATLAS_ID).getSprite(spriteId);
    }

    public static TextureAtlasSprite getParticlePaletteOrBlockSprite(Identifier blockLocation, Identifier fallbackSpriteLocation) {
        TextureAtlasSprite particlePaletteSprite = getParticlePaletteSprite(blockLocation);
        if(particlePaletteSprite.contents().name() == MissingTextureAtlasSprite.getLocation()) {
            return getTextureAtlasOrThrow(getBlocksAtlasID()).getSprite(fallbackSpriteLocation);
        }
        return particlePaletteSprite;
    }

    public static Identifier getBlocksAtlasID() {
        return AtlasIds.BLOCKS;
    }

    public static TextureAtlasSprite missingParticleSprite() {
        return TextureHelpers.getSpriteFromAtlas(MissingTextureAtlasSprite.getLocation(), AtlasIds.PARTICLES);
    }

    public static TextureAtlasSprite getItemParticleSprite(ItemStackTemplate item, ClientLevel level, RandomSource random) {
        final ItemStackRenderState scratchRenderState = new ItemStackRenderState();
        Minecraft.getInstance().getItemModelResolver().updateForTopItem(scratchRenderState, item.create(), ItemDisplayContext.GROUND, level, null, 0);
        Material.Baked material = scratchRenderState.pickParticleMaterial(random);
        return material != null ? material.sprite() : missingParticleSprite();
    }
}
