package games.enchanted.eg_particle_interactions.common.particle.event.action.appearance;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearanceManager;
import games.enchanted.eg_particle_interactions.common.particle.event.action.EventAction;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;

public class ModifyAppearanceAction extends EventAction {
    public static final MapCodec<ModifyAppearanceAction> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            ParticleAppearanceManager.referenceCodec().fieldOf("appearance").forGetter(ModifyAppearanceAction::getAppearance)
        ).apply(
            i,
            ModifyAppearanceAction::new
        )
    );

    final ParticleAppearance.Reference appearance;

    ModifyAppearanceAction(ParticleAppearance.Reference appearance) {
        this.appearance = appearance;
    }

    @Override
    public void onFire(ParticleInteractionsParticle particle) {
        particle.setAppearance(this.appearance.get());
    }

    protected ParticleAppearance.Reference getAppearance() {
        return this.appearance;
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return CODEC;
    }
}
