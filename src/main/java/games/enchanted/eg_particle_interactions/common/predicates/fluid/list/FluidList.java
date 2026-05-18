package games.enchanted.eg_particle_interactions.common.predicates.fluid.list;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.particle.util.ObjectReference;
import games.enchanted.eg_particle_interactions.common.predicates.ObjectList;
import games.enchanted.eg_particle_interactions.common.predicates.ObjectListFile;
import games.enchanted.eg_particle_interactions.common.predicates.fluid.FluidStatePredicate;
import games.enchanted.eg_particle_interactions.common.registry.ObjectOrTagLocation;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public record FluidList(List<ObjectOrTagLocation> blocksAndTags, List<FluidStatePredicate> statePredicates) implements ObjectList<FluidList.File> {
    private static final Codec<List<ObjectOrTagLocation>> BLOCKS_AND_TAGS_CODEC = Codec.list(ObjectOrTagLocation.CODEC);

    public static final Codec<FluidList.Reference> REFERENCE_CODEC = BLOCKS_AND_TAGS_CODEC.comapFlatMap(
        list -> DataResult.success(new InlineRef(new FluidList(list, List.of()))),
        ref -> ref.get().blocksAndTags()
    );

    public record File(List<ObjectOrTagLocation> blocksAndTags, List<ObjectOrTagLocation> removals, List<FluidStatePredicate> fluidPredicates) implements ObjectListFile {
        public static final Codec<FluidList.File> CODEC = RecordCodecBuilder.create(i ->
            i.group(
                BLOCKS_AND_TAGS_CODEC.optionalFieldOf("blocks", List.of()).forGetter(FluidList.File::blocksAndTags),
                BLOCKS_AND_TAGS_CODEC.optionalFieldOf("remove_blocks", List.of()).forGetter(FluidList.File::removals),
                Codec.list(FluidStatePredicate.CODEC.codec()).optionalFieldOf("fluid_states", List.of()).forGetter(FluidList.File::fluidPredicates)
            ).apply(
                i,
                FluidList.File::new
            )
        );

        public static FluidList combine(List<FluidList.File> files) {
            List<ObjectOrTagLocation> blocksAndTags = new ArrayList<>();
            List<FluidStatePredicate> statePredicates = new ArrayList<>();

            for (FluidList.File file : files) {
                blocksAndTags.addAll(file.blocksAndTags());
                for (ObjectOrTagLocation location : file.removals()) {
                    blocksAndTags.remove(location);
                }
                statePredicates.addAll(file.fluidPredicates());
            }

            return new FluidList(blocksAndTags, statePredicates);
        }
    }

    public static class Reference extends ObjectReference<FluidList> {
        public Reference(Identifier id) {
            super(id);
        }

        @Override
        protected FluidList lookupObject() {
            return FluidListManager.INSTANCE.getOrDefault(this.id());
        }
    }

    public static class InlineRef extends Reference {
        final FluidList list;

        public InlineRef(FluidList list) {
            super(ParticleInteractionsMod.id("inline_" + list.hashCode()));
            this.list = list;
        }

        @Override
        protected FluidList lookupObject() {
            return this.list;
        }
    }
}
