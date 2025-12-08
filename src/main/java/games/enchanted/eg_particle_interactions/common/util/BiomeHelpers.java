package games.enchanted.eg_particle_interactions.common.util;

import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.ArrayList;

public class BiomeHelpers {
    static ArrayList<Biome> SWAMPY_BIOMES = new ArrayList<>();

    public static boolean isWarmBiomeOrDimension(Level level, BlockPos blockPos) {
        final Biome biome = level.getBiome(blockPos).value();
        return !biome.hasPrecipitation() || isWarmDimension(level.dimensionType());
    }

    public static boolean isWarmDimension(DimensionType dimensionType) {
        return dimensionType.attributes().contains(EnvironmentAttributes.WATER_EVAPORATES);
    }

    public static boolean isSwampyBiome(Level level, BlockPos blockPos) {
        final Biome biome = level.getBiome(blockPos).value();
        if(SWAMPY_BIOMES.contains(biome)) return true;
        boolean isSwampyBiome = RegistryHelpers.isBiomeInTag(RegistryHelpers.getLocationFromBiome(biome), RegistryHelpers.getBiomeTagKey(Identifier.fromNamespaceAndPath("c", "is_swamp")));
        if(isSwampyBiome) SWAMPY_BIOMES.add(biome);
        return isSwampyBiome;
    }
}
