package games.enchanted.eg_particle_interactions.common.particle.appearance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.appearance.colour.ColourSource;
import games.enchanted.eg_particle_interactions.common.particle.appearance.colour.StaticColourSource;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record ParticleAppearance(@Nullable Sprites sprites, ColourSource colourSource) {
    public static final Codec<ParticleAppearance> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Sprites.CODEC.optionalFieldOf("texture_config").forGetter(textureSource -> Optional.ofNullable(textureSource.sprites))
        ).apply(
            instance,
            sprites -> new ParticleAppearance(sprites.orElse(null), new StaticColourSource(new int[]{255,255,255,255}))
        )
    );

    public record Sprites(List<Identifier> sprites, Identifier atlasId) {
        public static final Codec<Sprites> CODEC = RecordCodecBuilder.create(
            spritesInstance -> spritesInstance.group(
                Codec.list(Identifier.CODEC).fieldOf("sprites").forGetter(o -> null),
                Identifier.CODEC.optionalFieldOf("atlas", AtlasIds.PARTICLES).forGetter(Sprites::atlasId)
            ).apply(
                spritesInstance,
                Sprites::new
            )
        );
    }
}
