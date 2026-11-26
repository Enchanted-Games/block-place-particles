package games.enchanted.eg_particle_interactions.common.particle.appearance.texture;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.SpriteCycleMode;
import games.enchanted.eg_particle_interactions.common.resource.texture.AtlasIdAndTexture;
import games.enchanted.eg_particle_interactions.common.resource.texture.TextureHelpers;
import games.enchanted.eg_particle_interactions.common.resource.texture.TexturePalettes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;

import java.util.List;

public class FluidTextureConfig implements TextureConfig {
    public static final List<Identifier> DEFAULT_SPRITES = List.of(MissingTextureAtlasSprite.getLocation());

    public static final MapCodec<? extends TextureConfig> MAP_CODEC = RecordCodecBuilder.<FluidTextureConfig>mapCodec(
        instance -> instance.group(
            Codec.list(Identifier.CODEC, 1, SpritesTextureConfig.MAX_LIST_LENGTH).optionalFieldOf("fallback_sprites", DEFAULT_SPRITES).forGetter(o -> o.fallbackSprites),
            ModCodecs.ATLAS.optionalFieldOf("fallback_atlas", SpritesTextureConfig.DEFAULT_ATLAS).forGetter(o -> o.fallbackAtlas),
            TexturePalettes.FluidStateMaterialSource.CODEC.optionalFieldOf("texture_type", TexturePalettes.FluidStateMaterialSource.STILL).forGetter(o -> o.materialSource)
        ).apply(
            instance,
            FluidTextureConfig::new
        )
    );

    final List<Identifier> fallbackSprites;
    final AtlasIdAndTexture fallbackAtlas;
    final TexturePalettes.FluidStateMaterialSource materialSource;

    public FluidTextureConfig(List<Identifier> fallbackSprites, AtlasIdAndTexture fallbackAtlas, TexturePalettes.FluidStateMaterialSource materialSource) {
        this.fallbackSprites = fallbackSprites;
        this.fallbackAtlas = fallbackAtlas;
        this.materialSource = materialSource;
    }

    private Material.Baked getContextMaterial(ParticleContext.FluidContext context) {
        return this.materialSource.materialConverter().apply(Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(context.state()));
    }

    private TextureAtlasSprite lookupSprite(ParticleContext context, Identifier id) {
        if(context.fluidContext() == null) {
            return TextureHelpers.getSpriteFromAtlas(id, this.fallbackAtlas.id());
        }
        return getContextMaterial(context.fluidContext()).sprite();
    }

    @Override
    public TextureAtlasSprite getAt(ParticleContext context, float agePercentage) {
        int i = (int) (agePercentage * (this.fallbackSprites.size()));
        return this.lookupSprite(context, this.fallbackSprites.get(Math.clamp(i, 0, this.fallbackSprites.size() - 1)));
    }

    @Override
    public TextureAtlasSprite getRandom(ParticleContext context, float randomValue) {
        int i = (int) (randomValue * (this.fallbackSprites.size()));
        return this.lookupSprite(context, this.fallbackSprites.get(Math.clamp(i, 0, this.fallbackSprites.size() - 1)));
    }

    @Override
    public TextureAtlasSprite getFirst(ParticleContext context) {
        return this.lookupSprite(context, this.fallbackSprites.getFirst());
    }

    @Override
    public MapCodec<? extends TextureConfig> codec() {
        return MAP_CODEC;
    }

    @Override
    public SpriteCycleMode getSpriteCycleMode(ParticleContext context) {
        return SpriteCycleMode.RANDOM_ON_SPAWN;
    }

    @Override
    public List<Identifier> getSpriteIds(ParticleContext context) {
        if(context.fluidContext() == null) return this.fallbackSprites;
        return List.of(this.getContextMaterial(context.fluidContext()).sprite().contents().name());
    }

    @Override
    public AtlasIdAndTexture getAtlas(ParticleContext context) {
        if(context.fluidContext() == null) return this.fallbackAtlas;
        return TextureHelpers.getAtlasIdAndTextureFromTexturePath(this.getContextMaterial(context.fluidContext()).sprite().atlasLocation());
    }
}
