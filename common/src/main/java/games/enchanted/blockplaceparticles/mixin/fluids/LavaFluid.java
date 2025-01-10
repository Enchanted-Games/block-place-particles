package games.enchanted.blockplaceparticles.mixin.fluids;

import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.level.material.LavaFluid.class)
public class LavaFluid {
    @Inject(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"),
        method = "animateTick"
    )
    public void spawnBubblePoppingParticles(Level level, BlockPos blockPos, FluidState fluidState, RandomSource randomSource, CallbackInfo ci) {
        if(!(level instanceof ClientLevel clientLevel)) return;
        SpawnParticles.spawnLavaBubblePopParticles(clientLevel, blockPos, fluidState);
    }
}
