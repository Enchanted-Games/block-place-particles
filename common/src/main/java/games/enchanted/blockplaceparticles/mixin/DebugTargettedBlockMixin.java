package games.enchanted.blockplaceparticles.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LevelRenderer.class)
public abstract class DebugTargettedBlockMixin {
    @Shadow @Final private Minecraft minecraft;

    @Shadow private ClientLevel level;

    @Unique long block_place_particle$previousGameTime;

    @Inject(
        at = @At("HEAD"),
        method = "renderBlockOutline"
    )
    public void renderBlockOutline(Camera camera, MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, boolean bl, CallbackInfo ci) {
        HitResult hitResult = this.minecraft.hitResult;
        if (!(hitResult instanceof BlockHitResult)) {
            return;
        }
        if (hitResult.getType() == HitResult.Type.MISS) {
            return;
        }

        assert this.level != null;
        assert this.minecraft.player != null;

        if(this.minecraft.player.input.keyPresses.sprint() && this.level.getGameTime() != block_place_particle$previousGameTime) {
            SpawnParticles.spawnAnvilUseSparkParticles(level, ((BlockHitResult) hitResult).getBlockPos());
        }

        block_place_particle$previousGameTime = this.level.getGameTime();
    }
}