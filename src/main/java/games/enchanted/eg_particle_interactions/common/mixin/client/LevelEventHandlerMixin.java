package games.enchanted.eg_particle_interactions.common.mixin.client;

import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelEventHandler.class)
public abstract class LevelEventHandlerMixin {
    @Shadow @Final private ClientLevel level;

    @Inject(
        at = @At("HEAD"),
        method = "levelEvent"
    )
    public void eg_particle_interactions$particleInteractionsLevelEventHandler(int type, BlockPos pos, int data, CallbackInfo ci) {
        if(level == null) return;
        switch (type) {
            case 1030:
                SpawnParticles.spawnAnvilUseSparkParticles(this.level, pos);
                break;
            case 1042:
                SpawnParticles.spawnGrindstoneUseSparkParticles(this.level, pos);
                break;
        }
    }
}