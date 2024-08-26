package games.enchanted.blockplaceparticles.mixin;

import games.enchanted.blockplaceparticles.ParticleInteractionsLogging;
import games.enchanted.blockplaceparticles.mixin.accessor.BucketItemAccessor;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractCauldronBlock.class)
public abstract class AbstractCauldronBlockMixin {
    @Inject(
        at = @At("TAIL"),
        method = "useItemOn(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/ItemInteractionResult;"
    )
    protected void spawnFluidOrBlockPlaceParticlesOnItemUse(ItemStack itemStack, BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<ItemInteractionResult> cir) {
        if(!level.isClientSide()) return;
        ItemInteractionResult result = cir.getReturnValue();
        Item usedItem = itemStack.getItem();
        if(result != ItemInteractionResult.SUCCESS) return;
        if(usedItem instanceof BucketItem) {
            Fluid placedFluid = ((BucketItemAccessor) usedItem).getContent();
            ParticleInteractionsLogging.debugInfo("Bucket of " + placedFluid.builtInRegistryHolder().key().location() + " placed in a cauldron at " + pos.toShortString());
            SpawnParticles.spawnFluidPlacedParticle(level, pos, placedFluid);
        } else if(usedItem instanceof BlockItem) {
            BlockState placedState = ((BlockItem) usedItem).getBlock().defaultBlockState();
            ParticleInteractionsLogging.debugInfo("Block '" + placedState.getBlock().builtInRegistryHolder().key().location() + "' placed in a cauldron at " + pos.toShortString());
            SpawnParticles.spawnBlockPlaceParticle((ClientLevel) level, pos, placedState);
        }
    }
}
