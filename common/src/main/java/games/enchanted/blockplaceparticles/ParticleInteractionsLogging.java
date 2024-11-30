package games.enchanted.blockplaceparticles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParticleInteractionsLogging {
    private static boolean enableDebugLogs = false;
    public static final Logger LOG = LoggerFactory.getLogger(ParticleInteractionsMod.MOD_NAME);
    private static final SystemToast.SystemToastId DEBUG_LOGS_TOAST = new SystemToast.SystemToastId(3000L);

    private static final String debugPrefix = "[" + ParticleInteractionsMod.MOD_NAME + " Debug Message]: ";
    private static final String messagePrefix = "[" + ParticleInteractionsMod.MOD_NAME + "]: ";

    public static void message(String message, Object... args) {
        LOG.info(messagePrefix + message, args);
    }

    public static void debugInfo(String message, Object... args) {
        if(enableDebugLogs) {
            LOG.info(debugPrefix + message, args);
        }
    }

    public static void toggleDebugLogs() {
        if(ParticleInteractionsLogging.enableDebugLogs) {
            // toggle debug logs off
            ParticleInteractionsLogging.enableDebugLogs = false;
            LOG.info(debugPrefix + "Debug Logs disabled");
            SystemToast.addOrUpdate(Minecraft.getInstance().getToasts(), DEBUG_LOGS_TOAST, Component.literal(ParticleInteractionsMod.MOD_NAME), Component.translatable("eg_particle_interactions.toast.debug_logs_disabled"));
            return;
        }
        // toggle debug logs on
        ParticleInteractionsLogging.enableDebugLogs = true;
        LOG.info(debugPrefix + "Debug Logs enabled");
        SystemToast.addOrUpdate(Minecraft.getInstance().getToasts(), DEBUG_LOGS_TOAST, Component.literal(ParticleInteractionsMod.MOD_NAME), Component.translatable("eg_particle_interactions.toast.debug_logs_enabled"));
    }
}
