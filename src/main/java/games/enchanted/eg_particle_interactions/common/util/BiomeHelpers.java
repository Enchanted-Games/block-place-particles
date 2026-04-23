package games.enchanted.eg_particle_interactions.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;

public class BiomeHelpers {
    public static boolean isWarmDimension(DimensionType dimensionType) {
        return dimensionType.attributes().contains(EnvironmentAttributes.WATER_EVAPORATES);
    }

    public static Holder<Biome> getBiomeAtPosition(Level level, BlockPos pos) {
        return level.getBiome(pos);
    }
}
