package games.enchanted.eg_particle_interactions.common.particle.definition;

import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public class ParticleIDs {
    public static final Supplier<ParticleDefinition> FLYING_SPARK = ref(ParticleInteractionsMod.id("spark/flying_spark"));

    private static Supplier<ParticleDefinition> ref(Identifier id) {
        return () -> ParticleDefinitionManager.INSTANCE.getOrFallback(id);
    }
}
