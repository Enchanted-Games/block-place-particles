package games.enchanted.blockplaceparticles.mixin.blocks;

import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import games.enchanted.blockplaceparticles.util.InteractionPositionHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.level.block.RepeaterBlock.class)
public abstract class RepeaterBlock extends DiodeBlock {
    public RepeaterBlock(Properties properties) {
        super(properties);
    }

    @Inject(
        at = @At(shift = At.Shift.AFTER, value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"),
        method = "useWithoutItem"
    )
    protected void spawnDustParticlesOnInteraction(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if(!(level instanceof ClientLevel)) return;
        float[] interactionPos = InteractionPositionHelpers.getRedstoneRepeaterInteractionPos(level.getBlockState(pos));
        SpawnParticles.spawnRedstoneInteractionParticles(
            (ClientLevel) level,
            level.getBlockState(pos),
            pos.getX() + interactionPos[0],
            pos.getY() + interactionPos[1],
            pos.getZ() + interactionPos[2],
            0.15f,
            0.15f,
            0.15f
        );
    }
}