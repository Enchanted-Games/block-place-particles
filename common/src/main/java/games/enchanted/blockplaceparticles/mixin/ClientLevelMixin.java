package games.enchanted.blockplaceparticles.mixin;

import games.enchanted.blockplaceparticles.ParticleInteractionsLogging;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {
    protected ClientLevelMixin(WritableLevelData pLevelData, ResourceKey<Level> pDimension, RegistryAccess pRegistryAccess, Holder<DimensionType> pDimensionTypeRegistration, Supplier<ProfilerFiller> pProfiler, boolean pIsClientSide, boolean pIsDebug, long pBiomeZoomSeed, int pMaxChainedNeighborUpdates) {
        super(pLevelData, pDimension, pRegistryAccess, pDimensionTypeRegistration, pProfiler, pIsClientSide, pIsDebug, pBiomeZoomSeed, pMaxChainedNeighborUpdates);
    }

    @Inject(
        at = @At("HEAD"),
        method = "gameEvent(Lnet/minecraft/core/Holder;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)V"
    )
    public void gameEvent(Holder<GameEvent> eventHolder, Vec3 pos, GameEvent.Context context, CallbackInfo ci) {
        if(eventHolder.value().equals(GameEvent.BLOCK_PLACE.value())) {
            BlockPos blockPos = BlockPos.containing(pos);
            SpawnParticles.spawnBlockPlaceParticle(this, blockPos);
            ParticleInteractionsLogging.debugInfo(context.affectedState() + " placed at " + blockPos.toShortString() + " from '" + this + "'");
        };
    }
}
