package games.enchanted.eg_particle_interactions.common.mixin.client.particles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.config.categories.ItemInteractionOptions;
import games.enchanted.eg_particle_interactions.common.override_system.OverridePreset;
import games.enchanted.eg_particle_interactions.common.override_system.ParticleOrigin;
import games.enchanted.eg_particle_interactions.common.override_system.override.BlockOverrideManager;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverride;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverrides;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ParticleUtils.class)
public class ParticleUtilsMixin {
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"),
        method = "spawnSmashAttackParticles"
    )
    private static void eg_particle_interactions$conditionallyReplaceSmashParticles(LevelAccessor levelAccessor, ParticleOptions options, final double x, final double y, final double z, final double xSpeed, final double ySpeed, final double zSpeed, Operation<Void> original) {
        if(!(options instanceof BlockParticleOption blockParticleOption) || !(levelAccessor instanceof ClientLevel clientLevel)) {
            original.call(levelAccessor, options, x, y, z, xSpeed, ySpeed, zSpeed);
            return;
        }

        if(options.getType() != ParticleTypes.DUST_PILLAR) {
            original.call(levelAccessor, options, x, y, z, xSpeed, ySpeed, zSpeed);
            return;
        }

        BlockPos particleBlockPos = BlockPos.containing(x, y, z);
        BlockState blockState = blockParticleOption.getState();
        BlockState belowParticleState = clientLevel.getBlockState(particleBlockPos.below());
        if(!belowParticleState.isAir() && ItemInteractionOptions.ACCURATE_MACE_SMASH_ENABLED.getValue()) {
            blockState = belowParticleState;
        }

        ParticleOrigin origin = ParticleOrigin.BLOCK_MACE_SMASH;
        OverridePreset overridePreset = BlockOverrideManager.getForBlock(blockState, origin);
        ParticleOverride override = overridePreset.getRandom();
        Identifier id = ParticleOverrides.getIdOrThrow(override);
        boolean isVanilla = id.equals(ParticleOverrides.VANILLA_OVERRIDE_ID);

        double newYSpeed = ySpeed + clientLevel.getRandom().nextGaussian() * (isVanilla ? 1.0 : 3.5);
        boolean closeToSurface = Mth.frac(y) < 0.01;

        ParticleContext context = ParticleContext.block(clientLevel, blockState, particleBlockPos);

        override.spawnParticle(
            origin,
            context,
            x,
            closeToSurface ? y + 0.1 : y,
            z,
            xSpeed,
            newYSpeed,
            zSpeed
        );
    }
}
