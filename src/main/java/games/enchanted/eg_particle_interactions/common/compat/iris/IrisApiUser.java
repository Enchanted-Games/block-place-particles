package games.enchanted.eg_particle_interactions.common.compat.iris;

import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;

public interface IrisApiUser {
    IrisApiUser INSTANCE = instance();

    default void doIrisStuff() {
    }

    default boolean shaderpackActive() {
        return false;
    }

    static IrisApiUser instance() {
        if(PlatformHelper.isModLoaded("iris")) {
            return new IrisApiUserImpl();
        } else {
            return new IrisApiUser() {};
        }
    }
}
