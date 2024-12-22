package games.enchanted.blockplaceparticles.util;


import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;

public class InteractionPositionHelperss {
    public static float[] getRedstoneRepeaterInteractionPos(BlockState repeaterState) {
        Direction repeaterDirection = repeaterState.getValue(RepeaterBlock.FACING);
        int repeaterDelay = repeaterState.getValue(RepeaterBlock.DELAY);

        float[][] positions;
        final float height = 0.4f;
        switch (repeaterDirection) {
            case NORTH -> {
                positions = new float[][]{{0.5f, height, 9/16f}, {0.5f, height, 7/16f}, {0.5f, height, 5/16f}, {0.5f, height, 3/16f}};
            }
            default -> {
                positions = new float[][]{{0f, height, 0f}, {0f, height, 0f}, {0f, height, 0f}, {0f, height, 0f}};
            }
        }

        return positions[repeaterDelay - 1];
    }
}
