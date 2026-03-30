package games.enchanted.eg_particle_interactions.common.config.screen.yacl;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.config.option.ConfigOption;
import games.enchanted.eg_particle_interactions.common.config.screen.yacl.controller.BlockLocationController;
import games.enchanted.eg_particle_interactions.common.config.screen.yacl.controller.FluidLocationController;
import games.enchanted.eg_particle_interactions.common.localisation.ConfigTranslation;
import games.enchanted.eg_particle_interactions.common.registry.ObjectOrTagLocation;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.function.Function;

class ConfigScreenHelper {
    // decoration
    public static OptionGroup createSeparator() {
        return OptionGroup.createBuilder()
            .name(Component.translatable("eg_particle_interactions.config.category_separator").withColor(0xff6c6c6c))
            .collapsed(true)
            .option(LabelOption.createBuilder().line(Component.empty()).build())
            .build();
    }

    // options
    public static Option<Boolean> booleanOption(String booleanOptionLabelText, String particleTypeKey, ConfigOption<Boolean> option) {
        return Option.<Boolean>createBuilder()
            .name( ConfigTranslation.createPlaceholder(ConfigTranslation.getGlobalOption(booleanOptionLabelText).toComponent(), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(booleanOptionLabelText)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
            .binding(option.createBinding())
            .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
            .build();
    }
    public static Option<Boolean> genericBooleanOption(String optionName, ConfigOption<Boolean> option) {
        ConfigTranslation.TranslationKey translationKey = ConfigTranslation.getGlobalOption(optionName);
        return Option.<Boolean>createBuilder()
            .name( translationKey.toComponent() )
            .description(OptionDescription.of( ConfigTranslation.createDesc(translationKey) ))
            .binding(option.createBinding())
            .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
            .build();
    }

    public static Option<Integer> integerSliderOption(String optionName, ConfigOption<Integer> option, int min, int max, int step) {
        return createIntegerOption(option, ConfigTranslation.getGlobalOption(optionName).toComponent(), ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(optionName)), min, max, step);
    }
    public static Option<Integer> integerSliderOption(String optionName, String particleTypeKey, ConfigOption<Integer> option, int min, int max, int step) {
        return createIntegerOption(option, ConfigTranslation.createPlaceholder(ConfigTranslation.getGlobalOption(optionName).toComponent(), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ), ConfigTranslation.createPlaceholder( ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(optionName)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ), min, max, step);
    }

    public static Option<Integer> createIntegerOption(ConfigOption<Integer> option, Component name, Component description, int min, int max, int step) {
        return Option.<Integer>createBuilder()
            .name(name)
            .description(OptionDescription.of(description))
            .binding(option.createBinding())
            .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(min, max).step(step))
            .build();
    }

