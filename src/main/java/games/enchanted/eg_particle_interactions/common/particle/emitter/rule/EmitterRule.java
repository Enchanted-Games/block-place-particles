package games.enchanted.eg_particle_interactions.common.particle.emitter.rule;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.EmptyEmitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.types.EmitterRuleType;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.types.EmitterRuleTypes;

import java.util.ArrayList;
import java.util.List;

public record EmitterRule(List<EmitterRuleType> rules) {
    public static final EmitterRule EMPTY = new EmitterRule(List.of());

    public Emitter getEmitter(ParticleContext context) {
        return this.getEmitter(context, EmptyEmitter.INSTANCE);
    }

    public Emitter getEmitter(ParticleContext context, Emitter fallback) {
        for (EmitterRuleType rule : rules()) {
            if(!rule.matches(context)) continue;
            return rule.getEmitter();
        }
        return fallback;
    }

    public static EmitterRule combineFiles(List<File> files) {
        List<EmitterRuleType> rules = new ArrayList<>();

        for (File file : files) {
            rules.addAll(file.rules());
        }

        return new EmitterRule(List.copyOf(rules));
    }

    public record File(List<EmitterRuleType> rules) {
        public static final Codec<File> CODEC = RecordCodecBuilder.create(
            i -> i.group(
                Codec.list(EmitterRuleTypes.CODEC).fieldOf("rules").forGetter(File::rules)
            ).apply(
                i,
                File::new
            )
        );
    }
}
