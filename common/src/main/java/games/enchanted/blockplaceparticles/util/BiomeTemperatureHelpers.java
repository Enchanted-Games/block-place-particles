package games.enchanted.blockplaceparticles.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class BiomeTemperatureHelpers {
    public static boolean isWarmBiomeOrDimension(Level level, BlockPos blockPos) {
        final Biome biome = level.getBiome(blockPos).value();
        return !biome.hasPrecipitation() || level.dimensionType().ultraWarm();
    }
}
