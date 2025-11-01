package games.enchanted.eg_particle_interactions.common.mixin.screen;

import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.config.screen.ConfigScreen;
import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;
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
    private void block_place_particle$addParticleInteractionsConfigButton(CallbackInfo ci) {
        if(PlatformHelper.isDevelopmentEnvironment()) {
            final int width = 120;
            final int height = 16;
            this.addRenderableWidget(
                Button.builder(
                    Component.literal(Constants.MOD_NAME),
                    (button) -> this.minecraft.setScreen(ConfigScreen.createConfigScreen(this))
                ).bounds( 2, 2, width, height).build()
            );
        }
    }
}
