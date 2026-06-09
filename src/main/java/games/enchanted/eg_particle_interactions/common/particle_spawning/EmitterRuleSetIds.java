package games.enchanted.eg_particle_interactions.common.particle_spawning;

import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.EmitterRuleSet;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.EmitterRuleSetManager;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public class EmitterRuleSetIds {
    public static final Supplier<EmitterRuleSet> FIRE_PLACED = ref(ParticleInteractionsMod.id("interaction/fire_placed"));
    public static final Supplier<EmitterRuleSet> FLINT_AND_STEEL_USE = ref(ParticleInteractionsMod.id("interaction/flint_and_steel_use"));

    public static final Supplier<EmitterRuleSet> ANVIL_USE_EMISSION = ref(ParticleInteractionsMod.id("interaction/anvil_use_emission"));
    public static final Supplier<EmitterRuleSet> GRINDSTONE_USE_EMISSION = ref(ParticleInteractionsMod.id("interaction/grindstone_use_emission"));

    public static final Supplier<EmitterRuleSet> BRUSH_DUST = ref(ParticleInteractionsMod.id("interaction/brush_dust_emission"));

    public static final Supplier<EmitterRuleSet> ITEM_FRAME_EMISSION = ref(ParticleInteractionsMod.id("interaction/item_frame_emission"));


    public static final Supplier<EmitterRuleSet> LIGHTNING_ARCS = ref(ParticleInteractionsMod.id("ambient/lightning_arcs"));

    public static final Supplier<EmitterRuleSet> CAMPFIRE_SPARKS = ref(ParticleInteractionsMod.id("ambient/campfire/sparks"));

    public static final Supplier<EmitterRuleSet> FIRE_SPARKS = ref(ParticleInteractionsMod.id("ambient/fire/sparks"));

    public static final Supplier<EmitterRuleSet> FURNACE_FRONT = ref(ParticleInteractionsMod.id("ambient/furnace/furnace_front"));
    public static final Supplier<EmitterRuleSet> BLAST_FURNACE_FRONT = ref(ParticleInteractionsMod.id("ambient/furnace/blast_furnace_front"));


    private static Supplier<EmitterRuleSet> ref(Identifier id) {
        return () -> EmitterRuleSetManager.getRuleSet(id);
    }
}
