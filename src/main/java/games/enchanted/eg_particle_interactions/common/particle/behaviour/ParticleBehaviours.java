package games.enchanted.eg_particle_interactions.common.particle.behaviour;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class ParticleBehaviours {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, ParticleBehaviourProvider> BEHAVIOUR_TYPES = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<ParticleBehaviourProvider> CODEC = BEHAVIOUR_TYPES.codec(ModCodecs.IDENTIFIER);
    public static final ParticleBehaviourProvider SIMPLE = new ParticleInteractionsParticle.Provider();

    static {
        BEHAVIOUR_TYPES.put(ParticleInteractionsMod.id("simple"), SIMPLE);
    }
}
