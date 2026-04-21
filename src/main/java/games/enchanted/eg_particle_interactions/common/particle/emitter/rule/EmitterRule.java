package games.enchanted.eg_particle_interactions.common.particle.emitter.rule;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitters;
import games.enchanted.eg_particle_interactions.common.particle.emitter.EmptyEmitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.types.EmitterRuleType;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.types.EmitterRuleTypes;

import java.util.ArrayList;
import java.util.List;

public record EmitterRule(List<EmitterRuleType> rules, Emitter fallbackEmitter) {
    public static final EmitterRule EMPTY = new EmitterRule(List.of(), EmptyEmitter.INSTANCE);

    public Emitter getEmitter(ParticleContext context) {
        for (EmitterRuleType rule : rules()) {
            if(!rule.matches(context)) continue;
            return rule.getEmitter();
        }
        return this.fallbackEmitter();
    }

    public static EmitterRule combineFiles(List<File> files) {
        List<EmitterRuleType> rules = new ArrayList<>();
        Emitter fallback = EmptyEmitter.INSTANCE;

        for (File file : files) {
            rules.addAll(file.rules());

            if(file.fallbackEmitter() instanceof EmptyEmitter) continue;
            fallback = file.fallbackEmitter();
        }

        return new EmitterRule(List.copyOf(rules), fallback);
    }

    public record File(List<EmitterRuleType> rules, Emitter fallbackEmitter) {
        public static final Codec<File> CODEC = RecordCodecBuilder.create(
            i -> i.group(
                Codec.list(EmitterRuleTypes.CODEC).optionalFieldOf("rules", List.of()).forGetter(File::rules),
                Emitters.CODEC.optionalFieldOf("fallback_emitter",EmptyEmitter.INSTANCE).forGetter(File::fallbackEmitter)
            ).apply(
                i,
                File::new
            )
        );
    }
}
