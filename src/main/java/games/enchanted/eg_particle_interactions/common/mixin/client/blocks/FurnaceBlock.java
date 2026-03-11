package games.enchanted.eg_particle_interactions.common.mixin.client.blocks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.config.categories.BlockInteractionOptions;
import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
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
    protected FurnaceBlock(Properties properties) {
        super(properties);
    }

    @Inject(
        at = @At("TAIL"),
        method = "animateTick"
    )
    private void eg_particle_interactions$spawnAdditionalParticles(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {
        if (!(level instanceof ClientLevel clientLevel)) return;
        if (blockState.getValue(LIT)) {
            SpawnParticles.spawnAdditionalFurnaceParticles(clientLevel, blockPos, blockState);
        }
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"),
        method = "animateTick"
    )
    private void eg_particle_interactions$conditionallySkipSpawningVanillaParticles(Level level, ParticleOptions particleOptions, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original) {
        if(!BlockInteractionOptions.VANILLA_FURNACE_PARTICLES_ENABLED.getValue()) return;
        original.call(level, particleOptions, x, y, z, xSpeed, ySpeed, zSpeed);
    }
}