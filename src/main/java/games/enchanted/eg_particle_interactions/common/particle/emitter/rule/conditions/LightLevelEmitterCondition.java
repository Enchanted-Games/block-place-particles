package games.enchanted.eg_particle_interactions.common.particle.emitter.rule.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LightLayer;

public class LightLevelEmitterCondition extends EmitterCondition {
    public static final MapCodec<LightLevelEmitterCondition> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            Vec3i.CODEC.optionalFieldOf(EmitterCondition.POS_OFFSET_FIELD, Vec3i.ZERO).forGetter(LightLevelEmitterCondition::getPosOffset),
            LightRange.CODEC.optionalFieldOf("sky", LightRange.EVERYTHING).forGetter(LightLevelEmitterCondition::getSkyRange),
            LightRange.CODEC.optionalFieldOf("block", LightRange.EVERYTHING).forGetter(LightLevelEmitterCondition::getBlockRange)
        ).apply(
            i,
            LightLevelEmitterCondition::new
        )
    );

    final Vec3i posOffset;
    final LightRange skyRange;
    final LightRange blockRange;

    LightLevelEmitterCondition(Vec3i posOffset, LightRange skyRange, LightRange blockRange) {
        this.posOffset = posOffset;
        this.skyRange = skyRange;
        this.blockRange = blockRange;
    }

    protected Vec3i getPosOffset() {
        return this.posOffset;
    }

    protected LightRange getSkyRange() {
        return this.skyRange;
    }

    protected LightRange getBlockRange() {
        return this.blockRange;
    }

    @Override
    public boolean matches(ParticleContext context) {
        var lightEngine = context.level().getLightEngine();
        int skyLight = lightEngine.getLayerListener(LightLayer.SKY).getLightValue(context.pos().offset(this.posOffset));
        int blockLight = lightEngine.getLayerListener(LightLayer.BLOCK).getLightValue(context.pos().offset(this.posOffset));
        return this.skyRange.containsLevel(skyLight) && this.blockRange.containsLevel(blockLight);
    }

    @Override
    public MapCodec<? extends EmitterCondition> codec() {
        return CODEC;
    }


    protected record LightRange(int min, int max) {
        public static final LightRange EVERYTHING = new LightRange(0, 15);

        static final Codec<LightRange> CODEC = RecordCodecBuilder.create(
            i -> i.group(
                Codec.intRange(0, 15).fieldOf("min").forGetter(LightRange::min),
                Codec.intRange(0, 15).fieldOf("max").forGetter(LightRange::max)
            ).apply(
                i,
                LightRange::new
            )
        );

        public boolean containsLevel(int level) {
            return this.min <= level && level <= this.max;
        }
    }
}
