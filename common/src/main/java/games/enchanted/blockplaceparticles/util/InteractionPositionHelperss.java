package games.enchanted.blockplaceparticles.util;


import net.minecraft.core.Direction;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;

public class InteractionPositionHelperss {
    public static float[] getRedstoneRepeaterInteractionPos(BlockState repeaterState) {
        Direction repeaterDirection = repeaterState.getValue(RepeaterBlock.FACING);
        int repeaterDelay = repeaterState.getValue(RepeaterBlock.DELAY);

        float[][] positions;
        final float height = 0.4f;
        switch (repeaterDirection) {
            case NORTH -> positions = new float[][]{{0.5f, height, 9/16f}, {0.5f, height, 7/16f}, {0.5f, height, 5/16f}, {0.5f, height, 3/16f}};
            case EAST ->  positions = new float[][]{{7/16f, height, 0.5f}, {9/16f, height, 0.5f}, {11/16f, height, 0.5f}, {13/16f, height, 0.5f}};
            case SOUTH -> positions = new float[][]{{0.5f, height, 7/16f}, {0.5f, height, 9/16f}, {0.5f, height, 11/16f}, {0.5f, height, 13/16f}};
            case WEST ->  positions = new float[][]{{9/16f, height, 0.5f}, {7/16f, height, 0.5f}, {5/16f, height, 0.5f}, {3/16f, height, 0.5f}};
            default ->    positions = new float[][]{{0f, height, 0f}, {0f, height, 0f}, {0f, height, 0f}, {0f, height, 0f}};
        }

        if(repeaterDelay - 1 < 4 && repeaterDelay - 1 >= 0) {
            return positions[repeaterDelay - 1];
        }
        return new float[]{0f, height, 0f};
    }

    public static float[] getRedstoneComparatorInteractionPos(BlockState comparatorState) {
        Direction comparatorDirection = comparatorState.getValue(ComparatorBlock.FACING);

        float[] position;
        final float height = 0.4f;
        switch (comparatorDirection) {
            case NORTH -> position = new float[]{0.5f, height, 13/16f};
            case EAST ->  position = new float[]{3/16f, height, 0.5f};
            case SOUTH -> position = new float[]{0.5f, height, 3/16f};
            case WEST ->  position = new float[]{13/16f, height, 0.5f};
            default ->    position = new float[]{0f, height, 0f};
        }

        return position;
    }
}
