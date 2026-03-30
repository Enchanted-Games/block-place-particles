package games.enchanted.eg_particle_interactions.common;

import games.enchanted.eg_particle_interactions.common.config.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.override_system.override.BlockOverrideManager;
import games.enchanted.eg_particle_interactions.common.override_system.override.FluidOverrideManager;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverrides;
import games.enchanted.eg_particle_interactions.common.particle.ParticleTypesRegistry;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearanceManager;
import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;
import net.minecraft.resources.Identifier;

public class ParticleInteractionsMod {
    public static Identifier PARTICLE_OVERRIDES_RELOAD_LISTENER = ParticleInteractionsMod.id("particle_overrides");
    public static Identifier BLOCK_OVERRIDE_RULE_RELOAD_LISTENER = ParticleInteractionsMod.id("block_override_rules");
    public static Identifier FLUID_OVERRIDE_RULE_RELOAD_LISTENER = ParticleInteractionsMod.id("fluid_override_rules");
    public static Identifier PARTICLE_APPEARANCE_RELOAD_LISTENER = ParticleInteractionsMod.id("particle_appearances");

    public static void startOfModLoading() {
        Logging.info("Mod init started. Compiled for {}", Constants.TARGET_PLATFORM);

        ParticleTypesRegistry.init();

        // register reload listeners here if fabric api is installed or if targeting neoforge
        // if no fabric api reload listeners are registered in Minecraft init
        //? if fabric {
        if (isFabricResourceLoaderPresent()) {
            registerResourceReloadListeners();
        }
        //?} else {
        /*registerResourceReloadListeners();
        *///? }
    }

    public static void endOfModLoading() {
        ConfigOptions.readConfig();
        Logging.info("Init done!");
    }

    public static void registerResourceReloadListeners() {
        PlatformHelper.registerResourceReloadListener(ParticleOverrides.INSTANCE, PARTICLE_OVERRIDES_RELOAD_LISTENER);
        PlatformHelper.registerResourceReloadListener(BlockOverrideManager.INSTANCE, BLOCK_OVERRIDE_RULE_RELOAD_LISTENER);
        PlatformHelper.registerResourceReloadListener(FluidOverrideManager.INSTANCE, FLUID_OVERRIDE_RULE_RELOAD_LISTENER);
        PlatformHelper.registerResourceReloadListener(ParticleAppearanceManager.INSTANCE, PARTICLE_APPEARANCE_RELOAD_LISTENER);
    }

    public static boolean isFabricResourceLoaderPresent() {
        return PlatformHelper.isModLoaded(Constants.FABRIC_RESOURCE_LOADER_ID);
    }

    public static boolean isModMenuPresent() {
        return PlatformHelper.isModLoaded(Constants.MOD_MENU_ID);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
    }
}
