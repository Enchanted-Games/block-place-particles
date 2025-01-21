package games.enchanted.blockplaceparticles.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class LocalPlayer_EntityMixin {
    @Shadow public abstract double getX();
    @Shadow public abstract Level level();
    @Shadow public abstract Vec3 getDeltaMovement();

    @Shadow public abstract double getY();

    @Shadow public abstract double getZ();

    @Unique private int block_place_particle$ticksUntilNextBlockDisturbance = 0;

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;entityInside(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)V"),
        method = "checkInsideBlocks(Ljava/util/List;Ljava/util/Set;)V"
    )
    protected void trySpawnParticlesWhenPlayerInsideBlock(BlockState insideBlockState, Level level, BlockPos insideBlockPos, Entity entity, Operation<Void> original) {
        original.call(insideBlockState, level, insideBlockPos, entity);

        if(
            block_place_particle$ticksUntilNextBlockDisturbance > 0
            ||
            // check if entity is player
            !((Object) entity instanceof Player player)
            ||
            // check if player's level is client side
            !(player.level() instanceof ClientLevel clientLevel)
        ) {
            --block_place_particle$ticksUntilNextBlockDisturbance;
            return;
        }

        block_place_particle$ticksUntilNextBlockDisturbance = MathHelpers.randomBetween(3, 10);
        BlockPos verticalOffsetBlockPos = BlockPos.containing(insideBlockPos.getX(), this.getY() + 0.1, insideBlockPos.getZ());
        SpawnParticles.spawnBlockDisturbanceParticles(clientLevel, verticalOffsetBlockPos, level.getBlockState(verticalOffsetBlockPos), this.getX(), this.getY(), this.getZ(), this.getDeltaMovement(), player.isSprinting());
    }
}
