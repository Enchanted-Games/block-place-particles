package games.enchanted.eg_particle_interactions.common.particle.appearance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.particle.appearance.colour.ColourSource;
import games.enchanted.eg_particle_interactions.common.particle.appearance.colour.ColourSources;
import games.enchanted.eg_particle_interactions.common.particle.appearance.texture.TextureConfig;
import games.enchanted.eg_particle_interactions.common.particle.appearance.texture.TextureConfigs;
import games.enchanted.eg_particle_interactions.common.particle.event.EventStack;
import games.enchanted.eg_particle_interactions.common.particle.options.value.RandomFloatProvider;
import games.enchanted.eg_particle_interactions.common.util.ObjectReference;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.List;

public record ParticleAppearance(TextureConfig textureConfig, ColourSource colourSource, int lightEmission, SpinConfig spinConfig, RandomFloatProvider scale, Vector3fc modelOffset, List<EventStack.Event> events) {
    private static final int DEFAULT_LIGHT_EMISSION = 0;
    private static final RandomFloatProvider DEFAULT_SCALE = new RandomFloatProvider(List.of(3f));
    private static final Vector3fc MODEL_OFFSET_DEFAULT = new Vector3f(0);

    public static final ParticleAppearance.Reference MISSING_APPEARANCE = new InlineRef(new ParticleAppearance(
        TextureConfigs.MISSING_APPEARANCE,
        ColourSources.WHITE,
        DEFAULT_LIGHT_EMISSION,
        SpinConfig.NO_SPIN,
        DEFAULT_SCALE,
        MODEL_OFFSET_DEFAULT,
        List.of()
    ));

    public static final ParticleAppearance.Reference MISSING_DEFINITION = new InlineRef(new ParticleAppearance(
        TextureConfigs.MISSING_DEFINITION,
        ColourSources.WHITE,
        DEFAULT_LIGHT_EMISSION,
        SpinConfig.NO_SPIN,
        DEFAULT_SCALE,
        MODEL_OFFSET_DEFAULT,
        List.of()
    ));

    public static Codec<ParticleAppearance> codec() {
        return RecordCodecBuilder.create(i -> i
            .group(
                TextureConfigs.CODEC.optionalFieldOf("texture_config", TextureConfigs.MISSING_APPEARANCE).forGetter(ParticleAppearance::textureConfig),
                ColourSources.CODEC.optionalFieldOf("colour", ColourSources.WHITE).forGetter(ParticleAppearance::colourSource),
                Codec.intRange(0, 15).optionalFieldOf("light_emission", DEFAULT_LIGHT_EMISSION).forGetter(ParticleAppearance::lightEmission),
                SpinConfig.CODEC.optionalFieldOf("spin_config", SpinConfig.NO_SPIN).forGetter(ParticleAppearance::spinConfig),
                RandomFloatProvider.CODEC.optionalFieldOf("scale", DEFAULT_SCALE).forGetter(ParticleAppearance::scale),
                ExtraCodecs.VECTOR3F.optionalFieldOf("model_offset", MODEL_OFFSET_DEFAULT).forGetter(ParticleAppearance::modelOffset),
                EventStack.Event.appearanceCodec().listOf().optionalFieldOf("events", List.of()).forGetter(ParticleAppearance::events)
            ).apply(
                i,
                ParticleAppearance::new
            )
        );
    }

    public static class Reference extends ObjectReference<ParticleAppearance> {
        public Reference(Identifier id) {
            super(id);
        }

        @Override
        protected ParticleAppearance lookupObject() {
            return ParticleAppearanceManager.get(this.id());
        }
    }

    public static class InlineRef extends Reference {
        final ParticleAppearance appearance;

        public InlineRef(ParticleAppearance appearance) {
            super(ParticleInteractionsMod.id("inline_" + appearance.hashCode()));
            this.appearance = appearance;
        }

        @Override
        protected ParticleAppearance lookupObject() {
            return this.appearance;
        }
    }
}
