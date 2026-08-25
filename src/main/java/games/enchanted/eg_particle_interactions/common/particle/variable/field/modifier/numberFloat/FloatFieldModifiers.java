package games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.numberFloat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.variable.field.modifier.FieldModifier;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class FloatFieldModifiers {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends FieldModifier<Float>>> FLOAT_MODIFIERS = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<FieldModifier<Float>> CODEC = Codec.withAlternative(
        FLOAT_MODIFIERS.codec(ModCodecs.IDENTIFIER).dispatch("value", FieldModifier::codec, mapCodec -> mapCodec),
        Codec.FLOAT.xmap(
            StaticValueFloatModifier::new,
            StaticValueFloatModifier::getValue
        )
    );

    static {
        FLOAT_MODIFIERS.put(ParticleInteractionsMod.id("initial"), NoOpFloatModifier.CODEC);
        FLOAT_MODIFIERS.put(ParticleInteractionsMod.id("static"), StaticValueFloatModifier.CODEC);
        FLOAT_MODIFIERS.put(ParticleInteractionsMod.id("modify_initial"), ModifyFloatModifier.CODEC);
    }
}
