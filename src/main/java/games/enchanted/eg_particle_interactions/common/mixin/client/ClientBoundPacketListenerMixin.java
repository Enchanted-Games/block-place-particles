package games.enchanted.eg_particle_interactions.common.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import games.enchanted.eg_particle_interactions.common.Logging;
import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPacketListener.class)
public class ClientBoundPacketListenerMixin {
    @Inject(
        at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V", remap = false),
        method =
            //? if minecraft: <= 26.2 {
            /*"handleParticleEvent"
            *///? } else {
            "tryAddParticle"
            //? }
    )
    private void eg_particle_interactions$addAdditionalLogMessagesOnParticleFail(
        //? if minecraft: <= 26.2 {
        /*ClientboundLevelParticlesPacket packet, CallbackInfo ci, @Local Throwable throwable
        *///? } else {
        ClientboundLevelParticlesPacket packet, double x, double y, double z, double xa, double ya, double za, CallbackInfoReturnable<Boolean> cir, @Local Throwable throwable
        //? }
    ) {
        if(GeneralOptions.DEBUG_EXTRA_INFO_ON_PARTICLE_PACKET_ERROR.getValue() || PlatformHelper.isDevelopmentEnvironment()) {
            Logging.error("ignored throwable: {}", throwable.getMessage());
            Logging.error("stacktrace:");
            throwable.printStackTrace();
            //? if minecraft: <= 26.2 {
            /*Logging.error("packet info: {} {} {} {} {} {}", packet.getParticle(), packet.isOverrideLimiter(), packet.alwaysShow(), packet.getX(), packet.getY(), packet.getZ());
            Logging.error("particle type: {}", packet.getParticle().getType());
            *///? } else {
            Logging.error("packet info: {} {} {} {} {} {}", packet.particle(), packet.overrideLimiter(), packet.alwaysShow(), packet.x(), packet.y(), packet.z());
            Logging.error("particle type: {}", packet.particle().getType());
            //? }
        }
    }
}
