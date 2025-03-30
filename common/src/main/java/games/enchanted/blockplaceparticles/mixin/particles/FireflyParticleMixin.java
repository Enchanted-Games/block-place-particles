package games.enchanted.blockplaceparticles.mixin.particles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.blockplaceparticles.particle_override.BlockParticleOverrides;
import games.enchanted.blockplaceparticles.registry.BlockOrTagLocation;
import games.enchanted.blockplaceparticles.registry.RegistryHelpers;
import net.minecraft.client.particle.FireflyParticle;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.function.Supplier;

@Mixin(FireflyParticle.class)
public class FireflyParticleMixin {
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"),
        method = "tick"
    )
    public boolean block_place_particle$makeFirefliesNotDieInFireflyBushes(BlockState state, Operation<Boolean> original) {
        Supplier<List<BlockOrTagLocation>> fireflyOverrideBlocks = BlockParticleOverrides.FIREFLY.getSupportedBlockResourceLocations_getter();
        Supplier<List<BlockOrTagLocation>> grassBladeOverrideBlocks = BlockParticleOverrides.GRASS_BLADE.getSupportedBlockResourceLocations_getter();
        if(fireflyOverrideBlocks == null || grassBladeOverrideBlocks == null) {
            return original.call(state);
        }
        if(
            fireflyOverrideBlocks.get().contains(new BlockOrTagLocation(RegistryHelpers.getLocationFromBlock(state.getBlock()), false)) ||
            grassBladeOverrideBlocks.get().contains(new BlockOrTagLocation(RegistryHelpers.getLocationFromBlock(state.getBlock()), false)))
        {
            return original.call(Blocks.AIR.defaultBlockState());
        }
        return original.call(state);
    }
}
