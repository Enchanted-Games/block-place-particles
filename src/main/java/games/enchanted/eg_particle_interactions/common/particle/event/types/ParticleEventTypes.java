package games.enchanted.eg_particle_interactions.common.particle.event.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class ParticleEventTypes {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends ParticleEventType>> EVENT_TYPES = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<ParticleEventType> CODEC = EVENT_TYPES.codec(ModCodecs.IDENTIFIER).dispatch("type", ParticleEventType::codec, mapCodec -> mapCodec);

    static {
        EVENT_TYPES.put(ParticleInteractionsMod.id("on_ground"), OnGroundEventType.CODEC);
        EVENT_TYPES.put(ParticleInteractionsMod.id("in_air"), InAirEventType.CODEC);
        EVENT_TYPES.put(ParticleInteractionsMod.id("on_bounce"), OnBounceEventType.CODEC);
    }
}
