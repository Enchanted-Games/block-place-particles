package games.enchanted.eg_particle_interactions.common.util;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.VoxelShape;

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

        for (boolean bool : bools) {
            if (bool && (++total == howMany)) {
                return true;
            }
        }
        return false;
    }

    public static FluidState fluidAtPosition(ClientLevel level, double x, double y, double z) {
        BlockPos blockPos = BlockPos.containing(x, y, z);
        FluidState fluidState = level.getFluidState(blockPos);
        double fluidBottom = blockPos.getY();
        double fluidTop = fluidBottom + fluidState.getHeight(level, blockPos);

        if(fluidBottom <= y && y <= fluidTop) {
            return fluidState;
        }

        return Fluids.EMPTY.defaultFluidState();
    }
}
