package games.enchanted.eg_particle_interactions.common.resource.texture.palette;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.predicates.block.BlockStatePredicate;
import games.enchanted.eg_particle_interactions.common.predicates.fluid.FluidStatePredicate;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class FluidPaletteManager extends AbstractPaletteManager<FluidState, FluidStatePredicate> {
    public static final FluidPaletteManager INSTANCE = new FluidPaletteManager(
        FileToIdConverter.json("eg_particle_interactions/palettes/fluids"),
        PaletteDefinition.File.FLUID_CODEC,
        "fluid"
    );

    public FluidPaletteManager(FileToIdConverter fileToIdConverter, Codec<PaletteDefinition.File<FluidState, FluidStatePredicate>> fileCodec, String typeName) {
        super(fileToIdConverter, fileCodec, typeName);
    }

    @Override
    protected Identifier lookupId(FluidState object) {
        return RegistryHelpers.getLocationFromFluid(object.getType());
    }
}
