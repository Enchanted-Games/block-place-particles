package games.enchanted.blockplaceparticles.util;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;

public class FluidHelpers {
    public static boolean isSurroundedByWater(Level level, BlockPos blockPos, int minWaterBlocks) {
        return trueBooleans(
            minWaterBlocks,
            level.getFluidState(blockPos.above()).is(FluidTags.WATER),
            level.getFluidState(blockPos.below()).is(FluidTags.WATER),
            level.getFluidState(blockPos.north()).is(FluidTags.WATER),
            level.getFluidState(blockPos.east()).is(FluidTags.WATER),
            level.getFluidState(blockPos.south()).is(FluidTags.WATER),
            level.getFluidState(blockPos.west()).is(FluidTags.WATER)
        );
    }

    public static boolean probablyPlacedUnderwater(Level level, BlockPos blockPos) {
        boolean isWaterAtNorth = level.getFluidState(blockPos.north()).is(FluidTags.WATER);
        boolean isWaterAtEast = level.getFluidState(blockPos.east()).is(FluidTags.WATER);
        boolean isWaterAtSouth = level.getFluidState(blockPos.south()).is(FluidTags.WATER);
        boolean isWaterAtWest = level.getFluidState(blockPos.west()).is(FluidTags.WATER);
        boolean isWaterAbove = level.getFluidState(blockPos.above()).is(FluidTags.WATER);
        boolean isWaterBelow = level.getFluidState(blockPos.below()).is(FluidTags.WATER);

        boolean oneOrMoreWaterSurrounding = trueBooleans(
            1,
            isWaterAtNorth,
            isWaterAtEast,
            isWaterAtSouth,
            isWaterAtWest
        );
        boolean threeOrMoreWaterSurrounding = trueBooleans(
            3,
            isWaterAtNorth,
            isWaterAtEast,
            isWaterAtSouth,
            isWaterAtWest
        );
        return ((isWaterAbove || isWaterBelow) && oneOrMoreWaterSurrounding) || (!isWaterAbove && threeOrMoreWaterSurrounding);
    }

    static boolean trueBooleans(int howMany, boolean ... bools) {
        int total = 0;

        for (boolean b:bools) {
            if (b && (++total == howMany)) {
                return true;
            }
        }

        return false;
    }
}
