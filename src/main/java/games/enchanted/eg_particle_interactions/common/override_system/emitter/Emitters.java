package games.enchanted.eg_particle_interactions.common.override_system.emitter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class Emitters {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends Emitter>> EMITTER_TYPES = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<Emitter> CODEC = EMITTER_TYPES.codec(Identifier.CODEC).dispatch("emitter_type", Emitter::codec, mapCodec -> mapCodec);

    static {
        EMITTER_TYPES.put(ParticleInteractionsMod.id("vanilla"), VanillaEmitter.CODEC);
        EMITTER_TYPES.put(ParticleInteractionsMod.id("particle_interactions"), ParticleInteractionsEmitter.CODEC);
        EMITTER_TYPES.put(ParticleInteractionsMod.id("empty"), Emitter.EMPTY_CODEC);
    }
}
