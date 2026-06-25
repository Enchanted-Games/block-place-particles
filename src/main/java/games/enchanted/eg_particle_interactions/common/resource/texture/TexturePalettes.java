package games.enchanted.eg_particle_interactions.common.resource.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.NativeImageAccessor;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.SpriteContentsAccessor;
import games.enchanted.eg_particle_interactions.common.predicates.block.BlockStatePredicate;
import games.enchanted.eg_particle_interactions.common.predicates.fluid.FluidStatePredicate;
import games.enchanted.eg_particle_interactions.common.resource.texture.palette.BlockPaletteManager;
import games.enchanted.eg_particle_interactions.common.resource.texture.palette.FluidPaletteManager;
import games.enchanted.eg_particle_interactions.common.resource.texture.palette.Palette;
import games.enchanted.eg_particle_interactions.common.resource.texture.palette.PaletteDefinition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TexturePalettes {
    private static final int OPAQUE_PIXELS_THRESHOLD = 20;
    private static final HashMap<BlockState, Palette> BLOCKSTATE_TO_PALETTE_CACHE = new HashMap<>();
    private static final HashMap<FluidState, Palette> FLUIDSTATE_TO_PALETTE_CACHE = new HashMap<>();

    /**
     * Gets a random pixel's colour from a {@link BlockState}'s particle texture
     *
     * @param blockState the block state to get a random colour from
     * @return the colour in an array of a, r, g, b
     */
    public static int[] getRandomBlockColour(BlockState blockState, int[] tintColour) {
        if(BLOCKSTATE_TO_PALETTE_CACHE.containsKey(blockState)) {
            Palette palette = BLOCKSTATE_TO_PALETTE_CACHE.get(blockState);
            return palette.getRandomColour(tintColour);
        }

        PaletteDefinition<BlockState, BlockStatePredicate> paletteDefinition = BlockPaletteManager.INSTANCE.getOrNull(blockState);
        if(paletteDefinition != null) {
            Palette palette = paletteDefinition.getPalette(blockState);
            BLOCKSTATE_TO_PALETTE_CACHE.put(blockState, palette);
            Logging.textureDebugInfo("Blockstate {} has a palette override, using that instead of generating a new palette", blockState);
            return palette.getRandomColour(tintColour);
        }

        var model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(blockState);
        TextureAtlasSprite sprite =
            //? if minecraft: >= 26.1 {
            model.particleMaterial().sprite();
            //? } else {
            /*model.particleIcon();
            *///? }

        Palette palette = getOrGeneratePalette(sprite, blockState, BLOCKSTATE_TO_PALETTE_CACHE);

        Logging.textureDebugInfo("Created texture palette for block state {}. sprite '{}' size {}x{}. palette: {}", blockState, sprite.contents().name(), sprite.contents().width(), sprite.contents().height(), palette.debugEntriesString());

        return palette.getRandomColour(tintColour);
    }

    /**
     * Gets a random pixel's colour from one of the textures in a {@link FluidState} model
     *
     * @param fluidState the fluid state to get a random colour from
     * @return the colour in an array of a, r, g, b
     */
    public static int[] getRandomFluidColour(FluidState fluidState, int[] tintColour, FluidStateMaterialSource materialSource) {
        if(FLUIDSTATE_TO_PALETTE_CACHE.containsKey(fluidState)) {
            Palette palette = FLUIDSTATE_TO_PALETTE_CACHE.get(fluidState);
            return palette.getRandomColour(tintColour);
        }

        PaletteDefinition<FluidState, FluidStatePredicate> paletteDefinition = FluidPaletteManager.INSTANCE.getOrNull(fluidState);
        if(paletteDefinition != null) {
            Palette palette = paletteDefinition.getPalette(fluidState);
            FLUIDSTATE_TO_PALETTE_CACHE.put(fluidState, palette);
            Logging.textureDebugInfo("Fluidstate {} has a palette override, using that instead of generating a new palette", fluidState);
            return palette.getRandomColour(tintColour);
        }

        var model = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidState);
        TextureAtlasSprite sprite = materialSource.spriteConverter().apply(model);

        Palette palette = getOrGeneratePalette(sprite, fluidState, FLUIDSTATE_TO_PALETTE_CACHE);

        Logging.textureDebugInfo("Created texture palette for fluid state {}. sprite '{}' size {}x{}. palette: {}", fluidState, sprite.contents().name(), sprite.contents().width(), sprite.contents().height(), palette.debugEntriesString());

        return palette.getRandomColour(tintColour);
    }

    private static <T> Palette getOrGeneratePalette(TextureAtlasSprite sprite, T state, Map<T, Palette> paletteCache) {
        SpriteContents spriteContents = sprite.contents();
        Palette palette = generatePalette(spriteContents);
        if(palette.cacheable()) {
            paletteCache.put(state, palette);
        }
        return palette;
    }

    public static Palette generatePalette(SpriteContents paletteSprite) {
        ArrayList<Integer> colours = new ArrayList<>();
        NativeImage image = ((SpriteContentsAccessor) paletteSprite).eg_particle_interactions$getOriginalImage();

        if(((NativeImageAccessor) (Object) image).eg_particle_interactions$getPixels() == 0L) {
            // if image is somehow not allocated, return white as fallback
            Logging.textureDebugInfo("Sprite {} is not allocated", paletteSprite.name());
            return Palette.BLANK;
        }

        for (int x = 0; x < paletteSprite.width(); x++) {
            for (int y = 0; y < paletteSprite.height(); y++) {
                int sampledColour = image.getPixel(x, y);
                int alpha = ARGB.alpha(sampledColour);

                if(alpha <= OPAQUE_PIXELS_THRESHOLD) continue;
                colours.add(sampledColour);
            }
        }

        if(colours.isEmpty()) {
            // image has no valid palette colours
            Logging.textureDebugInfo("Sprite {} contains no valid pixels for palette", paletteSprite.name());
            return Palette.CACHEABLE_BLANK;
        }

        Logging.textureDebugInfo("Sprite {} has {} valid palette pixels", paletteSprite.name(), colours.size());

        Palette.Entry[] paletteEntries = new Palette.Entry[colours.size()];
        for (int i = 0; i < colours.size(); i++) {
            paletteEntries[i] = new Palette.Entry(colours.get(i));
        }
        return new Palette(paletteEntries, true);
    }

    /**
     * Clears all calculated average colours
     */
    public static void invalidateCaches() {
        BLOCKSTATE_TO_PALETTE_CACHE.clear();
        FLUIDSTATE_TO_PALETTE_CACHE.clear();
        Logging.info("Cleared particle palette cache");
    }

    public record FluidStateMaterialSource(Function<FluidModel, TextureAtlasSprite> spriteConverter, Function<FluidModel, Material.Baked> materialConverter) {
        public static final FluidStateMaterialSource STILL = new FluidStateMaterialSource(
            fluidModel -> fluidModel.stillMaterial().sprite(),
            FluidModel::stillMaterial
        );

        public static final FluidStateMaterialSource FLOWING = new FluidStateMaterialSource(
            fluidModel -> fluidModel.flowingMaterial().sprite(),
            FluidModel::flowingMaterial
        );

        public static Codec<FluidStateMaterialSource> CODEC = Codec.STRING.xmap(
            s -> {
                switch (s) {
                    case "still": return STILL;
                    case "flowing": return FLOWING;
                }
                throw new IllegalArgumentException("Unknown fluid sprite source '" + s + "'");
            },
            materialSource -> {
                if(materialSource == STILL) return "still";
                return "flowing";
            }
        );
    }
}
