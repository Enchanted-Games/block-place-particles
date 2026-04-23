package games.enchanted.eg_particle_interactions.common.particle.emitter.rule.types;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.predicates.block.BlockPredicate;
import games.enchanted.eg_particle_interactions.common.predicates.block.BlockPredicates;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitters;
import net.minecraft.core.Vec3i;

public class BlockPredicateEmitterRuleType extends EmitterRuleType {
    public static final MapCodec<BlockPredicateEmitterRuleType> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            Emitters.CODEC.fieldOf(EmitterRuleType.EMITTER_FIELD).forGetter(EmitterRuleType::getEmitter),
            Vec3i.CODEC.optionalFieldOf(EmitterRuleType.POS_OFFSET_FIELD, Vec3i.ZERO).forGetter(BlockPredicateEmitterRuleType::getPosOffset),
            BlockPredicates.CODEC.fieldOf(EmitterRuleType.PREDICATE_FIELD).forGetter(BlockPredicateEmitterRuleType::getBlockPredicate)
        ).apply(
            i,
            BlockPredicateEmitterRuleType::new
        )
    );

    final Vec3i posOffset;
    final BlockPredicate blockPredicate;

    public BlockPredicateEmitterRuleType(Emitter emitter, Vec3i posOffset, BlockPredicate blockPredicate) {
        super(emitter);
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
    public MapCodec<? extends BlockPredicateEmitterRuleType> codec() {
        return CODEC;
    }
}