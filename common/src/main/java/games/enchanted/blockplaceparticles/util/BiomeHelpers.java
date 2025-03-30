package games.enchanted.blockplaceparticles.util;

import games.enchanted.blockplaceparticles.registry.RegistryHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;

public class BiomeHelpers {
    static ArrayList<Biome> SWAMPY_BIOMES = new ArrayList<>();

    public static boolean isWarmBiomeOrDimension(Level level, BlockPos blockPos) {
        final Biome biome = level.getBiome(blockPos).value();
        return !biome.hasPrecipitation() || level.dimensionType().ultraWarm();
    }

    public static boolean isSwampyBiome(Level level, BlockPos blockPos) {
        final Biome biome = level.getBiome(blockPos).value();
        if(SWAMPY_BIOMES.contains(biome)) return true;
        boolean isSwampyBiome = RegistryHelpers.isBiomeInTag(RegistryHelpers.getLocationFromBiome(biome), RegistryHelpers.getBiomeTagKey(ResourceLocation.fromNamespaceAndPath("c", "is_swamp")));
        if(isSwampyBiome) SWAMPY_BIOMES.add(biome);
        return isSwampyBiome;
    }
}
