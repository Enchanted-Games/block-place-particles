package games.enchanted.blockplaceparticles.mixin.blocks;

import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.level.block.SmokerBlock.class)
public abstract class SmokerBlock extends AbstractFurnaceBlock {
    protected SmokerBlock(Properties p_48687_) {
        super(p_48687_);
    }

    @Inject(
        at = @At("HEAD"),
        method = "animateTick"
    )
    public void animateTick(BlockState smokerState, Level level, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {
        if(!smokerState.getValue(AbstractFurnaceBlock.LIT)) return;
        if(level instanceof ClientLevel clientLevel) {
            SpawnParticles.spawnSmokerSmokeParticles(clientLevel, blockPos);
        }
    }
}