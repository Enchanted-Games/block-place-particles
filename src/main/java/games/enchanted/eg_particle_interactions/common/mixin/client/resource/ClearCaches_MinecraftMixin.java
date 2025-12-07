package games.enchanted.eg_particle_interactions.common.mixin.client.resource;

import games.enchanted.eg_particle_interactions.common.resource.CacheClearer;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class ClearCaches_MinecraftMixin {
    @Inject(
        at = @At("HEAD"),
        method = "onResourceLoadFinished"
    )
    private void eg_particle_interactions$clearCachesOnReload(@Coerce Object gameLoadCookie, CallbackInfo ci) {
        CacheClearer.clear();
    }
}
