package games.enchanted.eg_particle_interactions.common.config2.screen;

import dev.isxander.yacl3.api.Binding;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import games.enchanted.eg_particle_interactions.common.config2.option.ConfigOption;
import games.enchanted.eg_particle_interactions.common.localisation.ConfigTranslation;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigScreenHelper {
    protected static OptionGroup createGenericConfigGroup(String groupName, String category, boolean collapseByDefault, Option<?>...options) {
        ConfigTranslation.TranslationKey groupNameKey = ConfigTranslation.getGroupName(category, groupName);
        OptionGroup.Builder optionGroupBuilder = OptionGroup.createBuilder()
            .name( groupNameKey.toComponent() )
            .description(OptionDescription.of( ConfigTranslation.createDesc(groupNameKey) ));
        for (Option<?> option : options) {
            optionGroupBuilder.option(option);
        }
        return optionGroupBuilder.collapsed(collapseByDefault).build();
    }

    protected static Option<Boolean> booleanOption(String booleanOptionLabelText, String particleTypeKey, ConfigOption<Boolean> option) {
        return Option.<Boolean>createBuilder()
            .name( ConfigTranslation.createPlaceholder(ConfigTranslation.getGlobalOption(booleanOptionLabelText).toComponent(), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) )
            .description(OptionDescription.of( ConfigTranslation.createPlaceholder(ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(booleanOptionLabelText)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ) ))
            .binding(option.createBinding())
            .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
            .build();
    }
    protected static Option<Boolean> genericBooleanOption(String optionName, ConfigOption<Boolean> option) {
        ConfigTranslation.TranslationKey translationKey = ConfigTranslation.getGlobalOption(optionName);
        return Option.<Boolean>createBuilder()
            .name( translationKey.toComponent() )
            .description(OptionDescription.of( ConfigTranslation.createDesc(translationKey) ))
            .binding(option.createBinding())
            .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
            .build();
    }

    protected static Option<Integer> integerSliderOption(String optionName, ConfigOption<Integer> option, int min, int max, int step) {
        return createIntegerOption(option, ConfigTranslation.getGlobalOption(optionName).toComponent(), ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(optionName)), min, max, step);
    }
    protected static Option<Integer> integerSliderOption(String optionName, String particleTypeKey, ConfigOption<Integer> option, int min, int max, int step) {
        return createIntegerOption(option, ConfigTranslation.createPlaceholder(ConfigTranslation.getGlobalOption(optionName).toComponent(), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ), ConfigTranslation.createPlaceholder( ConfigTranslation.createDesc(ConfigTranslation.getGlobalOption(optionName)), Component.translatable(ConfigTranslation.getParticleType(particleTypeKey).toString()).getString() ), min, max, step);
    }

    protected static Option<Integer> createIntegerOption(ConfigOption<Integer> option, Component name, Component description, int min, int max, int step) {
        return Option.<Integer>createBuilder()
            .name(name)
            .description(OptionDescription.of(description))
            .binding(option.createBinding())
            .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(min, max).step(step))
            .build();
    }
}
