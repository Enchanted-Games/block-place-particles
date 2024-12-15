package games.enchanted.blockplaceparticles.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.blockplaceparticles.ParticleInteractionsLogging;
import games.enchanted.blockplaceparticles.config.ConfigHandler;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import games.enchanted.blockplaceparticles.particle_spawning.override.BlockParticleOverride;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientLevel.class)
public abstract class FallingBlockEntityLand_ClientLevelMixin extends Level {
    protected FallingBlockEntityLand_ClientLevelMixin(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeHolder, boolean isClientSide, boolean isDebug, long p_270248_, int p_270466_) {
        super(writableLevelData, resourceKey, registryAccess, dimensionTypeHolder, isClientSide, isDebug, p_270248_, p_270466_);
    }

    @SuppressWarnings("UnreachableCode")
    @WrapOperation(
        method = "removeEntity",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/entity/LevelEntityGetter;get(I)Lnet/minecraft/world/level/entity/EntityAccess;")
    )
    public EntityAccess removeEntity(LevelEntityGetter instance, int i, Operation<EntityAccess> original) {
        EntityAccess entity = original.call(instance, i);
        if(!(entity instanceof FallingBlockEntity fallingBlockEntity)) {
            // not a falling block so return early
            return original.call(instance, i);
        }

        if(Minecraft.getInstance().player != null && fallingBlockEntity.distanceTo(Minecraft.getInstance().player) > ConfigHandler.fallingBlockEffect_renderDistance) {
            return original.call(instance, i);
        }
        if(fallingBlockEntity.getBlockState().getBlock() instanceof BrushableBlock) {
            return original.call(instance, i);
        }

        BlockState fallingState = fallingBlockEntity.getBlockState();
        BlockPos fallingBlockPos = fallingBlockEntity.blockPosition();

        SpawnParticles.spawnFallingBlockLandParticles((ClientLevel) (Object) this, fallingState, fallingBlockEntity.getX(), fallingBlockEntity.getY(), fallingBlockEntity.getZ(), fallingBlockEntity.getDeltaMovement());

        ParticleInteractionsLogging.debugInfo("Falling block ({}) was removed at {}", fallingBlockEntity, fallingBlockPos);
        return original.call(instance, i);
    }
}
