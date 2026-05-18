package games.enchanted.eg_particle_interactions.common.predicates.block.list;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.util.ObjectReference;
import games.enchanted.eg_particle_interactions.common.predicates.AbstractListManager;
import games.enchanted.eg_particle_interactions.common.predicates.fluid.list.FluidList;
import games.enchanted.eg_particle_interactions.common.registry.ObjectOrTagLocation;
import net.minecraft.resources.FileToIdConverter;

import java.util.List;

public class BlockListManager extends AbstractListManager<BlockList.File, BlockList> {
    public static final Codec<BlockList.Reference> INLINE_OR_ID_CODEC = BlockList.REFERENCE_CODEC.withAlternative(
        ModCodecs.IDENTIFIER.xmap(
            BlockList.Reference::new,
            ObjectReference::id
        )
    );

    public static final BlockListManager INSTANCE = new BlockListManager();

    public BlockListManager() {
        super(FileToIdConverter.json(Constants.MOD_ID + "/lists/blocks"), "block");
    }

    @Override
    protected Codec<BlockList.File> fileCodec() {
        return BlockList.File.CODEC;
    }

    @Override
    protected BlockList listMaker(List<ObjectOrTagLocation> objectOrTagLocations) {
        return new BlockList(objectOrTagLocations, List.of());
    }

    @Override
    protected BlockList combineFiles(List<BlockList.File> files) {
        return BlockList.File.combine(files);
    }
}
