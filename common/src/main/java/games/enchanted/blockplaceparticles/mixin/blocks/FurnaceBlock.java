package games.enchanted.blockplaceparticles.mixin.blocks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.blockplaceparticles.config.ConfigHandler;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.level.block.FurnaceBlock.class)
public abstract class FurnaceBlock extends AbstractFurnaceBlock {
    protected FurnaceBlock(Properties p_48687_) {
        super(p_48687_);
    }

    @Inject(
        at = @At("TAIL"),
        method = "animateTick"
    )
    private void spawnAdditionalParticles(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {
        if (!(level instanceof ClientLevel clientLevel)) return;
        if (blockState.getValue(LIT)) {
            SpawnParticles.spawnAdditionalFurnaceParticles(clientLevel, blockPos, blockState);
        }
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"),
        method = "animateTick"
    )
    private void conditionallySkipSpawningVanillaParticles(Level level, ParticleOptions particleOptions, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original) {
        if(!ConfigHandler.furnaceVanillaParticles_enabled) return;
        original.call(level, particleOptions, x, y, z, xSpeed, ySpeed, zSpeed);
    }
}