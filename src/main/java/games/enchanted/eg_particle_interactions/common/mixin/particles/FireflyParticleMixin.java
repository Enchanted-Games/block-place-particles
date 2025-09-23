package games.enchanted.eg_particle_interactions.common.mixin.particles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.duck.ParticleAccess;
import games.enchanted.eg_particle_interactions.common.particle_override.BlockParticleOverrides;
import games.enchanted.eg_particle_interactions.common.registry.BlockOrTagLocation;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FireflyParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Mixin(FireflyParticle.class)
public abstract class FireflyParticleMixin extends TextureSheetParticle {
    protected FireflyParticleMixin(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Inject(
        at = @At("TAIL"),
        method = "<init>"
    )
    private void block_place_particle$makeFirefliesNotGetStuckOnStuff(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, CallbackInfo ci) {
        ((ParticleAccess) this).eg_particle_interactions$setBypassMovementCollisionCheck(true);
    }

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
