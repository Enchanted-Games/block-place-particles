package games.enchanted.blockplaceparticles.mixin.blocks;

import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RedStoneWireBlock.class)
public abstract class RedstoneWireBlock extends Block {
    public RedstoneWireBlock(Properties properties) {
        super(properties);
    }

    @Inject(
        at = @At("TAIL"),
        method = "updatesOnShapeChange(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;)V"
    )
    private void spawnDustParticlesOnInteraction(Level level, BlockPos pos, BlockState oldState, BlockState newState, CallbackInfo ci) {
        if(!(level instanceof ClientLevel)) return;
        SpawnParticles.spawnRedstoneInteractionParticles(
            (ClientLevel) level,
            pos,
            newState,
            pos.getX() + (level.random.nextFloat() / 2) + 0.5f,
            pos.getY() + 0.1f,
            pos.getZ() + (level.random.nextFloat() / 2) + 0.5f
        );
    }
}
