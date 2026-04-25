package games.enchanted.eg_particle_interactions.common.particle.emitter.rule.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LightLayer;

public class RawLightEmitterCondition extends EmitterCondition {
    public static final MapCodec<RawLightEmitterCondition> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            Vec3i.CODEC.optionalFieldOf(EmitterCondition.POS_OFFSET_FIELD, Vec3i.ZERO).forGetter(RawLightEmitterCondition::getPosOffset),
            Codec.intRange(0, 15).fieldOf("min").forGetter(RawLightEmitterCondition::getMin),
            Codec.intRange(0, 15).fieldOf("max").forGetter(RawLightEmitterCondition::getMax)
        ).apply(
            i,
            RawLightEmitterCondition::new
        )
    );

    final Vec3i posOffset;
    final int min;
    final int max;

    RawLightEmitterCondition(Vec3i posOffset, int min, int max) {
        this.posOffset = posOffset;
        this.min = min;
        this.max = max;
    }

    protected Vec3i getPosOffset() {
        return this.posOffset;
    }

    protected int getMin() {
        return this.min;
    }

    protected int getMax() {
        return this.max;
    }

    @Override
    public boolean matches(ParticleContext context) {
        int level = context.level().getMaxLocalRawBrightness(context.pos().offset(this.posOffset));
        return this.min <= level && level <= this.max;
    }

    @Override
    public MapCodec<? extends EmitterCondition> codec() {
        return CODEC;
    }
}
