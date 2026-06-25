package games.enchanted.eg_particle_interactions.common;

import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logging {
    public static final Logger LOG = LoggerFactory.getLogger(Constants.MOD_NAME);

    private static final String messagePrefix = "[" + Constants.MOD_NAME + "]: ";
    private static final String textureDebugPrefix = "[" + Constants.MOD_NAME + " Texture Debug]: ";

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


    public static void textureDebugInfo(String message, Object... args) {
        if(GeneralOptions.DEBUG_TEXTURE_LOGGING.getValue()) {
            LOG.info(textureDebugPrefix + message, args);
        }
    }
}
