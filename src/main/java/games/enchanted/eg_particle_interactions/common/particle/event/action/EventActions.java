package games.enchanted.eg_particle_interactions.common.particle.event.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class EventActions {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends EventAction>> EVENT_ACTIONS = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<EventAction> CODEC = EVENT_ACTIONS.codec(ModCodecs.IDENTIFIER).dispatch("type", EventAction::codec, mapCodec -> mapCodec);

    static {
        EVENT_ACTIONS.put(ParticleInteractionsMod.id("modify_gravity"), ModifyGravityAction.CODEC);
        EVENT_ACTIONS.put(ParticleInteractionsMod.id("modify_velocity"), ModifyVelocityAction.CODEC);
        EVENT_ACTIONS.put(ParticleInteractionsMod.id("spawn_particle"), SpawnParticleAction.CODEC);
        EVENT_ACTIONS.put(ParticleInteractionsMod.id("modify_lifetime"), ModifyLifetimeAction.CODEC);
        EVENT_ACTIONS.put(ParticleInteractionsMod.id("kill"), KillParticleAction.CODEC);
        EVENT_ACTIONS.put(ParticleInteractionsMod.id("set_appearance"), ModifyAppearanceAction.CODEC);
        EVENT_ACTIONS.put(ParticleInteractionsMod.id("modify_scale"), ModifyScaleAction.CODEC);
        EVENT_ACTIONS.put(ParticleInteractionsMod.id("modify_model_offset"), ModifyModelOffsetAction.CODEC);
        EVENT_ACTIONS.put(ParticleInteractionsMod.id("set_model_offset"), SetModelOffsetAction.CODEC);
        EVENT_ACTIONS.put(ParticleInteractionsMod.id("modify_light_emission"), ModifyLightEmissionAction.CODEC);
        EVENT_ACTIONS.put(ParticleInteractionsMod.id("age_based_scale"), AgeBasedScale.CODEC);
        EVENT_ACTIONS.put(ParticleInteractionsMod.id("age_based_light_emission"), AgeBasedLightEmission.CODEC);
        EVENT_ACTIONS.put(ParticleInteractionsMod.id("age_based_alpha"), AgeBasedAlpha.CODEC);
        EVENT_ACTIONS.put(ParticleInteractionsMod.id("set_uv"), SetUVAction.CODEC);
    }
}
