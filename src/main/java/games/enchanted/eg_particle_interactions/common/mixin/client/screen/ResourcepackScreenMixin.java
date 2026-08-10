package games.enchanted.eg_particle_interactions.common.mixin.client.screen;

import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;

@Mixin(PackSelectionScreen.class)
public class ResourcepackScreenMixin {
    @Shadow
    @Final
    private Path packDir;

    @Unique
    private boolean eg_particle_interactions$shownVersionWarning = false;

    @Inject(
        at = @At("TAIL"),
        method = "init"
    )
    private void eg_particle_interactions$packScreenInit(CallbackInfo ci) {
        if(!this.packDir.equals(Minecraft.getInstance().getResourcePackDirectory())) return;
        if(this.eg_particle_interactions$shownVersionWarning) return;

        ParticleInteractionsMod.showVersionLoadFailedToast();
        this.eg_particle_interactions$shownVersionWarning = true;
    }
}
