package games.enchanted.eg_particle_interactions.common.config.categories;

import games.enchanted.eg_particle_interactions.common.config.ConfigCategory;
import games.enchanted.eg_particle_interactions.common.config.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.config.option.BoolOption;
import games.enchanted.eg_particle_interactions.common.config.option.ConfigOption;
import games.enchanted.eg_particle_interactions.common.config.option.IntOption;
import games.enchanted.eg_particle_interactions.common.config.option.ResourceLocationListOption;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class FluidPlacementOptions {
    public static final ConfigOption<List<Identifier>> WATER_SPLASH_FLUIDS = registerOption(
        new ResourceLocationListOption(
            List.of(
                RegistryHelpers.getLocationFromFluid(Fluids.WATER),
                RegistryHelpers.getLocationFromFluid(Fluids.FLOWING_WATER)
            ),
            "water_splash_fluids"
        )
    );
    public static final ConfigOption<Boolean> WATER_SPLASH_ENABLED = registerOption(
        new BoolOption(true, "water_splash_enabled")
    );
    public static final ConfigOption<Integer> WATER_SPLASH_AMOUNT_ON_PLACE = registerOption(
        new IntOption(12, "water_splash_amount_on_place")
    );

    public static final ConfigOption<List<Identifier>> LAVA_SPLASH_FLUIDS = registerOption(
        new ResourceLocationListOption(
            List.of(
                RegistryHelpers.getLocationFromFluid(Fluids.LAVA),
                RegistryHelpers.getLocationFromFluid(Fluids.FLOWING_LAVA)
            ),
            "lava_splash_fluids"
        )
    );
    public static final ConfigOption<Boolean> LAVA_SPLASH_ENABLED = registerOption(
        new BoolOption(true, "lava_splash_enabled")
    );
    public static final ConfigOption<Integer> LAVA_SPLASH_AMOUNT_ON_PLACE = registerOption(
        new IntOption(7, "lava_splash_amount_on_place")
    );

    public static final ConfigOption<List<Identifier>> GENERIC_SPLASH_FLUIDS = registerOption(
        new ResourceLocationListOption(
            List.of(),
            "generic_splash_fluids"
        )
    );
    public static final ConfigOption<Boolean> GENERIC_SPLASH_ENABLED = registerOption(
        new BoolOption(true, "generic_splash_enabled")
    );
    public static final ConfigOption<Integer> GENERIC_SPLASH_AMOUNT_ON_PLACE = registerOption(
        new IntOption(10, "generic_splash_amount_on_place")
    );

    private static <T> ConfigOption<T> registerOption(ConfigOption<T> option) {
        return ConfigOptions.registerOption(ConfigCategory.FLUID_PLACEMENT, option);
    }

    public static void init() {}
}
