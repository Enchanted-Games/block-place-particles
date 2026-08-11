package games.enchanted.eg_particle_interactions.common.particle.appearance.billboard;

import com.mojang.serialization.MapCodec;

public class PresetCameraMode extends FacingCameraModeDefinition {
    public static final FacingCameraModeDefinition XYZ = new PresetCameraMode(FacingCameraMode.XYZ);

    final FacingCameraMode mode;

    public PresetCameraMode(FacingCameraMode mode) {
        this.mode = mode;
    }

    @Override
    public FacingCameraMode facingCameraMode() {
        return this.mode;
    }

    @Override
    public MapCodec<? extends FacingCameraModeDefinition> codec() {
        return MapCodec.unit(this);
    }
}
