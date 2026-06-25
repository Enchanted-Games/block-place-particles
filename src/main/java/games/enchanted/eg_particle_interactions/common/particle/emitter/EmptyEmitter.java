package games.enchanted.eg_particle_interactions.common.particle.emitter;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;

public class EmptyEmitter extends Emitter {
    public static final Emitter INSTANCE = new EmptyEmitter();
    static final MapCodec<? extends Emitter> EMPTY_CODEC = MapCodec.unit(INSTANCE);

    EmptyEmitter() {
        super(0, POSITION_OFFSET_DEFAULT);
    }

    @Override
    protected void emit(ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
    }

    @Override
    public MapCodec<? extends Emitter> codec() {
        return EMPTY_CODEC;
    }
}
