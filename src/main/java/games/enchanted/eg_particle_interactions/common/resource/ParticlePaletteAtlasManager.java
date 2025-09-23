package games.enchanted.eg_particle_interactions.common.resource;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.Constants;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.metadata.MetadataSectionType;

import java.util.Set;

public class ParticlePaletteAtlasManager extends TextureAtlasHolder {
    public static final String ATLAS_ID = "particle_palettes";
    public static final ResourceLocation ATLAS_LOCATION = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/atlas/" + ATLAS_ID);

    public ParticlePaletteAtlasManager(TextureManager textureManager) {
        super(
            textureManager,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/atlas/" + ATLAS_ID),
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, ATLAS_ID),
            Set.of(ParticlePaletteSettingsMetadataSection.TYPE)
        );
    }

    public TextureAtlasSprite get(ResourceLocation location) {
        return this.getSprite(location);
    }

    public ParticlePaletteSettingsMetadataSection getMetadata(ResourceLocation location) {
        return getMetadataFromSprite(this.get(location));
    }

    public static ParticlePaletteSettingsMetadataSection getMetadataFromSprite(TextureAtlasSprite sprite) {
        return sprite.contents().metadata().getSection(ParticlePaletteSettingsMetadataSection.TYPE).orElse(ParticlePaletteSettingsMetadataSection.DEFAULT);
    }

    public record ParticlePaletteSettingsMetadataSection(boolean useBiomeTint) {
        private static final boolean BIOME_TINT_DEFAULT = true;

        public static final ParticlePaletteSettingsMetadataSection DEFAULT = new ParticlePaletteSettingsMetadataSection(BIOME_TINT_DEFAULT);

        public static final Codec<ParticlePaletteSettingsMetadataSection> CODEC = RecordCodecBuilder.create(
            instance -> instance
                .group(
                    Codec.BOOL.optionalFieldOf("use_biome_tint", BIOME_TINT_DEFAULT).forGetter(ParticlePaletteSettingsMetadataSection::useBiomeTint)
                )
                .apply(
                    instance, ParticlePaletteSettingsMetadataSection::new
                )
        );

        public static final MetadataSectionType<ParticlePaletteSettingsMetadataSection> TYPE = new MetadataSectionType<>(Constants.MOD_ID + ":particle_palette_settings", CODEC);
    }
}
