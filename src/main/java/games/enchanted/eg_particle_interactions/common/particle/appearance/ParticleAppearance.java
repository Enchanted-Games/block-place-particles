package games.enchanted.eg_particle_interactions.common.particle.appearance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.particle.appearance.colour.ColourSource;
import games.enchanted.eg_particle_interactions.common.particle.appearance.colour.ColourSources;
import games.enchanted.eg_particle_interactions.common.particle.appearance.texture.TextureConfig;
import games.enchanted.eg_particle_interactions.common.particle.appearance.texture.TextureConfigs;
import games.enchanted.eg_particle_interactions.common.util.ObjectReference;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public record ParticleAppearance(TextureConfig textureConfig, ColourSource colourSource, int lightEmission) {
    private static final int DEFAULT_LIGHT_EMISSION = 0;

    public static final ParticleAppearance FALLBACK_APPEARANCE = new ParticleAppearance(
        TextureConfigs.MISSING,
        ColourSources.WHITE,
        DEFAULT_LIGHT_EMISSION
    );

    public static final Codec<ParticleAppearance> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            TextureConfigs.CODEC.optionalFieldOf("texture_config", TextureConfigs.MISSING).forGetter(ParticleAppearance::textureConfig),
            ColourSources.CODEC.optionalFieldOf("colour").forGetter(appearance -> Optional.of(appearance.colourSource())),
            Codec.intRange(0, 15).optionalFieldOf("light_emission").forGetter(appearance -> Optional.of(appearance.lightEmission()))
        ).apply(
            instance,
            (
                textureConfig,
                colourSource,
                lightEmission
            ) -> new ParticleAppearance(
                textureConfig,
                colourSource.orElse(ColourSources.WHITE),
                lightEmission.orElse(DEFAULT_LIGHT_EMISSION)
            )
        )
    );

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
