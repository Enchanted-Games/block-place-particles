package games.enchanted.eg_particle_interactions.common.predicates.biome.list;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.particle.util.ObjectReference;
import games.enchanted.eg_particle_interactions.common.predicates.ObjectList;
import games.enchanted.eg_particle_interactions.common.predicates.ObjectListFile;
import games.enchanted.eg_particle_interactions.common.registry.ObjectOrTagLocation;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public record BiomeList(List<ObjectOrTagLocation> biomesAndTags) implements ObjectList<BiomeList.File> {
    private static final Codec<List<ObjectOrTagLocation>> BIOMES_AND_TAGS_CODEC = Codec.list(ObjectOrTagLocation.CODEC);

    public static final Codec<BiomeList.Reference> CODEC = BIOMES_AND_TAGS_CODEC.comapFlatMap(
        list -> DataResult.success(new BiomeList.InlineRef(new BiomeList(list))),
        reference -> reference.get().biomesAndTags()
    );

    public record File(List<ObjectOrTagLocation> biomesAndTags, List<ObjectOrTagLocation> removals) implements ObjectListFile {
        public static final Codec<File> CODEC = RecordCodecBuilder.create(i ->
            i.group(
                BIOMES_AND_TAGS_CODEC.optionalFieldOf("biomes", List.of()).forGetter(File::biomesAndTags),
                BIOMES_AND_TAGS_CODEC.optionalFieldOf("remove_biomes", List.of()).forGetter(File::removals)
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

    public static class Reference extends ObjectReference<BiomeList> {
        public Reference(Identifier id) {
            super(id);
        }

        @Override
        protected BiomeList lookupObject() {
            return BiomeListManager.INSTANCE.getOrDefault(this.id());
        }
    }

    public static class InlineRef extends BiomeList.Reference {
        final BiomeList list;

        public InlineRef(BiomeList list) {
            super(ParticleInteractionsMod.id("inline_" + list.hashCode()));
            this.list = list;
        }

        @Override
        protected BiomeList lookupObject() {
            return this.list;
        }
    }
}
