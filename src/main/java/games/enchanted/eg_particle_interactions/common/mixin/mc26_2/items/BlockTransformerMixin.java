//? if minecraft: >= 26.3 {
package games.enchanted.eg_particle_interactions.common.mixin.mc26_2.items;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockTransformer.class)
public class BlockTransformerMixin {
    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"),
        method =
            //? if fabric {
            "transformBlock"
            //? } else {
            /*"transformBlock(Lnet/minecraft/world/item/context/UseOnContext;Lnet/minecraft/core/Holder;)Lnet/minecraft/world/InteractionResult;"
            *///? }
    )
    private void displayTransformationParticles(
        Level level,
        Entity except,
        BlockPos pos,
        SoundEvent sound,
        SoundSource source,
        float volume,
        float pitch,
        Operation<Void> original,
        UseOnContext context,
        //? if neoforge {
        /*@Nullable Holder<BlockTransformer> transformer,
        *///? }
        @Local(name = "oldBlockState") BlockState oldBlockState
    ) {
        original.call(level, except, pos, sound, source, volume, pitch);
        if(!(level instanceof ClientLevel clientLevel)) {
            return;
        }

        BlockState newBlockState = clientLevel.getBlockState(pos);
        ItemStack interactionStack = context.getItemInHand();

        if(interactionStack.is(ItemTags.AXES)) {
            SpawnParticles.spawnAxeStripParticle(clientLevel, pos, oldBlockState, newBlockState, context);
        } else if(interactionStack.is(ItemTags.HOES)) {
            SpawnParticles.spawnHoeTillParticle(clientLevel, pos, oldBlockState, context);
        } else if(interactionStack.is(ItemTags.SHOVELS)) {
            SpawnParticles.spawnShovelFlattenParticle(clientLevel, pos, oldBlockState, context);
        }
    }
}
//? }
