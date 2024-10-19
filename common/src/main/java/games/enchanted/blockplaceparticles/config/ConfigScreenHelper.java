package games.enchanted.blockplaceparticles.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import games.enchanted.blockplaceparticles.particle_spawning.BlockParticleOverride;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ConfigScreenHelper {
    public static ConfigCategory.Builder createBlockParticleOverrideConfigWidgets(ConfigCategory.Builder configCategoryBuilder) {
        BlockParticleOverride[] overrides = BlockParticleOverride.values();
        for (BlockParticleOverride override : overrides) {
            if(override == BlockParticleOverride.NONE || override == BlockParticleOverride.BLOCK) continue;
            configCategoryBuilder.group(OptionGroup.createBuilder()
                .name( Component.literal("--------------") )
                .collapsed(true)
                .option(LabelOption.createBuilder()
                    .build())
                .build()
            );
            configCategoryBuilder.group(
                createOptionsForBlockOverride(override)
            );
            configCategoryBuilder.group(createBlockListForBlockOverride(override));
        }
        configCategoryBuilder.group(OptionGroup.createBuilder()
            .name( Component.literal("--------------") )
            .collapsed(true)
            .option(LabelOption.createBuilder()
                .build())
            .build()
        );
        configCategoryBuilder.group(
            createOptionsForBlockOverride(BlockParticleOverride.BLOCK)
        );

        return configCategoryBuilder;
    }

    public static OptionGroup createOptionsForBlockOverride(BlockParticleOverride override) {
        String particleTypeKey = override.getName();

        Option<Boolean> overrideEnabled = Option.<Boolean>createBuilder()
            .name( ConfigTranslation.getGlobalOption(ConfigTranslation.IS_OVERRIDE_ENABLED).toComponent() )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(ConfigTranslation.IS_OVERRIDE_ENABLED)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
            .binding(Binding.generic(override.getOverrideEnabled_default(), override.getOverrideEnabled_getter(), override.getOverrideEnabled_setter()))
            .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
        .build();
        Option<Integer> maxParticlesOnPlaceOption = ConfigScreen.maxParticlesOnPlaceOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_PLACE_ALONG_EDGES, override.getMaxParticlesOnPlace_default(), override.getMaxParticlesOnPlace_getter(), override.getMaxParticlesOnPlace_setter());
        Option<Integer> maxParticlesOnBreakOption = ConfigScreen.maxParticlesOnBreakOption(ConfigTranslation.MAX_PARTICLES_ON_BLOCK_BREAK_ALONG_AXIS, override.getMaxParticlesOnBreak_default(), override.getMaxParticlesOnBreak_getter(), override.getMaxParticlesOnBreak_setter());

        return ConfigScreen.createMultipleOptionsConfigGroup(override.getName(), override.getGroupName(), ConfigTranslation.BLOCKS_CONFIG_CATEGORY, overrideEnabled, maxParticlesOnPlaceOption, maxParticlesOnBreakOption);
    }
    public static ListOption<ResourceLocation> createBlockListForBlockOverride(BlockParticleOverride override) {
        return ConfigScreen.createBlockLocationListOption(
            override.getName(),
            override.getGroupName() + "_blocks",
            ConfigTranslation.BLOCKS_CONFIG_CATEGORY,
            override.getSupportedBlockResourceLocations_default(),
            override.getSupportedBlockResourceLocations_getter(),
            override.getSupportedBlockResourceLocations_setter()
        );
    }
}
