package games.enchanted.eg_particle_interactions.common.resource;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.metadata.MetadataSectionType;

//? if minecraft: <= 1.21.8 {
/*import net.minecraft.client.resources.TextureAtlasHolder;
 *///?}

import java.util.Set;

public class ParticlePaletteAtlasManager
    //? if minecraft: <= 1.21.8 {
    /*extends TextureAtlasHolder
    *///?}
{
    private static final String ID = "particle_palettes";
    public static final Identifier ATLAS_LOCATION = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/atlas/" + ID);
    public static final Identifier ATLAS_ID = Identifier.fromNamespaceAndPath(Constants.MOD_ID, ID);
    public static final Set<MetadataSectionType<?>> METADATA_SECTIONS = Set.of(ParticlePaletteSettingsMetadataSection.TYPE);

    public ParticlePaletteAtlasManager(TextureManager textureManager) {
        //? if minecraft: <= 1.21.8 {
        /*super(
            textureManager,
            ATLAS_LOCATION,
            ATLAS_ID,
            METADATA_SECTIONS
        );
        *///?}
    }

    public TextureAtlasSprite get(Identifier location) {
        //? if minecraft: <= 1.21.8 {
        /*return this.getSprite(location);
        *///?} else {
        return Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(ATLAS_ID).getSprite(location);
        //?}
    }

    public static ParticlePaletteSettingsMetadataSection getMetadataFromSprite(TextureAtlasSprite sprite) {
        //? if minecraft: <= 1.21.8 {
        /*return sprite.contents().metadata().getSection(ParticlePaletteSettingsMetadataSection.TYPE).orElse(ParticlePaletteSettingsMetadataSection.DEFAULT);
        *///?} else {
        return sprite.contents().getAdditionalMetadata(ParticlePaletteSettingsMetadataSection.TYPE).orElse(ParticlePaletteSettingsMetadataSection.DEFAULT);
        //?}
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
