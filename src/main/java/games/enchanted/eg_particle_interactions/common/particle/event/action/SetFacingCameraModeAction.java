package games.enchanted.eg_particle_interactions.common.particle.event.action;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.particle.appearance.billboard.CustomFacingCameraMode;

public class SetFacingCameraModeAction extends EventAction {
    public static final MapCodec<SetFacingCameraModeAction> CODEC = CustomFacingCameraMode.MAP_CODEC.xmap(
        SetFacingCameraModeAction::new,
        SetFacingCameraModeAction::getFacingMode
    );

    final CustomFacingCameraMode facingMode;

    SetFacingCameraModeAction(CustomFacingCameraMode facingMode) {
        this.facingMode = facingMode;
    }

    protected CustomFacingCameraMode getFacingMode() {
        return this.facingMode;
    }

    @Override
    public void onFire(ParticleInteractionsParticle particle) {
        particle.setFacingCameraMode(this.facingMode);
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return CODEC;
    }
}
