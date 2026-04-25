package games.enchanted.eg_particle_interactions.common.particle.emitter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public class RandomEmitter extends Emitter {
    public static final MapCodec<RandomEmitter> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Codec.list(EmitterAndWeight.CODEC).fieldOf("emitters").forGetter(RandomEmitter::getEmitters)
        ).apply(
            instance,
            RandomEmitter::new
        )
    );

    final List<EmitterAndWeight> emitters;
    final int totalWeights;

    public RandomEmitter(List<EmitterAndWeight> emitters) {
        super(0);
        this.emitters = emitters;

        final int[] x = {0};
        this.emitters.forEach(emitterAndWeight -> x[0] += emitterAndWeight.weight());
        this.totalWeights = x[0];
    }

    @Override
    public void spawnParticle(ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        if(this.emitters.isEmpty()) {
            return;
        }

        int rand = (int) Math.round(Math.random() * this.totalWeights);
        Emitter emitter = this.emitters.getFirst().emitter();
        for (EmitterAndWeight emitterAndWeight : this.emitters) {
            if (rand < emitterAndWeight.weight()) {
                emitter = emitterAndWeight.emitter();
                break;
            }
            rand -= emitterAndWeight.weight();
        }

        emitter.spawnParticle(context, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    protected List<EmitterAndWeight> getEmitters() {
        return this.emitters;
    }

    @Override
    public MapCodec<? extends Emitter> codec() {
        return CODEC;
    }

    public record EmitterAndWeight(Emitter emitter, int weight) {
        static final Codec<EmitterAndWeight> CODEC = RecordCodecBuilder.create(
            i -> i.group(
                Emitters.CODEC.fieldOf("emitter").forGetter(EmitterAndWeight::emitter),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("weight").forGetter(EmitterAndWeight::weight)
            ).apply(
                i,
                EmitterAndWeight::new
            )
        );
    }
}
