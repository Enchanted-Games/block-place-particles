package games.enchanted.eg_particle_interactions.common.resource.texture.palette;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.predicates.block.BlockStatePredicate;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;

public class BlockPaletteManager extends AbstractPaletteManager<BlockState, BlockStatePredicate> {
    public static final BlockPaletteManager INSTANCE = new BlockPaletteManager(
        FileToIdConverter.json("eg_particle_interactions/palettes/blocks"),
        PaletteDefinition.File.BLOCK_CODEC,
        "block"
    );

    public BlockPaletteManager(FileToIdConverter fileToIdConverter, Codec<PaletteDefinition.File<BlockState, BlockStatePredicate>> fileCodec, String typeName) {
        super(fileToIdConverter, fileCodec, typeName);
    }

    @Override
    protected Identifier lookupId(BlockState object) {
        return RegistryHelpers.getLocationFromBlock(object.getBlock());
    }
}
