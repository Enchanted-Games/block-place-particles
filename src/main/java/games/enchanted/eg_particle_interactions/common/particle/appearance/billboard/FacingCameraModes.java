package games.enchanted.eg_particle_interactions.common.particle.appearance.billboard;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public class FacingCameraModes {
    public static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends FacingCameraModeDefinition>> PROVIDERS = new ExtraCodecs.LateBoundIdMapper<>();
    public static final Codec<FacingCameraModeDefinition> CODEC = PROVIDERS.codec(ModCodecs.IDENTIFIER).dispatch("type", FacingCameraModeDefinition::codec, mapCodec -> mapCodec);

    static {
        PROVIDERS.put(ParticleInteractionsMod.id("xyz"), PresetCameraMode.XYZ.codec());
        PROVIDERS.put(ParticleInteractionsMod.id("none"), new PresetCameraMode(FacingCameraMode.NONE).codec());
        PROVIDERS.put(ParticleInteractionsMod.id("horizontal"), new PresetCameraMode(FacingCameraMode.HORIZONTAL).codec());
        PROVIDERS.put(ParticleInteractionsMod.id("fixed"), FixedCameraMode.CODEC);
    }
}
