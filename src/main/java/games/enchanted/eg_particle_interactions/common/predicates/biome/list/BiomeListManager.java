package games.enchanted.eg_particle_interactions.common.predicates.biome.list;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.util.ObjectReference;
import games.enchanted.eg_particle_interactions.common.predicates.AbstractListManager;
import games.enchanted.eg_particle_interactions.common.registry.ObjectOrTagLocation;
import net.minecraft.resources.FileToIdConverter;

import java.util.List;

public class BiomeListManager extends AbstractListManager<BiomeList.File, BiomeList> {
    public static final Codec<BiomeList.Reference> INLINE_OR_ID_CODEC = BiomeList.CODEC.withAlternative(
        ModCodecs.IDENTIFIER.xmap(
            BiomeList.Reference::new,
            ObjectReference::id
        )
    );

    public static final BiomeListManager INSTANCE = new BiomeListManager();

    public BiomeListManager() {
        super(FileToIdConverter.json(Constants.MOD_ID + "/lists/biomes"), "biome");
    }

    @Override
    protected Codec<BiomeList.File> fileCodec() {
        return BiomeList.File.CODEC;
    }

    @Override
    protected BiomeList listMaker(List<ObjectOrTagLocation> objectOrTagLocations) {
        return new BiomeList(objectOrTagLocations);
    }

    @Override
    protected BiomeList combineFiles(List<BiomeList.File> files) {
        return BiomeList.File.combine(files);
    }
}
