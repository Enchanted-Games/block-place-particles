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

@Mixin(ClientPacketListener.class)
public class ClientBoundPacketListenerMixin {
    @Inject(
        at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V", remap = false),
        method = "handleParticleEvent"
    )
    private void eg_particle_interactions$addAdditionalLogMessagesOnParticleFail(ClientboundLevelParticlesPacket packet, CallbackInfo ci, @Local Throwable throwable) {
        if(GeneralOptions.DEBUG_EXTRA_INFO_ON_PARTICLE_PACKET_ERROR.getValue() || PlatformHelper.isDevelopmentEnvironment()) {
            Logging.error("ignored throwable: {}", throwable.getMessage());
            Logging.error("stacktrace:");
            throwable.printStackTrace();
            Logging.error("packet info: {} {} {} {} {} {}", packet.getParticle(), packet.isOverrideLimiter(), packet.alwaysShow(), packet.getX(), packet.getY(), packet.getZ());
            Logging.error("particle type: {}", packet.getParticle().getType());
        }
    }
}
