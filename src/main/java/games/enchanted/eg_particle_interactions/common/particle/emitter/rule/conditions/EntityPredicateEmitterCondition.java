package games.enchanted.eg_particle_interactions.common.particle.emitter.rule.conditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.predicates.biome.BiomePredicate;
import games.enchanted.eg_particle_interactions.common.predicates.biome.BiomePredicates;
import games.enchanted.eg_particle_interactions.common.predicates.entity.EntityPredicate;
import games.enchanted.eg_particle_interactions.common.predicates.entity.EntityPredicates;
import games.enchanted.eg_particle_interactions.common.util.BiomeHelpers;
import net.minecraft.core.Vec3i;

public class EntityPredicateEmitterCondition extends EmitterCondition {
    public static final MapCodec<EntityPredicateEmitterCondition> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            Vec3i.CODEC.optionalFieldOf(EmitterCondition.POS_OFFSET_FIELD, Vec3i.ZERO).forGetter(EntityPredicateEmitterCondition::getPosOffset),
            EntityPredicates.CODEC.fieldOf(EmitterCondition.PREDICATE_FIELD).forGetter(EntityPredicateEmitterCondition::getEntityPredicate)
        ).apply(
            i,
            EntityPredicateEmitterCondition::new
        )
    );

    final Vec3i posOffset;
    final EntityPredicate entityPredicate;

    public EntityPredicateEmitterCondition(Vec3i posOffset, EntityPredicate entityPredicate) {
        this.posOffset = posOffset;
        this.entityPredicate = entityPredicate;
    }

    protected Vec3i getPosOffset() {
        return this.posOffset;
    }

    protected EntityPredicate getEntityPredicate() {
        return this.entityPredicate;
    }

    @Override
    public boolean matches(ParticleContext context) {
        if(context.entityTypeContext() == null) return false;
        return this.entityPredicate.matches(new EntityPredicate.EntityContext(
            context.level(),
            context.entityTypeContext().entityType()
        ));
    }

    @Override
    public MapCodec<? extends EntityPredicateEmitterCondition> codec() {
        return CODEC;
    }
}