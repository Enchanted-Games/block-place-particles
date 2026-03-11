package games.enchanted.eg_particle_interactions.common.mixin.client.resource.vanilla;

import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.resource.pack.ModPackResources;
import net.minecraft.client.resources.ClientPackSource;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(BuiltInPackSource.class)
public class RegisterModAssets_BuiltinPackSourceMixin {
    @Inject(
        at = @At("TAIL"),
        method = "listBundledPacks"
    )
    private void eg_particle_interactions$registerModResources(Consumer<Pack> packConsumer, CallbackInfo ci) {
        if(!((BuiltInPackSource) (Object) this instanceof ClientPackSource)) return;
        if(!ParticleInteractionsMod.isFabricResourceLoaderPresent()) {
            packConsumer.accept(ModPackResources.createPack());
        }
    }
}
