package games.enchanted.eg_particle_interactions.common.mixin.blocks;

import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeehiveBlock.class)
public abstract class BeehiveBlockMixin extends BaseEntityBlock {
    protected BeehiveBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(
        at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;BOTTLE_FILL:Lnet/minecraft/sounds/SoundEvent;", opcode = Opcodes.GETSTATIC),
        method = "useItemOn"
    )
    private void block_place_particle$spawnHoneyCollectionParticles(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if(!(level instanceof ClientLevel clientLevel)) return;
        SpawnParticles.spawnHoneyCollectionParticlesOnPlayer(clientLevel, player);
        Direction hitFace = hitResult.getDirection();
        BlockPos hitPos = hitResult.getBlockPos();
        SpawnParticles.spawnHoneyCollectionParticles(
            clientLevel,
            hitPos.getX() + 0.5f + (hitFace.getStepX() * 0.55),
            hitPos.getY() + 0.5f + (hitFace.getStepY() * 0.55),
            hitPos.getZ() + 0.5f + (hitFace.getStepZ() * 0.55),
            hitFace
        );
    }
}
