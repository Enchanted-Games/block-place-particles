package games.enchanted.eg_particle_interactions.common.resource.texture.palette;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.predicates.ObjectPredicate;
import games.enchanted.eg_particle_interactions.common.predicates.block.BlockStatePredicate;
import games.enchanted.eg_particle_interactions.common.predicates.fluid.FluidStatePredicate;
import games.enchanted.eg_particle_interactions.common.resource.texture.palette.types.PaletteType;
import games.enchanted.eg_particle_interactions.common.resource.texture.palette.types.PaletteTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import java.util.ArrayList;
import java.util.List;

public class PaletteDefinition<T, P extends ObjectPredicate<T>> {
    private static final String RULES_FIELD = "rules";
    private static final String PALETTE_FIELD = "palette";

    private final PaletteType defaultType;
    private final List<Rule<T, P>> rules;

    private PaletteDefinition(PaletteType defaultType, List<Rule<T, P>> rules) {
        this.defaultType = defaultType;
        this.rules = rules;
    }

    public Palette getPalette(T object) {
        for (Rule<T, P> rule : this.rules) {
            if(rule.predicate().matches(object)) return rule.type().getOrCreatePalette();
        }
        return this.defaultType.getOrCreatePalette();
    }

    public static <T, P extends ObjectPredicate<T>> PaletteDefinition<T, P> combineFiles(List<File<T, P>> files) {
        if(files.isEmpty()) {
            throw new IllegalStateException("No default palette type");
        }

        PaletteType defaultType = null;
        List<Rule<T, P>> rules = new ArrayList<>();

        for (File<T, P> file : files) {
            defaultType = file.defaultType();
            rules.addAll(file.rules());
        }

        return new PaletteDefinition<>(defaultType, List.copyOf(rules));
    }

    public record Rule<T, P extends ObjectPredicate<T>>(P predicate, PaletteType type) {
        public static final Codec<Rule<BlockState, BlockStatePredicate>> BLOCK_RULE_CODEC = RecordCodecBuilder.create(i -> i
            .group(
                BlockStatePredicate.CODEC.fieldOf("block_state_predicate").forGetter(Rule::predicate),
                PaletteTypes.CODEC.fieldOf(PALETTE_FIELD).forGetter(Rule::type)
            ).apply(
                i,
                Rule::new
            )
        );

        public static final Codec<Rule<FluidState, FluidStatePredicate>> FLUID_RULE_CODEC = RecordCodecBuilder.create(i -> i
            .group(
                FluidStatePredicate.CODEC.fieldOf("fluid_state_predicate").forGetter(Rule::predicate),
                PaletteTypes.CODEC.fieldOf(PALETTE_FIELD).forGetter(Rule::type)
            ).apply(
                i,
                Rule::new
            )
        );
    }

    public static class File<T, P extends ObjectPredicate<T>> {
        public static final Codec<File<BlockState, BlockStatePredicate>> BLOCK_CODEC = RecordCodecBuilder.create(i -> i
            .group(
                PaletteTypes.CODEC.fieldOf("default").forGetter(File::defaultType),
                Rule.BLOCK_RULE_CODEC.listOf().optionalFieldOf(RULES_FIELD, List.of()).forGetter(File::rules)
            ).apply(
                i,
                File::new
            )
        );

        public static final Codec<File<FluidState, FluidStatePredicate>> FLUID_CODEC = RecordCodecBuilder.create(i -> i
            .group(
                PaletteTypes.CODEC.fieldOf("default").forGetter(File::defaultType),
                Rule.FLUID_RULE_CODEC.listOf().optionalFieldOf(RULES_FIELD, List.of()).forGetter(File::rules)
            ).apply(
                i,
                File::new
            )
        );

        private final PaletteType defaultType;
        private final List<Rule<T, P>> rules;

        private File(PaletteType defaultType, List<Rule<T, P>> rules) {
            this.defaultType = defaultType;
            this.rules = rules;
        }

        private PaletteType defaultType() {
            return this.defaultType;
        }

        private List<Rule<T, P>> rules() {
            return this.rules;
        }
    }
}
