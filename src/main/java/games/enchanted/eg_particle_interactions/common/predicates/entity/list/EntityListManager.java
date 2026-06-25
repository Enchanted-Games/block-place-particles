package games.enchanted.eg_particle_interactions.common.predicates.entity.list;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.predicates.AbstractListManager;
import games.enchanted.eg_particle_interactions.common.registry.ObjectOrTagLocation;
import games.enchanted.eg_particle_interactions.common.util.ObjectReference;
import net.minecraft.resources.FileToIdConverter;

import java.util.List;

public class EntityListManager extends AbstractListManager<EntityList.File, EntityList> {
    public static final Codec<EntityList.Reference> INLINE_OR_ID_CODEC = EntityList.CODEC.withAlternative(
        ModCodecs.IDENTIFIER.xmap(
            EntityList.Reference::new,
            ObjectReference::id
        )
    );

    public static final EntityListManager INSTANCE = new EntityListManager();

    public EntityListManager() {
        super(FileToIdConverter.json(Constants.MOD_ID + "/lists/entities"), "entity");
    }

    @Override
    protected Codec<EntityList.File> fileCodec() {
        return EntityList.File.CODEC;
    }

    @Override
    protected EntityList listMaker(List<ObjectOrTagLocation> objectOrTagLocations) {
        return new EntityList(objectOrTagLocations);
    }

    @Override
    protected EntityList combineFiles(List<EntityList.File> files) {
        return EntityList.File.combine(files);
    }
}
