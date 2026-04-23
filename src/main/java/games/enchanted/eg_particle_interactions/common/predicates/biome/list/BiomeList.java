package games.enchanted.eg_particle_interactions.common.predicates.biome.list;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.predicates.block.BlockStatePredicate;
import games.enchanted.eg_particle_interactions.common.registry.ObjectOrTagLocation;

import java.util.ArrayList;
import java.util.List;

public record BiomeList(List<ObjectOrTagLocation> biomesAndTags) {
    private static final Codec<List<ObjectOrTagLocation>> BLOCKS_AND_TAGS_CODEC = Codec.list(ObjectOrTagLocation.CODEC);

    public static final Codec<BiomeList> CODEC = BLOCKS_AND_TAGS_CODEC.comapFlatMap(
        list -> DataResult.success(new BiomeList(list)),
        BiomeList::biomesAndTags
    );

    public record File(List<ObjectOrTagLocation> biomesAndTags, List<ObjectOrTagLocation> removals) {
        public static final Codec<File> CODEC = RecordCodecBuilder.create(i ->
            i.group(
                BLOCKS_AND_TAGS_CODEC.optionalFieldOf("biomes", List.of()).forGetter(File::biomesAndTags),
                BLOCKS_AND_TAGS_CODEC.optionalFieldOf("remove_biomes", List.of()).forGetter(File::removals)
            ).apply(
                i,
                File::new
            )
        );

        public static BiomeList combine(List<File> files) {
            List<ObjectOrTagLocation> blocksAndTags = new ArrayList<>();

            for (File file : files) {
                blocksAndTags.addAll(file.biomesAndTags());
                for (ObjectOrTagLocation location : file.removals()) {
                    blocksAndTags.remove(location);
                }
            }

            return new BiomeList(blocksAndTags);
        }
    }
}
