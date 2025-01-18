package games.enchanted.blockplaceparticles.mixin.blocks;

import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class SeagrassBlock_BlockMixin extends BlockBehaviour {
    protected SeagrassBlock_BlockMixin(Properties p_51021_) {
        super(p_51021_);
    }

    @Inject(
        at = @At("HEAD"),
        method = "animateTick"
    )
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if(!(level instanceof ClientLevel clientLevel)) return;
        if((Object) this instanceof net.minecraft.world.level.block.SeagrassBlock || (Object) this instanceof TallSeagrassBlock) {
            SpawnParticles.spawnRandomUnderwaterBubbleStreams(clientLevel, pos, state);
        }
    }
}