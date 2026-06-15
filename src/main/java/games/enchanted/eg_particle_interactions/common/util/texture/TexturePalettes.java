package games.enchanted.eg_particle_interactions.common.util.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.NativeImageAccessor;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.SpriteContentsAccessor;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import games.enchanted.eg_particle_interactions.common.resource.ParticlePaletteAtlasManager;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;
import games.enchanted.eg_particle_interactions.common.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
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

        var model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(blockState);
        TextureAtlasSprite particleSprite =
            //? if minecraft: >= 26.1 {
            model.particleMaterial().sprite();
            //? } else {
            /*model.particleIcon();
            *///? }

        Identifier particleSpriteLocation = particleSprite.contents().name();
        particleSprite = TextureHelpers.getParticlePaletteOrBlockSprite(RegistryHelpers.getLocationFromBlock(blockState.getBlock()), particleSpriteLocation);

        Palette palette = getOrGeneratePalette(particleSprite, blockState, BLOCKSTATE_TO_PALETTE_CACHE);

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

        var model = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidState);
        TextureAtlasSprite sprite = model.flowingMaterial().sprite();

        Palette palette = getOrGeneratePalette(sprite, fluidState, FLUIDSTATE_TO_PALETTE_CACHE);

        return palette.getRandomColour(tintColour);
    }

    private static <T> Palette getOrGeneratePalette(TextureAtlasSprite sprite, T state, Map<T, Palette> paletteCache) {
        SpriteContents spriteContents = sprite.contents();
        ParticlePaletteAtlasManager.ParticlePaletteSettingsMetadataSection paletteMetadata = ParticlePaletteAtlasManager.getMetadataFromSprite(sprite);
        Palette palette = collectValidPalettePixels(spriteContents, paletteMetadata.useBiomeTint());
        if(palette.cacheable()) {
            paletteCache.put(state, palette);
        }
        return palette;
    }

    private static Palette collectValidPalettePixels(SpriteContents paletteSprite, boolean hasTint) {
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
            return Palette.BLANK;
        }

        Logging.textureDebugInfo("Sprite {} has {} valid palette pixels", paletteSprite.name(), colours.size());

        PaletteEntry[] paletteEntries = new PaletteEntry[colours.size()];
        for (int i = 0; i < colours.size(); i++) {
            paletteEntries[i] = new PaletteEntry(colours.get(i));
        }
        return new Palette(paletteEntries, true, hasTint);
    }

    /**
     * Clears all calculated average colours
     */
    public static void invalidateCaches() {
        BLOCKSTATE_TO_PALETTE_CACHE.clear();
        FLUIDSTATE_TO_PALETTE_CACHE.clear();
    }

    private record PaletteEntry(int argb) {
        int[] argbAsArray() {
            return ColourUtil.ARGBint_to_ARGB(argb());
        }
    }

    private record Palette(PaletteEntry[] entries, boolean cacheable, boolean hasTint) {
        static final Palette BLANK = new Palette(new PaletteEntry[]{new PaletteEntry(-1)}, false);

        Palette(PaletteEntry[] entries, boolean cacheable) {
            this(entries, cacheable, false);
        }

        PaletteEntry getRandomEntry() {
            return entries()[MathHelper.randomBetween(0, entries().length - 1)];
        }

        int[] getRandomColour(int[] tintColour) {
            if(this.hasTint()) {
                return ColourUtil.multiplyColours(this.getRandomEntry().argbAsArray(), tintColour);
            }
            return this.getRandomEntry().argbAsArray();
        }
    }

    public record FluidStateMaterialSource(Function<FluidModel, TextureAtlasSprite> converter) {
        public static final FluidStateMaterialSource STILL = new FluidStateMaterialSource(fluidModel ->
            fluidModel.stillMaterial().sprite()
        );

        public static final FluidStateMaterialSource FLOWING = new FluidStateMaterialSource(fluidModel ->
            fluidModel.flowingMaterial().sprite()
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
