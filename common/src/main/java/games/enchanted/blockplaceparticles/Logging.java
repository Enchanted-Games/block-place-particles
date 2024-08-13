package games.enchanted.blockplaceparticles;

public class Logging {
    public static void debugInfo(String message) {
        if(BlockPlaceParticlesConstants.DEBUG_LOGS) {
            BlockPlaceParticlesConstants.LOG.info(message);
        }
    }
}
