package games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.numberInt;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.FieldModifier;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class IntFieldModifiers {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends FieldModifier<Integer>>> INT_MODIFIERS = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<FieldModifier<Integer>> CODEC = Codec.withAlternative(
        INT_MODIFIERS.codec(ModCodecs.IDENTIFIER).dispatch("value", FieldModifier::codec, mapCodec -> mapCodec),
        Codec.INT.xmap(
            StaticValueIntModifier::new,
            StaticValueIntModifier::getValue
        )
    );

    static {
        INT_MODIFIERS.put(ParticleInteractionsMod.id("initial"), NoOpIntModifier.CODEC);
        INT_MODIFIERS.put(ParticleInteractionsMod.id("static"), StaticValueIntModifier.CODEC);
        INT_MODIFIERS.put(ParticleInteractionsMod.id("modify_initial"), ModifyIntModifier.CODEC);
    }
}
