package games.enchanted.eg_particle_interactions.common;

import games.enchanted.eg_particle_interactions.common.resource.version.PackVersion;

public class Constants {
    public static final String MOD_NAME = "Particle Interactions";
    public static final String MOD_ID = "eg_particle_interactions";

    public static final String FABRIC_RESOURCE_LOADER_ID = "fabric-resource-loader-v1";
    public static final String MOD_MENU_ID = "modmenu";

    public static final PackVersion CURRENT_PACK_VERSION = new PackVersion(
        0,
        1,
        0
    );

    public static final String TARGET_PLATFORM =
    //? if fabric {
        "fabric";
    //?}
    //? if neoforge {
        /*"neoforge";
    *///?}
}
