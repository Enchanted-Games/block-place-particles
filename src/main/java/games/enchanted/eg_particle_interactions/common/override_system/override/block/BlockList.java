package games.enchanted.eg_particle_interactions.common.override_system.override.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.registry.BlockOrTagLocation;

import java.util.ArrayList;
import java.util.List;

public record BlockList(List<BlockOrTagLocation> blocksAndTags) {
    private static final Codec<List<BlockOrTagLocation>> BLOCKS_AND_TAGS_CODEC = Codec.list(BlockOrTagLocation.CODEC);

    public static final Codec<BlockList> CODEC = BLOCKS_AND_TAGS_CODEC.comapFlatMap(
        list -> DataResult.success(new BlockList(list)),
        BlockList::blocksAndTags
    );

    public record File(List<BlockOrTagLocation> blocksAndTags, List<BlockOrTagLocation> removals) {
        public static final Codec<File> CODEC = RecordCodecBuilder.create(i ->
            i.group(
                BLOCKS_AND_TAGS_CODEC.optionalFieldOf("blocks", List.of()).forGetter(File::blocksAndTags),
                BLOCKS_AND_TAGS_CODEC.optionalFieldOf("remove", List.of()).forGetter(File::removals)
            ).apply(
                i,
                File::new
            )
        );

        public static BlockList combine(List<File> files) {
            List<BlockOrTagLocation> blocksAndTags = new ArrayList<>();
            for (File file : files) {
                blocksAndTags.addAll(file.blocksAndTags());
                for (BlockOrTagLocation location : file.removals()) {
                    blocksAndTags.remove(location);
                }
            }
            return new BlockList(blocksAndTags);
        }
    }
}
