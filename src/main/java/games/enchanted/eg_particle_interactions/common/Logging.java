package games.enchanted.eg_particle_interactions.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Logging {
    public static final Logger LOG = LoggerFactory.getLogger(Constants.MOD_NAME);

    private static final String messagePrefix = "[" + Constants.MOD_NAME + "]: ";
    private static final String interactionDebugPrefix = "[" + Constants.MOD_NAME + " Interaction Debug]: ";
    private static final String textureDebugPrefix = "[" + Constants.MOD_NAME + " Texture Debug]: ";

    private static final SystemToast.SystemToastId INTERACTION_DEBUG_LOGS_TOAST = new SystemToast.SystemToastId(3000L);
    private static boolean interactionDebugLogsEnabled = false;
    private static final SystemToast.SystemToastId TEXTURE_DEBUG_LOGS_TOAST = new SystemToast.SystemToastId(3000L);
    private static boolean textureDebugLogsEnabled = false;

    public static void info(String message, Object... args) {
        LOG.info(messagePrefix + message, args);
    }

    public static void warn(String message, Object... args) {
        LOG.warn(messagePrefix + message, args);
    }

    public static void error(String message, Object... args) {
        LOG.error(messagePrefix + message, args);
    }

    public static void debug(String message, Object... args) {
        LOG.debug(messagePrefix + message, args);
    }

    public static void interactionDebugInfo(String message, Object... args) {
        if(interactionDebugLogsEnabled) {
            LOG.info(interactionDebugPrefix + message, args);
        }
    }

    public static void textureDebugInfo(String message, Object... args) {
        if(textureDebugLogsEnabled) {
            LOG.info(textureDebugPrefix + message, args);
        }
    }

    protected static void toggleDebugLogs(Consumer<Boolean> fieldSetter, Supplier<Boolean> fieldGetter, SystemToast.SystemToastId toastId, String debugLoggingName, String debugPrefix, String enabledTranslationKey, String disabledTranslationKey) {
        if(fieldGetter.get()) {
            // toggle debug logs off
            fieldSetter.accept(false);
            LOG.info(debugPrefix + debugLoggingName + " disabled");
            SystemToast.addOrUpdate(Minecraft.getInstance().getToastManager(), toastId, Component.literal(Constants.MOD_NAME), Component.translatable(disabledTranslationKey));
            return;
        }
        // toggle debug logs on
        fieldSetter.accept(true);
        LOG.info(debugPrefix + debugLoggingName + " enabled");
        SystemToast.addOrUpdate(Minecraft.getInstance().getToastManager(), toastId, Component.literal(Constants.MOD_NAME), Component.translatable(enabledTranslationKey));
    }

    public static void toggleInteractionDebugLogging() {
        toggleDebugLogs(
            (newValue) -> interactionDebugLogsEnabled = newValue,
            () -> interactionDebugLogsEnabled,
            INTERACTION_DEBUG_LOGS_TOAST,
            "Interaction Debug Logging",
            interactionDebugPrefix,
            "eg_particle_interactions.common.toast.debug_logs_enabled",
            "eg_particle_interactions.common.toast.debug_logs_disabled"
        );
    }
    public static void toggleTextureDebugLogging() {
        toggleDebugLogs(
            (newValue) -> textureDebugLogsEnabled = newValue,
            () -> textureDebugLogsEnabled,
            TEXTURE_DEBUG_LOGS_TOAST,
            "Texture Debug Logging",
            textureDebugPrefix,
            "eg_particle_interactions.common.toast.texture_debug_logs_enabled",
            "eg_particle_interactions.common.toast.texture_debug_logs_disabled"
        );
    }
}
