package games.enchanted.eg_particle_interactions.common.predicates.biome;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.predicates.biome.list.BiomeList;
import games.enchanted.eg_particle_interactions.common.predicates.biome.list.BiomeListManager;
import games.enchanted.eg_particle_interactions.common.registry.ObjectOrTagLocation;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;

public class BiomeListPredicate extends BiomePredicate {
    public static final MapCodec<BiomeListPredicate> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            BiomeListManager.INLINE_OR_ID_CODEC.fieldOf("biomes").forGetter(BiomeListPredicate::getBiomeList)
        ).apply(
            i,
            BiomeListPredicate::new
        )
    );

    final BiomeList biomeList;

    public BiomeListPredicate(BiomeList biomeList) {
        this.biomeList = biomeList;
    }

    protected BiomeList getBiomeList() {
        return this.biomeList;
    }

    @Override
    public MapCodec<? extends BiomePredicate> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(BiomeContext biomeContext) {
        return ObjectOrTagLocation.doesListContainObject(biomeList.biomesAndTags(), biomeContext.biome(), RegistryHelpers.getBiomeRegistry(biomeContext.level()));
    }
}
