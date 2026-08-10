package games.enchanted.eg_particle_interactions.common.mixin.client;

import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftInitFinishedMixin {
    @Inject(
        at = @At("TAIL"),
        method = "<init>"
    )
    private static void eg_particle_interactions$onClientInitFinish(GameConfig gameConfig, CallbackInfo ci) {
        ParticleInteractionsMod.clientInitFinished();
    }
}
