package games.enchanted.eg_particle_interactions.common.particle.emitter.rule;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitters;
import games.enchanted.eg_particle_interactions.common.particle.emitter.EmptyEmitter;
import games.enchanted.eg_particle_interactions.common.util.ObjectReference;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public record EmitterRuleSet(List<EmitterRule> rules, Emitter fallbackEmitter) {
    public static final EmitterRuleSet.InlineRef EMPTY = new InlineRef(new EmitterRuleSet(List.of(), EmptyEmitter.INSTANCE));

    public static final Codec<EmitterRuleSet> CODEC = File.CODEC.xmap(
        file -> combineFiles(List.of(file)),
        emitterRuleSet -> new File(emitterRuleSet.rules, emitterRuleSet.fallbackEmitter)
    );

    public static final Codec<EmitterRuleSet.Reference> INLINE_REFERENCE_CODEC = CODEC.xmap(
        InlineRef::new,
        Reference::lookupObject
    );

    public Emitter getEmitter(ParticleContext context) {
        for (EmitterRule rule : this.rules()) {
            if(!rule.matches(context)) continue;
            return rule.emitter();
        }
        return this.fallbackEmitter();
    }

    public static EmitterRuleSet combineFiles(List<File> files) {
        List<EmitterRule> rules = new ArrayList<>();
        Emitter fallback = EmptyEmitter.INSTANCE;

        for (File file : files) {
            rules.addAll(file.rules());

            if(file.fallbackEmitter() instanceof EmptyEmitter) continue;
            fallback = file.fallbackEmitter();
        }

        return new EmitterRuleSet(List.copyOf(rules), fallback);
    }

    public record File(List<EmitterRule> rules, Emitter fallbackEmitter) {
        public static final Codec<EmitterRuleSet.File> CODEC = RecordCodecBuilder.create(
            i -> i.group(
                Codec.list(EmitterRule.CODEC).optionalFieldOf("rules", List.of()).forGetter(EmitterRuleSet.File::rules),
                Emitters.CODEC.fieldOf("fallback_emitter").forGetter(EmitterRuleSet.File::fallbackEmitter)
            ).apply(
                i,
                EmitterRuleSet.File::new
            )
        );
    }

    public static class Reference extends ObjectReference<EmitterRuleSet> {
        public Reference(Identifier id) {
            super(id);
        }

        @Override
        protected EmitterRuleSet lookupObject() {
            return EmitterRuleSetManager.getRuleSet(id());
        }
    }

    public static class InlineRef extends Reference {
        final EmitterRuleSet value;

        public InlineRef(EmitterRuleSet value) {
            super(ParticleInteractionsMod.id("inline_" + value.hashCode()));
            this.value = value;
        }

        @Override
        protected EmitterRuleSet lookupObject() {
            return this.value;
        }
    }
}
