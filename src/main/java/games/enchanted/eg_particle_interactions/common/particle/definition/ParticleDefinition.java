package games.enchanted.eg_particle_interactions.common.particle.definition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.behaviour.ParticleBehaviourProvider;
import games.enchanted.eg_particle_interactions.common.particle.behaviour.ParticleBehaviours;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.util.ObjectReference;
import net.minecraft.resources.Identifier;

public record ParticleDefinition(ParticleComponentMap defaultComponents, ParticleBehaviourProvider behaviourProvider) {
    public static final ParticleDefinition FALLBACK = new ParticleDefinition(ParticleComponentMap.Builder.create().build(), ParticleBehaviours.SIMPLE);

    public static final Codec<ParticleDefinition> CODEC = RecordCodecBuilder.create(
        i -> i.group(
            ParticleComponentMap.CODEC.optionalFieldOf("components", ParticleComponentMap.EMPTY).forGetter(ParticleDefinition::defaultComponents),
            ParticleBehaviours.CODEC.optionalFieldOf("special_type", ParticleBehaviours.SIMPLE).forGetter(ParticleDefinition::behaviourProvider)
        ).apply(
            i,
            ParticleDefinition::new
        )
    );

    public static class Reference extends ObjectReference<ParticleDefinition> {
        public Reference(Identifier id) {
            super(id);
        }

        @Override
        protected ParticleDefinition lookupObject() {
            return ParticleDefinitionManager.INSTANCE.getOrThrow(this.id());
        }
    }
}
