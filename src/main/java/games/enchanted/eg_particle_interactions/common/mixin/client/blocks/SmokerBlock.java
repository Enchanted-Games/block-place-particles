package games.enchanted.eg_particle_interactions.common.mixin.client.blocks;

import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
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
    protected SmokerBlock(Properties properties) {
        super(properties);
    }

    @Inject(
        at = @At("HEAD"),
        method = "animateTick"
    )
    public void eg_particle_interactions$spawnAdditionalParticles(BlockState smokerState, Level level, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {
        if(!smokerState.getValue(AbstractFurnaceBlock.LIT)) return;
        if(level instanceof ClientLevel clientLevel) {
            SpawnParticles.spawnSmokerSmokeParticles(clientLevel, blockPos);
        }
    }
}