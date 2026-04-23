package games.enchanted.eg_particle_interactions.common.predicates.block.list;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.predicates.block.BlockStatePredicate;
import games.enchanted.eg_particle_interactions.common.registry.ObjectOrTagLocation;

import java.util.ArrayList;
import java.util.List;

public record BlockList(List<ObjectOrTagLocation> blocksAndTags, List<BlockStatePredicate> statePredicates) {
    private static final Codec<List<ObjectOrTagLocation>> BLOCKS_AND_TAGS_CODEC = Codec.list(ObjectOrTagLocation.CODEC);

    public static final Codec<BlockList> CODEC = BLOCKS_AND_TAGS_CODEC.comapFlatMap(
        list -> DataResult.success(new BlockList(list, List.of())),
        BlockList::blocksAndTags
    );

    public record File(List<ObjectOrTagLocation> blocksAndTags, List<ObjectOrTagLocation> removals, List<BlockStatePredicate> statePredicates) {
        public static final Codec<File> CODEC = RecordCodecBuilder.create(i ->
            i.group(
                BLOCKS_AND_TAGS_CODEC.optionalFieldOf("blocks", List.of()).forGetter(File::blocksAndTags),
                BLOCKS_AND_TAGS_CODEC.optionalFieldOf("remove_blocks", List.of()).forGetter(File::removals),
                Codec.list(BlockStatePredicate.CODEC.codec()).optionalFieldOf("block_states", List.of()).forGetter(File::statePredicates)
            ).apply(
                i,
                File::new
            )
        );

        public static BlockList combine(List<File> files) {
            List<ObjectOrTagLocation> blocksAndTags = new ArrayList<>();
            List<BlockStatePredicate> statePredicates = new ArrayList<>();

            for (File file : files) {
                blocksAndTags.addAll(file.blocksAndTags());
                for (ObjectOrTagLocation location : file.removals()) {
                    blocksAndTags.remove(location);
                }
                statePredicates.addAll(file.statePredicates());
            }

            return new BlockList(blocksAndTags, statePredicates);
        }
    }
}
