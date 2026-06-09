package games.enchanted.eg_particle_interactions.common.predicates.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.predicates.entity.list.EntityList;
import games.enchanted.eg_particle_interactions.common.predicates.entity.list.EntityListManager;
import games.enchanted.eg_particle_interactions.common.registry.ObjectOrTagLocation;
import net.minecraft.core.registries.BuiltInRegistries;

public class EntityListPredicate extends EntityPredicate {
    public static final MapCodec<EntityListPredicate> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            EntityListManager.INLINE_OR_ID_CODEC.fieldOf("entities").forGetter(EntityListPredicate::getEntityList)
        ).apply(
            i,
            EntityListPredicate::new
        )
    );

    final EntityList.Reference entityList;

    public EntityListPredicate(EntityList.Reference entityList) {
        this.entityList = entityList;
    }

    protected EntityList.Reference getEntityList() {
        return this.entityList;
    }

    @Override
    public MapCodec<? extends EntityPredicate> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(EntityContext entityContext) {
        return ObjectOrTagLocation.doesListContainObject(entityList.get().entitiesAndTags(), entityContext.entity(), BuiltInRegistries.ENTITY_TYPE);
    }
}
