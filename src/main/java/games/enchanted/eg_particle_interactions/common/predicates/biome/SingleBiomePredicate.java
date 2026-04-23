package games.enchanted.eg_particle_interactions.common.predicates.biome;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import net.minecraft.resources.Identifier;

public class SingleBiomePredicate extends BiomePredicate {
    public static final MapCodec<SingleBiomePredicate> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            Identifier.CODEC.fieldOf("biome").forGetter(SingleBiomePredicate::getBiomeId)
        ).apply(
            i,
            SingleBiomePredicate::new
        )
    );

    final Identifier biomeId;

    SingleBiomePredicate(Identifier biomeId) {
        this.biomeId = biomeId;
    }

    protected Identifier getBiomeId() {
        return this.biomeId;
    }

    @Override
    public MapCodec<? extends BiomePredicate> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(BiomeContext biomeContext) {
        Identifier testId = RegistryHelpers.getIdFromHolder(biomeContext.biome(), RegistryHelpers.getBiomeRegistry(biomeContext.level()));
        return this.biomeId.equals(testId);
    }
}
