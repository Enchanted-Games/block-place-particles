package games.enchanted.eg_particle_interactions.common.mixin.blocks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.config.categories.ItemInteractionOptions;
import games.enchanted.eg_particle_interactions.common.particle.option.DripParticleOption;
import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
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

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"),
        method = "spawnFluidParticle"
    )
    private void block_place_particles$replaceHoneyDropParticles(Level instance, ParticleOptions particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Operation<Void> original) {
        if(!ItemInteractionOptions.HONEY_COLLECTION_REPLACE_VANILLA.getValue()) {
            original.call(instance, particleData, x, y, z, xSpeed, ySpeed, zSpeed);
            return;
        }
        original.call(instance, DripParticleOption.HANGING_HONEY_DROP, x, y + 0.047, z, xSpeed, ySpeed, zSpeed);
    }
}
