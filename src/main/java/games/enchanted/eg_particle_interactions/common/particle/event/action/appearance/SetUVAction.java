package games.enchanted.eg_particle_interactions.common.particle.event.action.appearance;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.appearance.uv.UVProvider;
import games.enchanted.eg_particle_interactions.common.particle.appearance.uv.UVProviders;
import games.enchanted.eg_particle_interactions.common.particle.event.action.EventAction;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;

public class SetUVAction extends EventAction {
    public static final MapCodec<SetUVAction> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            UVProviders.CODEC.fieldOf("uv").forGetter(SetUVAction::getUvProvider)
        )
        .apply(
            i,
            SetUVAction::new
        )
    );

    final UVProvider uvProvider;

    SetUVAction(UVProvider uvProvider) {
        this.uvProvider = uvProvider;
    }

    protected UVProvider getUvProvider() {
        return this.uvProvider;
    }

    @Override
    public void onFire(ParticleInteractionsParticle particle) {
        particle.modifyUV(this.uvProvider);
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return CODEC;
    }
}
