package games.enchanted.blockplaceparticles.mixin;

import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow @Nullable private ClientLevel level;

    @Inject(
        at = @At("HEAD"),
        method = "levelEvent"
    )
    public void particleInteractionsLevelEventHandler(int type, BlockPos pos, int data, CallbackInfo ci) {
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