package games.enchanted.blockplaceparticles.particle_spawning;


import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ParticlePositionHelpers {
    private static final float[][][] repeaterPositions = {
            new float[][]{{0.5f, 9/16f}, {0.5f, 7/16f}, {0.5f, 5/16f}, {0.5f, 3/16f}},
            new float[][]{{7/16f, 0.5f}, {9/16f, 0.5f}, {11/16f, 0.5f}, {13/16f, 0.5f}},
            new float[][]{{0.5f, 7/16f}, {0.5f, 9/16f}, {0.5f, 11/16f}, {0.5f, 13/16f}},
            new float[][]{{9/16f, 0.5f}, {7/16f, 0.5f}, {5/16f, 0.5f}, {3/16f, 0.5f}},
            new float[][]{{0f, 0f}, {0f, 0f}, {0f, 0f}, {0f, 0f}}
    };
    public static float[] getRedstoneRepeaterInteractionPos(BlockState repeaterState) {
        Direction repeaterDirection = repeaterState.getValue(RepeaterBlock.FACING);
        int repeaterDelay = repeaterState.getValue(RepeaterBlock.DELAY);

        int positionsIndex;
        switch (repeaterDirection) {
            case NORTH -> positionsIndex = 0;
            case EAST ->  positionsIndex = 1;
            case SOUTH -> positionsIndex = 2;
            case WEST ->  positionsIndex = 3;
            default ->    positionsIndex = 4;
        }

        final float height = 0.4f;
        if(repeaterDelay - 1 < 4 && repeaterDelay - 1 >= 0) {
            float[] xyPos = repeaterPositions[positionsIndex][repeaterDelay - 1];
            return new float[]{xyPos[0], height, xyPos[1]};
        }

        return new float[]{0f, height, 0f};
    }

    private static final float[][] comparatorPositions = {
        new float[]{0.5f, 13/16f},
        new float[]{3/16f, 0.5f},
        new float[]{0.5f, 3/16f},
        new float[]{13/16f, 0.5f},
        new float[]{0f, 0f}
    };
    public static float[] getRedstoneComparatorInteractionPos(BlockState comparatorState) {
        Direction comparatorDirection = comparatorState.getValue(ComparatorBlock.FACING);

        int positionsIndex;
        switch (comparatorDirection) {
            case NORTH -> positionsIndex = 0;
            case EAST ->  positionsIndex = 1;
            case SOUTH -> positionsIndex = 2;
            case WEST ->  positionsIndex = 3;
            default ->    positionsIndex = 4;
        }

        final float height = 0.4f;
        float[] xyPos = comparatorPositions[positionsIndex];
        return new float[]{xyPos[0], height, xyPos[1]};
    }

    public static double[] getRandomFurnaceParticlePosition(BlockPos blockPos, BlockState furnaceState) {
        Direction direction = furnaceState.getValue(AbstractFurnaceBlock.FACING);
        Direction.Axis direction$axis = direction.getAxis();
        double baseX = blockPos.getX() + 0.5;
        double baseY = blockPos.getY();
        double baseZ = blockPos.getZ() + 0.5;

        double outwardOffset = 0.52;
        double randomOffset = MathHelpers.randomBetween(-0.3f, 0.3f);
        double offsetX = direction$axis == Direction.Axis.X ? direction.getStepX() * outwardOffset : randomOffset;
        double offsetY = MathHelpers.randomBetween(0, 0.375f);
        double offsetZ = direction$axis == Direction.Axis.Z ? direction.getStepZ() * outwardOffset : randomOffset;

        return new double[]{baseX + offsetX, baseY + offsetY, baseZ + offsetZ};
    }
}
