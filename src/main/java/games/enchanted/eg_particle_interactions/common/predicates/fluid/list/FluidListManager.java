package games.enchanted.eg_particle_interactions.common.predicates.fluid.list;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.util.ObjectReference;
import games.enchanted.eg_particle_interactions.common.predicates.AbstractListManager;
import games.enchanted.eg_particle_interactions.common.registry.ObjectOrTagLocation;
import net.minecraft.resources.FileToIdConverter;

import java.util.List;

public class FluidListManager extends AbstractListManager<FluidList.File, FluidList> {
    public static final Codec<FluidList.Reference> INLINE_OR_ID_CODEC = FluidList.REFERENCE_CODEC.withAlternative(
        ModCodecs.IDENTIFIER.xmap(
            FluidList.Reference::new,
            ObjectReference::id
        )
    );

    public static final FluidListManager INSTANCE = new FluidListManager();

    public FluidListManager() {
        super(FileToIdConverter.json(Constants.MOD_ID + "/lists/fluids"), "fluid");
    }

    @Override
    protected Codec<FluidList.File> fileCodec() {
        return FluidList.File.CODEC;
    }

    @Override
    protected FluidList listMaker(List<ObjectOrTagLocation> objectOrTagLocations) {
        return new FluidList(objectOrTagLocations, List.of());
    }

    @Override
    protected FluidList combineFiles(List<FluidList.File> files) {
        return FluidList.File.combine(files);
    }
}
