package games.enchanted.eg_particle_interactions.common.mixin.client.debug;

import com.mojang.blaze3d.platform.InputConstants;
import games.enchanted.eg_particle_interactions.common.compat.yacl.YaclScreenInstanceChecker;
import games.enchanted.eg_particle_interactions.common.config.categories.DebugOptions;
import games.enchanted.eg_particle_interactions.common.gui.debug.DebugScreen;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyboardHandler.class)
abstract class KeyboardHandlerMixin {
    @Inject(
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;options:Lnet/minecraft/client/Options;", opcode = Opcodes.GETFIELD),
        method = "handleDebugKeys",
        cancellable = true
    )
    private void eg_particle_interactions$customDebugKeys(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        if(!DebugOptions.DEBUG_KEYBIND_ENABLED.getValue()) return;
        if(event.key() != InputConstants.KEY_5) return;
        if(YaclScreenInstanceChecker.instance().isYaclScreen(Minecraft.getInstance().gui.screen())) return;

        DebugScreen.show();
        cir.setReturnValue(true);
    }
}
