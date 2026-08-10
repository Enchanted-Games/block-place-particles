package games.enchanted.eg_particle_interactions.common.particle.event.trigger;

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
        EVENT_TYPES.put(ParticleInteractionsMod.id("tick"), TickEventType.CODEC);
        EVENT_TYPES.put(ParticleInteractionsMod.id("on_ground"), OnGroundEventType.CODEC);
        EVENT_TYPES.put(ParticleInteractionsMod.id("in_air"), InAirEventType.CODEC);
        EVENT_TYPES.put(ParticleInteractionsMod.id("on_bounce"), OnBounceEventType.CODEC);
        EVENT_TYPES.put(ParticleInteractionsMod.id("on_spawn"), OnSpawnEventType.CODEC);
        EVENT_TYPES.put(ParticleInteractionsMod.id("in_fluid"), InFluidEventType.CODEC);
        EVENT_TYPES.put(ParticleInteractionsMod.id("exited_fluid"), ExitedFluidEventType.CODEC);
        EVENT_TYPES.put(ParticleInteractionsMod.id("after_ticks"), AfterTicksEventType.CODEC);
        EVENT_TYPES.put(ParticleInteractionsMod.id("after_lifetime_percent"), AfterLifetimePercentEventType.CODEC);
        EVENT_TYPES.put(ParticleInteractionsMod.id("random_chance"), RandomChanceEventType.CODEC);
    }
}
