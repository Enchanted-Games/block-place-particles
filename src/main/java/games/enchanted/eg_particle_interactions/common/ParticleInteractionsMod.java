package games.enchanted.eg_particle_interactions.common;

import games.enchanted.eg_particle_interactions.common.config.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.override_system.override.BlockOverrideManager;
import games.enchanted.eg_particle_interactions.common.override_system.override.FluidOverrideManager;
import games.enchanted.eg_particle_interactions.common.override_system.override.ParticleOverrides;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleTypesRegistry;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearanceManager;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponents;
import games.enchanted.eg_particle_interactions.common.particle.definition.ParticleDefinitionManager;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.EmitterRuleSetManager;
import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;
import games.enchanted.eg_particle_interactions.common.predicates.biome.list.BiomeListManager;
import games.enchanted.eg_particle_interactions.common.predicates.block.list.BlockListManager;
import games.enchanted.eg_particle_interactions.common.predicates.entity.list.EntityListManager;
import games.enchanted.eg_particle_interactions.common.predicates.fluid.list.FluidListManager;
import games.enchanted.eg_particle_interactions.common.resource.texture.palette.BlockPaletteManager;
import games.enchanted.eg_particle_interactions.common.resource.texture.palette.FluidPaletteManager;
import games.enchanted.eg_particle_interactions.common.resource.version.PackVersion;
import games.enchanted.eg_particle_interactions.common.util.Toaster;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class ParticleInteractionsMod {
    public static Identifier PARTICLE_DEFINITIONS_RELOAD_LISTENER = ParticleInteractionsMod.id("particle_definitions");
    public static Identifier PARTICLE_OVERRIDES_RELOAD_LISTENER = ParticleInteractionsMod.id("particle_overrides");
    public static Identifier BLOCK_OVERRIDE_RULE_RELOAD_LISTENER = ParticleInteractionsMod.id("block_override_rules");
    public static Identifier FLUID_OVERRIDE_RULE_RELOAD_LISTENER = ParticleInteractionsMod.id("fluid_override_rules");
    public static Identifier PARTICLE_APPEARANCE_RELOAD_LISTENER = ParticleInteractionsMod.id("particle_appearances");
    public static Identifier BLOCK_LIST_RELOAD_LISTENER = ParticleInteractionsMod.id("block_lists");
    public static Identifier FLUID_LIST_RELOAD_LISTENER = ParticleInteractionsMod.id("fluid_lists");
    public static Identifier BIOME_LIST_RELOAD_LISTENER = ParticleInteractionsMod.id("biome_lists");
    public static Identifier ENTITY_LIST_RELOAD_LISTENER = ParticleInteractionsMod.id("entity_lists");
    public static Identifier EMITTER_RULES_RELOAD_LISTENER = ParticleInteractionsMod.id("emitter_rules");
    public static Identifier BLOCK_PALETTES_RELOAD_LISTENER = ParticleInteractionsMod.id("block_palettes");
    public static Identifier FLUID_PALETTES_RELOAD_LISTENER = ParticleInteractionsMod.id("fluid_palettes");

    public static void startOfModLoading() {
        Logging.info("Mod init started. Compiled for {}", Constants.TARGET_PLATFORM);

        ParticleComponents.init();
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

    public static void clientInitFinished() {
        if(Constants.CURRENT_PACK_VERSION.is(PackVersion.UNSPECIFIED)) {
            Toaster.showToast(Component.literal("Failed to load pack version"), Component.literal("Some resourcepacks made for Particle Interactions may not work correctly. Please report this"));
        }
    }

    public static void registerResourceReloadListeners() {
        PlatformHelper.registerResourceReloadListener(ParticleOverrides.INSTANCE, PARTICLE_OVERRIDES_RELOAD_LISTENER);
        PlatformHelper.registerResourceReloadListener(BlockOverrideManager.INSTANCE, BLOCK_OVERRIDE_RULE_RELOAD_LISTENER);
        PlatformHelper.registerResourceReloadListener(FluidOverrideManager.INSTANCE, FLUID_OVERRIDE_RULE_RELOAD_LISTENER);
        PlatformHelper.registerResourceReloadListener(ParticleAppearanceManager.INSTANCE, PARTICLE_APPEARANCE_RELOAD_LISTENER);
        PlatformHelper.registerResourceReloadListener(BlockListManager.INSTANCE, BLOCK_LIST_RELOAD_LISTENER);
        PlatformHelper.registerResourceReloadListener(FluidListManager.INSTANCE, FLUID_LIST_RELOAD_LISTENER);
        PlatformHelper.registerResourceReloadListener(BiomeListManager.INSTANCE, BIOME_LIST_RELOAD_LISTENER);
        PlatformHelper.registerResourceReloadListener(EntityListManager.INSTANCE, ENTITY_LIST_RELOAD_LISTENER);
        PlatformHelper.registerResourceReloadListener(EmitterRuleSetManager.INSTANCE, EMITTER_RULES_RELOAD_LISTENER);
        PlatformHelper.registerResourceReloadListener(ParticleDefinitionManager.INSTANCE, PARTICLE_DEFINITIONS_RELOAD_LISTENER);
        PlatformHelper.registerResourceReloadListener(BlockPaletteManager.INSTANCE, BLOCK_PALETTES_RELOAD_LISTENER);
        PlatformHelper.registerResourceReloadListener(FluidPaletteManager.INSTANCE, FLUID_PALETTES_RELOAD_LISTENER);
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
