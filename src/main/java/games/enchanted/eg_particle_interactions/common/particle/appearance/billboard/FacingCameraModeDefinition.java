package games.enchanted.eg_particle_interactions.common.particle.appearance.billboard;

import com.mojang.serialization.MapCodec;

public abstract class FacingCameraModeDefinition {
    public abstract FacingCameraMode facingCameraMode();
    public abstract MapCodec<? extends FacingCameraModeDefinition> codec();
}
