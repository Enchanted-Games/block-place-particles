package games.enchanted.eg_particle_interactions.common.mixin.client.resource;

import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class RegisterTextureAtlases_MinecraftMixin {
    @Shadow @Final private TextureManager textureManager;

    @Inject(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/MapTextureManager;<init>(Lnet/minecraft/client/renderer/texture/TextureManager;)V"),
        method = "<init>"
    )
    private void eg_particle_interactions$registerTextureAtlases(CallbackInfo ci) {
        //? if fabric {
        ParticleInteractionsMod.registerAtlases(textureManager);
        //?}
    }
}
