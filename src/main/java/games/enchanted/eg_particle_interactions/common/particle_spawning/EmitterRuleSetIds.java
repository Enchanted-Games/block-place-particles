package games.enchanted.eg_particle_interactions.common.particle_spawning;

import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.EmitterRuleSet;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.EmitterRuleSetManager;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public class EmitterRuleSetIds {
    private static final String INTERACTION_DIR = "interaction/";
    private static final String AMBIENT_DIR = "ambient/";

    public static final Supplier<EmitterRuleSet> FIRE_PLACED = ref(ParticleInteractionsMod.id(INTERACTION_DIR + "fire_placed"));
    public static final Supplier<EmitterRuleSet> FLINT_AND_STEEL_USE = ref(ParticleInteractionsMod.id(INTERACTION_DIR + "flint_and_steel_use"));

    public static final Supplier<EmitterRuleSet> ANVIL_USE_EMISSION = ref(ParticleInteractionsMod.id(INTERACTION_DIR + "anvil_use_emission"));
    public static final Supplier<EmitterRuleSet> GRINDSTONE_USE_EMISSION = ref(ParticleInteractionsMod.id(INTERACTION_DIR + "grindstone_use_emission"));

    public static final Supplier<EmitterRuleSet> BRUSH_DUST = ref(ParticleInteractionsMod.id(INTERACTION_DIR + "brush_dust_emission"));

    public static final Supplier<EmitterRuleSet> ITEM_FRAME_EMISSION = ref(ParticleInteractionsMod.id(INTERACTION_DIR + "item_frame_emission"));

    public static final Supplier<EmitterRuleSet> HONEY_COLLECTION = ref(ParticleInteractionsMod.id(INTERACTION_DIR + "honey_collection"));

    public static final Supplier<EmitterRuleSet> BLOCK_PLACED_UNDERWATER = ref(ParticleInteractionsMod.id(INTERACTION_DIR + "block_placed_underwater"));
    public static final Supplier<EmitterRuleSet> BLOCK_BROKEN_UNDERWATER = ref(ParticleInteractionsMod.id(INTERACTION_DIR + "block_broken_underwater"));


    public static final Supplier<EmitterRuleSet> LIGHTNING_ARCS = ref(ParticleInteractionsMod.id(AMBIENT_DIR + "lightning_arcs"));

    public static final Supplier<EmitterRuleSet> CAMPFIRE_SPARKS = ref(ParticleInteractionsMod.id(AMBIENT_DIR + "campfire/sparks"));

    public static final Supplier<EmitterRuleSet> FIRE_SPARKS = ref(ParticleInteractionsMod.id(AMBIENT_DIR + "fire/sparks"));

    public static final Supplier<EmitterRuleSet> FURNACE_FRONT = ref(ParticleInteractionsMod.id(AMBIENT_DIR + "furnace/furnace_front"));
    public static final Supplier<EmitterRuleSet> BLAST_FURNACE_FRONT = ref(ParticleInteractionsMod.id(AMBIENT_DIR + "furnace/blast_furnace_front"));

    public static final Supplier<EmitterRuleSet> BEEHIVE_DRIP = ref(ParticleInteractionsMod.id(AMBIENT_DIR + "beehive_drip"));

    public static final Supplier<EmitterRuleSet> UNDERWATER_BUBBLE_STREAM = ref(ParticleInteractionsMod.id(AMBIENT_DIR + "underwater_bubble_stream"));


    private static Supplier<EmitterRuleSet> ref(Identifier id) {
        return () -> EmitterRuleSetManager.getRuleSet(id);
    }
}