    public static ListOption<ObjectOrTagLocation> createBlockLocationListOption(String particleTypeKey, String groupName, String category, ConfigOption<List<ObjectOrTagLocation>> option) {
        return createListOption(new ObjectOrTagLocation(RegistryHelpers.getLocationFromBlock(Blocks.STONE)), BlockLocationController::new, particleTypeKey, groupName, category, GeneralOptions.AUTO_COLLAPSE_CONFIG_LISTS.getValue(), option);
    }
    public static ListOption<Identifier> createFluidListOption(String particleTypeKey, String groupName, String category, ConfigOption<List<Identifier>> option) {
        return createListOption(RegistryHelpers.getLocationFromFluid(Fluids.WATER), FluidLocationController::new, particleTypeKey, groupName, category, GeneralOptions.AUTO_COLLAPSE_CONFIG_LISTS.getValue(), option);
    }
    public static <T> ListOption<T> createListOption(T initial, Function<ListOptionEntry<T>, Controller<T>> controller, String particleTypeKey, String groupName, String category, boolean collapsedByDefault, ConfigOption<List<T>> option) {
        ConfigTranslation.TranslationKey groupNameKey = ConfigTranslation.getGroupName(category, groupName);
        return ListOption.<T>createBuilder()
            .name( ConfigTranslation.createPlaceholder(groupNameKey.toComponent(), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(groupNameKey), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
            .binding(option.createBinding())
            .customController(controller)
            .collapsed(collapsedByDefault)
            .initial(initial)
            .build();
    }

    public static Option<Integer> maxParticlesOnPlaceOption(String optionName, ConfigOption<Integer> option) {
        return integerSliderOption(optionName, option, 0, 16, 1);
    }
    public static Option<Integer> maxParticlesOnBreakOption(String optionName, ConfigOption<Integer> option) {
        return integerSliderOption(optionName, option, 0, 8, 1);
    }


    public static <T extends Enum<T>> Option<T> enumCycleOption(String optionName, ConfigOption<T> option, Class<T> enumClass) {
        ConfigTranslation.TranslationKey translationKey = ConfigTranslation.getGlobalOption(optionName);
        return Option.<T>createBuilder()
            .name( translationKey.toComponent() )
            .description(OptionDescription.of( ConfigTranslation.createDesc(translationKey) ))
            .binding(option.createBinding())
            .controller(
                opt -> EnumControllerBuilder.create(opt)
                    .enumClass(enumClass)
            )
            .build();
    }

    // groups
    public static OptionGroup createParticleToggleAndMaxAndIntensityConfigGroup(String particleTypeKey, String groupName, String category, ConfigOption<Boolean> particleEnabledOption, String particleEnabledTranslationOption, Option<Integer> maxParticlesOnUseOption, Option<Integer> particleIntensityOption) {
        Option<Boolean> particleToggleOption = booleanOption(particleEnabledTranslationOption, particleTypeKey, particleEnabledOption);
        return createMultipleOptionsConfigGroup(particleTypeKey, groupName, category, particleToggleOption, maxParticlesOnUseOption, particleIntensityOption);
    }
    public static OptionGroup createParticleToggleAndIntSliderConfigGroup(String particleTypeKey, String groupName, String category, ConfigOption<Boolean> particleEnabledOption, String particleEnabledTranslationOption, Option<Integer> intSlider) {
        Option<Boolean> particleToggleOption = booleanOption(particleEnabledTranslationOption, particleTypeKey, particleEnabledOption);
        return createMultipleOptionsConfigGroup(particleTypeKey, groupName, category, particleToggleOption, intSlider);
    }
    public static OptionGroup createMultipleOptionsConfigGroup(String particleTypeKey, String groupName, String category, Option<?> ...options) {
        return createMultipleOptionsConfigGroup(particleTypeKey, groupName, category, false, options);
    }
    public static OptionGroup createMultipleOptionsConfigGroup(String particleTypeKey, String groupName, String category, boolean collapseByDefault, Option<?> ...options) {
        ConfigTranslation.TranslationKey groupNameKey = ConfigTranslation.getGroupName(category, groupName);
        OptionGroup.Builder optionGroupBuilder = OptionGroup.createBuilder()
            .name( ConfigTranslation.createPlaceholder(groupNameKey.toComponent(), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(groupNameKey), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ));
        for (Option<?> option : options) {
            optionGroupBuilder.option(option);
        }
        return optionGroupBuilder.collapsed(collapseByDefault).build();
    }

    public static OptionGroup createFluidParticleToggleAndMaxConfigGroup(String particleTypeKey, String groupName, String category, ConfigOption<Boolean> spawnOnFluidPlaceOption, Option<Integer> maxPlaceParticlesOption) {
        Option<Boolean> onFluidPlaceOption = Option.<Boolean>createBuilder()
            .name( ConfigTranslation.getGlobalOption(ConfigTranslation.SPAWN_FLUID_PARTICLE_ON_PLACE).toComponent() )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(ConfigTranslation.SPAWN_FLUID_PARTICLE_ON_PLACE)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
            .binding(spawnOnFluidPlaceOption.createBinding())
            .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
            .build();

        return createMultipleOptionsConfigGroup(particleTypeKey, groupName, category, onFluidPlaceOption, maxPlaceParticlesOption);
    }

    public static OptionGroup createGenericConfigGroup(String groupName, String category, boolean collapseByDefault, Option<?>...options) {
        ConfigTranslation.TranslationKey groupNameKey = ConfigTranslation.getGroupName(category, groupName);
        OptionGroup.Builder optionGroupBuilder = OptionGroup.createBuilder()
            .name( groupNameKey.toComponent() )
            .description(OptionDescription.of( ConfigTranslation.createDesc(groupNameKey) ));
        for (Option<?> option : options) {
            optionGroupBuilder.option(option);
        }
        return optionGroupBuilder.collapsed(collapseByDefault).build();
    }

}
