package games.enchanted.blockplaceparticles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParticleInteractionsLogging {
    public static boolean enableDebugLogs = false;
    public static final Logger LOG = LoggerFactory.getLogger(ParticleInteractionsMod.MOD_NAME);

    private static final String debugPrefix = "[" + ParticleInteractionsMod.MOD_NAME + " Debug Message]: ";
    private static final String messagePrefix = "[" + ParticleInteractionsMod.MOD_NAME + "]: ";

    public static void debugInfo(String message, Object... args) {
        if(enableDebugLogs) {
            LOG.info(debugPrefix + message, args);
        }
    }

    public static void message(String message, Object... args) {
        LOG.info(messagePrefix + message, args);
    }
}
