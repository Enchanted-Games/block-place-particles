package games.enchanted.eg_particle_interactions.common.particle.appearance.texture;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.LayerDefinition;
import games.enchanted.eg_particle_interactions.common.particle.appearance.SpriteCycleMode;
import games.enchanted.eg_particle_interactions.common.resource.texture.AtlasIdAndTexture;
import games.enchanted.eg_particle_interactions.common.resource.texture.TextureHelpers;
import games.enchanted.eg_particle_interactions.common.resource.texture.TexturePalettes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;

import java.util.List;

public class FluidTextureConfig implements TextureConfig {
    public static final List<Identifier> DEFAULT_SPRITES = List.of(MissingTextureAtlasSprite.getLocation());

    public static final MapCodec<? extends TextureConfig> MAP_CODEC = RecordCodecBuilder.<FluidTextureConfig>mapCodec(
        instance -> instance.group(
            Codec.list(Identifier.CODEC).optionalFieldOf("fallback_sprites", DEFAULT_SPRITES).forGetter(o -> o.fallbackSprites),
            ModCodecs.ATLAS.optionalFieldOf("fallback_atlas", SpritesTextureConfig.DEFAULT_ATLAS).forGetter(o -> o.fallbackAtlas),
            StringRepresentable.fromEnum(LayerDefinition::values).optionalFieldOf("layer", SpritesTextureConfig.DEFAULT_LAYER_DEFINITION).forGetter(o -> o.layer),
            TexturePalettes.FluidStateMaterialSource.CODEC.optionalFieldOf("texture_type", TexturePalettes.FluidStateMaterialSource.STILL).forGetter(o -> o.materialSource)
        ).apply(
            instance,
            FluidTextureConfig::new
        )
    );

    final List<Identifier> fallbackSprites;
    final AtlasIdAndTexture fallbackAtlas;
    final LayerDefinition layer;
    final TexturePalettes.FluidStateMaterialSource materialSource;

    public FluidTextureConfig(List<Identifier> fallbackSprites, AtlasIdAndTexture fallbackAtlas, LayerDefinition layer, TexturePalettes.FluidStateMaterialSource materialSource) {
        this.fallbackSprites = fallbackSprites;
        this.fallbackAtlas = fallbackAtlas;
        this.layer = layer;
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
    public TextureAtlasSprite getRandom(ParticleContext context, RandomSource random) {
        return this.lookupSprite(context, this.fallbackSprites.get(random.nextInt(this.fallbackSprites.size())));
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
    public LayerDefinition getLayerDefinition(ParticleContext context) {
        if(context.fluidContext() == null) return this.layer;
        return LayerDefinition.fromVanillaSprite(this.getContextMaterial(context.fluidContext()).sprite(), this.layer.showBackface());
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
