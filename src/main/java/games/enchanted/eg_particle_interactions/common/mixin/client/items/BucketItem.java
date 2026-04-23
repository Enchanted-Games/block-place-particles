package games.enchanted.eg_particle_interactions.common.mixin.client.items;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(net.minecraft.world.item.BucketItem.class)
public abstract class BucketItem {
    @WrapOperation(
        method =
            //? if fabric {
            "emptyContents"
            //? } else {
            /*"emptyContents(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/BlockHitResult;Lnet/minecraft/world/item/ItemStack;)Z"
            *///? }
        ,
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BucketItem;playEmptySound(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)V")
    )
    private void eg_particle_interactions$spawnFluidParticlesOnBucketEmpty(net.minecraft.world.item.BucketItem instance, LivingEntity user, LevelAccessor level, BlockPos pos, Operation<Void> original) {
        original.call(instance, user, level, pos);
        if(!(level instanceof ClientLevel clientLevel)) return;

        FluidState placedFluid = clientLevel.getFluidState(pos);
        SpawnParticles.spawnFluidPlacedParticle(clientLevel, pos, placedFluid);
    }
}
