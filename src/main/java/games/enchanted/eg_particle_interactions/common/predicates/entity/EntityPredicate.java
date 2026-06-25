package games.enchanted.eg_particle_interactions.common.predicates.entity;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.predicates.ObjectPredicate;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class EntityPredicate implements ObjectPredicate<EntityPredicate.EntityContext> {
    public abstract MapCodec<? extends EntityPredicate> codec();

    public record EntityContext(Level level, Holder<EntityType<?>> entity) {
    }
}
