package games.enchanted.eg_particle_interactions.common.particle.event.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.appearance.uv.UVProvider;
import games.enchanted.eg_particle_interactions.common.particle.appearance.uv.UVProviders;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;

public class SetUVAction extends EventAction {
    public static final MapCodec<SetUVAction> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            UVProviders.CODEC.fieldOf("uv").forGetter(SetUVAction::getUvProvider),
            Codec.BOOL.optionalFieldOf("modify_mask_uv", false).forGetter(SetUVAction::getModifyMaskUv)
        )
        .apply(
            i,
            SetUVAction::new
        )
    );

    final UVProvider uvProvider;
    final boolean modifyMaskUv;

    SetUVAction(UVProvider uvProvider, boolean modifyMaskUv) {
        this.uvProvider = uvProvider;
        this.modifyMaskUv = modifyMaskUv;
    }

    protected UVProvider getUvProvider() {
        return this.uvProvider;
    }

    protected boolean getModifyMaskUv() {
        return this.modifyMaskUv;
    }

    @Override
    public void onFire(ParticleInteractionsParticle particle) {
        particle.modifyUV(this.uvProvider, this.modifyMaskUv);
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return CODEC;
    }
}
