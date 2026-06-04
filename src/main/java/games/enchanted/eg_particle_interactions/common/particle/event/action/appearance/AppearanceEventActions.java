package games.enchanted.eg_particle_interactions.common.particle.event.action.appearance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.event.action.EventAction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class AppearanceEventActions {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends EventAction>> EVENT_ACTIONS = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<EventAction> CODEC = EVENT_ACTIONS.codec(ModCodecs.IDENTIFIER).dispatch("type", EventAction::codec, mapCodec -> mapCodec);

    static {
        EVENT_ACTIONS.put(ParticleInteractionsMod.id("set_appearance"), ModifyAppearanceAction.CODEC);
        EVENT_ACTIONS.put(ParticleInteractionsMod.id("modify_scale"), ModifyScaleAction.CODEC);
        EVENT_ACTIONS.put(ParticleInteractionsMod.id("modify_model_offset"), ModifyModelOffsetAction.CODEC);
        EVENT_ACTIONS.put(ParticleInteractionsMod.id("modify_light_emission"), ModifyLightEmissionAction.CODEC);
    }
}
