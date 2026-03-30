package games.enchanted.eg_particle_interactions.common.mixin.client.items;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    @WrapOperation(
        at = @At(
            value = "INVOKE",
            target =
                //? if fabric {
                "Lnet/minecraft/world/item/AxeItem;evaluateNewBlockState(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/state/BlockState;)Ljava/util/Optional;"
                //? } else {
                /*"Lnet/minecraft/world/item/AxeItem;evaluateNewBlockState(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/item/context/UseOnContext;)Ljava/util/Optional;"
                *///? }
        ),
        method = "useOn"
    )
    private Optional<BlockState> eg_particle_interactions$trySpawnAxeStripParticles(
        AxeItem instance,
        Level level,
        BlockPos pos,
        @Nullable Player player,
        BlockState oldState,
        //? if fabric {
        Operation<Optional<BlockState>> original,
        UseOnContext context
        //? } else {
        /*UseOnContext context,
        Operation<Optional<BlockState>> original
        *///? }
    ) {
        Optional<BlockState> newState = original.call(
            instance,
            level,
            pos,
            player,
            oldState
            //? if neoforge {
            /*, context
            *///? }
        );
        if(!(level instanceof ClientLevel clientLevel)) return newState;
        if(newState.isEmpty()) return newState;

        SpawnParticles.spawnAxeStripParticle(clientLevel, pos, oldState, newState.get(), context);
        return newState;
    }
}
