package games.enchanted.eg_particle_interactions.common.predicates.entity.list;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.predicates.ObjectList;
import games.enchanted.eg_particle_interactions.common.predicates.ObjectListFile;
import games.enchanted.eg_particle_interactions.common.registry.ObjectOrTagLocation;
import games.enchanted.eg_particle_interactions.common.util.ObjectReference;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public record EntityList(List<ObjectOrTagLocation> entitiesAndTags) implements ObjectList<EntityList.File> {
    private static final Codec<List<ObjectOrTagLocation>> ENTITIES_AND_TAGS_CODEC = Codec.list(ObjectOrTagLocation.CODEC);

    public static final Codec<Reference> CODEC = ENTITIES_AND_TAGS_CODEC.comapFlatMap(
        list -> DataResult.success(new InlineRef(new EntityList(list))),
        reference -> reference.get().entitiesAndTags()
    );

    public record File(List<ObjectOrTagLocation> entitiesAndTags, List<ObjectOrTagLocation> removals) implements ObjectListFile {
        public static final Codec<File> CODEC = RecordCodecBuilder.create(i ->
            i.group(
                ENTITIES_AND_TAGS_CODEC.optionalFieldOf("entities", List.of()).forGetter(File::entitiesAndTags),
                ENTITIES_AND_TAGS_CODEC.optionalFieldOf("remove_entities", List.of()).forGetter(File::removals)
            ).apply(
                i,
                File::new
            )
        );

        public static EntityList combine(List<File> files) {
            List<ObjectOrTagLocation> entitiesAndTags = new ArrayList<>();

            for (File file : files) {
                entitiesAndTags.addAll(file.entitiesAndTags());
                for (ObjectOrTagLocation location : file.removals()) {
                    entitiesAndTags.remove(location);
                }
            }

            return new EntityList(entitiesAndTags);
        }
    }

    public static class Reference extends ObjectReference<EntityList> {
        public Reference(Identifier id) {
            super(id);
        }

        @Override
        protected EntityList lookupObject() {
            return EntityListManager.INSTANCE.getOrDefault(this.id());
        }
    }

    public static class InlineRef extends Reference {
        final EntityList list;

        public InlineRef(EntityList list) {
            super(ParticleInteractionsMod.id("inline_" + list.hashCode()));
            this.list = list;
        }

        @Override
        protected EntityList lookupObject() {
            return this.list;
        }
    }
}
