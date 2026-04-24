package games.enchanted.eg_particle_interactions.common.particle.emitter.rule.conditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.predicates.block.BlockPredicate;
import games.enchanted.eg_particle_interactions.common.predicates.block.BlockPredicates;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitters;
import net.minecraft.core.Vec3i;

public class BlockPredicateEmitterCondition extends EmitterCondition {
    public static final MapCodec<BlockPredicateEmitterCondition> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            Vec3i.CODEC.optionalFieldOf(EmitterCondition.POS_OFFSET_FIELD, Vec3i.ZERO).forGetter(BlockPredicateEmitterCondition::getPosOffset),
            BlockPredicates.CODEC.fieldOf(EmitterCondition.PREDICATE_FIELD).forGetter(BlockPredicateEmitterCondition::getBlockPredicate)
        ).apply(
            i,
            BlockPredicateEmitterCondition::new
        )
    );

    final Vec3i posOffset;
    final BlockPredicate blockPredicate;

    public BlockPredicateEmitterCondition(Vec3i posOffset, BlockPredicate blockPredicate) {
        this.posOffset = posOffset;
        this.blockPredicate = blockPredicate;
    }

    protected Vec3i getPosOffset() {
        return this.posOffset;
    }

    protected BlockPredicate getBlockPredicate() {
        return this.blockPredicate;
    }

    @Override
    public boolean matches(ParticleContext context) {
        if(context.blockContext() == null) return false;
        if(this.posOffset.toMutable().equals(0, 0, 0)) {
            return this.blockPredicate.matches(context.blockContext().state());
        }
        return this.blockPredicate.matches(context.level().getBlockState(
            context.pos().offset(this.posOffset)
        ));
    }

    @Override
    public MapCodec<? extends BlockPredicateEmitterCondition> codec() {
        return CODEC;
    }
}