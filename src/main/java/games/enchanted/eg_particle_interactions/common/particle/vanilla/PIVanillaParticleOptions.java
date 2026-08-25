package games.enchanted.eg_particle_interactions.common.particle.vanilla;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public class PIVanillaParticleOptions implements ParticleOptions {
    final ParticleType<PIVanillaParticleOptions> type;
    public final ParticleComponentMap components;
    public final Identifier definitionId;

    public PIVanillaParticleOptions(ParticleType<PIVanillaParticleOptions> type, ParticleComponentMap components, Identifier definition) {
        this.type = type;
        this.components = components;
        this.definitionId = definition;
    }

    @Override
    public ParticleType<?> getType() {
        return this.type;
    }

    private static Codec<PIVanillaParticleOptions> createCodec(ParticleType<PIVanillaParticleOptions> type) {
        return RecordCodecBuilder.create(i ->
            i.group(
                ParticleComponentMap.CODEC.optionalFieldOf("components", ParticleComponentMap.EMPTY).forGetter(o -> o.components),
                ModCodecs.IDENTIFIER.fieldOf("definition").forGetter(o -> o.definitionId)
            ).apply(
                i,
                (components, definitionId) -> {
                    return new PIVanillaParticleOptions(type, components, definitionId);
                }
            )
        );
    }

    public static MapCodec<PIVanillaParticleOptions> codec(ParticleType<PIVanillaParticleOptions> type) {
        return createCodec(type).fieldOf("config");
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, PIVanillaParticleOptions> streamCodec(ParticleType<PIVanillaParticleOptions> type) {
        return ByteBufCodecs.fromCodec(createCodec(type));
    }
}
