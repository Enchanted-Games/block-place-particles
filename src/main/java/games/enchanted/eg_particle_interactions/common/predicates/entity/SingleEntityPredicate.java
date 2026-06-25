package games.enchanted.eg_particle_interactions.common.predicates.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class SingleEntityPredicate extends EntityPredicate {
    public static final MapCodec<SingleEntityPredicate> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            Identifier.CODEC.fieldOf("entity").forGetter(SingleEntityPredicate::getEntityId)
        ).apply(
            i,
            SingleEntityPredicate::new
        )
    );

    final Identifier entityId;

    SingleEntityPredicate(Identifier entityId) {
        this.entityId = entityId;
    }

    protected Identifier getEntityId() {
        return this.entityId;
    }

    @Override
    public MapCodec<? extends EntityPredicate> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(EntityContext entityContext) {
        Identifier testId = RegistryHelpers.getIdFromHolder(entityContext.entity(), BuiltInRegistries.ENTITY_TYPE);
        return this.entityId.equals(testId);
    }
}
