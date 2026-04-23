package games.enchanted.eg_particle_interactions.common.predicates.biome;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.predicates.ObjectPredicate;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public abstract class BiomePredicate implements ObjectPredicate<BiomePredicate.BiomeContext> {
    public abstract MapCodec<? extends BiomePredicate> codec();

    public record BiomeContext(Level level, Holder<Biome> biome) {
    }
}
