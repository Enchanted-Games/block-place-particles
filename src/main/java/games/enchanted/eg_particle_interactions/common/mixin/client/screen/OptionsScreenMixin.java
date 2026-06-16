package games.enchanted.eg_particle_interactions.common.mixin.client.screen;

import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.config.compat.ConfigScreenCreator;
import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    public OptionsScreenMixin(Component title) {
        super(title);
    }

    @Inject(
        at = @At("TAIL"),
        method = "init()V"
    )
    private void eg_particle_interactions$addParticleInteractionsConfigButton(CallbackInfo ci) {
        if(!GeneralOptions.SHOW_BUTTON_IN_OPTIONS_SCREEN.getValue()) return;
        if(PlatformHelper.isDevelopmentEnvironment() || (Minecraft.getInstance().level != null && !ParticleInteractionsMod.isModMenuPresent())) {
            final int width = 120;
            final int height = 16;
            this.addRenderableWidget(
                Button.builder(
                    Component.literal(Constants.MOD_NAME),
                    (button) -> this.minecraft.setScreen(ConfigScreenCreator.getScreenCreator().createScreen(this))
                ).bounds( 2, 2, width, height).build()
            );
        }
    }
}
