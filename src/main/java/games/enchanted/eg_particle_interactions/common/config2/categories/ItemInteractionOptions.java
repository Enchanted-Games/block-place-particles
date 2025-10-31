package games.enchanted.eg_particle_interactions.common.config2.categories;

import games.enchanted.eg_particle_interactions.common.config.type.BrushParticleBehaviour;
import games.enchanted.eg_particle_interactions.common.config2.ConfigCategory;
import games.enchanted.eg_particle_interactions.common.config2.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.config2.option.BoolOption;
import games.enchanted.eg_particle_interactions.common.config2.option.ConfigOption;
import games.enchanted.eg_particle_interactions.common.config2.option.IntOption;
import games.enchanted.eg_particle_interactions.common.config2.option.enums.BrushParticleBehaviourOption;

public class ItemInteractionOptions {
    public static final ConfigOption<BrushParticleBehaviour> BRUSH_PARTICLE_BEHAVIOUR = registerOption(
        new BrushParticleBehaviourOption(BrushParticleBehaviour.BLOCK_OVERRIDE_OR_DUST, "brush_particle_behaviour")
    );

    public static final ConfigOption<Boolean> FLINT_AND_STEEL_SPARKS_ENABLED = registerOption(
        new BoolOption(true, "flint_and_steel_sparks_enabled")
    );
    public static final ConfigOption<Integer> FLINT_AND_STEEL_SPARKS_INTENSITY = registerOption(
        new IntOption(5, "flint_and_steel_sparks_intensity")
    );
    public static final ConfigOption<Integer> FLINT_AND_STEEL_SPARKS_AMOUNT = registerOption(
        new IntOption(12, "flint_and_steel_sparks_amount")
    );

    public static final ConfigOption<Boolean> FIRE_CHARGE_PARTICLES_ENABLED = registerOption(
        new BoolOption(true, "fire_charge_particles_enabled")
    );
    public static final ConfigOption<Integer> FIRE_CHARGE_PARTICLES_INTENSITY = registerOption(
        new IntOption(5, "fire_charge_particles_intensity")
    );
    public static final ConfigOption<Integer> FIRE_CHARGE_PARTICLES_AMOUNT = registerOption(
        new IntOption(12, "fire_charge_particles_amount")
    );

    public static final ConfigOption<Boolean> AXE_STRIP_ENABLED = registerOption(
        new BoolOption(true, "axe_strip_enabled")
    );
    public static final ConfigOption<Integer> AXE_STRIP_AMOUNT = registerOption(
        new IntOption(12, "axe_strip_amount")
    );

    public static final ConfigOption<Boolean> HOE_TILL_ENABLED = registerOption(
        new BoolOption(true, "hoe_till_enabled")
    );
    public static final ConfigOption<Integer> HOE_TILL_AMOUNT = registerOption(
        new IntOption(12, "hoe_till_amount")
    );

    public static final ConfigOption<Boolean> SHOVEL_FLATTEN_ENABLED = registerOption(
        new BoolOption(true, "shovel_flatten_enabled")
    );
    public static final ConfigOption<Integer> SHOVEL_FLATTEN_AMOUNT = registerOption(
        new IntOption(12, "shovel_flatten_amount")
    );

    public static final ConfigOption<Boolean> HONEY_COLLECTION_ENABLED = registerOption(
        new BoolOption(true, "honey_collection_enabled")
    );
    public static final ConfigOption<Integer> HONEY_COLLECTION_AMOUNT = registerOption(
        new IntOption(12, "honey_collection_amount")
    );
    public static final ConfigOption<Boolean> HONEY_COLLECTION_REPLACE_VANILLA = registerOption(
        new BoolOption(true, "honey_collection_replace_vanilla")
    );

    private static <T> ConfigOption<T> registerOption(ConfigOption<T> option) {
        return ConfigOptions.registerOption(ConfigCategory.ITEM_INTERACTIONS, option);
    }

    public static void init() {}
}
