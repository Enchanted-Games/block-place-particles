package games.enchanted.eg_particle_interactions.common.mixin.client.resource.vanilla;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Debug(export = true)
@Mixin(Minecraft.class)
public class RegisterListeners_MinecraftMixin {
    @Definition(id = "resourceManager", field = "Lnet/minecraft/client/Minecraft;resourceManager:Lnet/minecraft/server/packs/resources/ReloadableResourceManager;")
    @Definition(id = "registerReloadListener", method = "Lnet/minecraft/server/packs/resources/ReloadableResourceManager;registerReloadListener(Lnet/minecraft/server/packs/resources/PreparableReloadListener;)V")
    @Expression("this.resourceManager.registerReloadListener(?)")
    @WrapOperation(
        at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0),
        method = "<init>"
    )
    private void eg_particle_interactions$registerReloadListenersIfNoFAPI(ReloadableResourceManager instance, PreparableReloadListener listener, Operation<Void> original) {
        original.call(instance, listener);
        if(!ParticleInteractionsMod.isFabricResourceLoaderPresent()) {
            ParticleInteractionsMod.registerResourceReloadListeners();
        }
    }
}
