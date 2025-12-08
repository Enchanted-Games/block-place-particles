package games.enchanted.eg_particle_interactions.common.mixin.client.blocks;

import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.BucketItemAccessor;
import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.level.block.AbstractCauldronBlock.class)
public abstract class AbstractCauldronBlock {
    @Inject(
        at = @At("TAIL"),
        method = "useItemOn(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;"
    )
    protected void spawnFluidOrBlockPlaceParticlesOnItemUse(ItemStack itemStack, BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if(!level.isClientSide()) return;
        InteractionResult result = cir.getReturnValue();
        Item usedItem = itemStack.getItem();
        if(result != InteractionResult.SUCCESS) return;
        if(usedItem instanceof BucketItem) {
            Fluid placedFluid = ((BucketItemAccessor) usedItem).block_place_particle$getContent();
            Logging.interactionDebugInfo("Bucket of " + RegistryHelpers.getLocationFromFluid(placedFluid) + " placed in a cauldron at " + pos.toShortString());
            SpawnParticles.spawnFluidPlacedParticle(level, pos, placedFluid);
        } else if(usedItem instanceof BlockItem) {
            BlockState placedState = ((BlockItem) usedItem).getBlock().defaultBlockState();
            Logging.interactionDebugInfo("Block '" + RegistryHelpers.getLocationFromBlock(placedState.getBlock()) + "' placed in a cauldron at " + pos.toShortString());
            SpawnParticles.spawnBlockPlaceParticle((ClientLevel) level, pos, placedState);
        }
    }
}
