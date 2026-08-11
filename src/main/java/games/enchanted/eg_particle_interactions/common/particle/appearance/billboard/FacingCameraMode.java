package games.enchanted.eg_particle_interactions.common.particle.appearance.billboard;

import games.enchanted.eg_particle_interactions.common.util.math.MathHelper;
import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public interface FacingCameraMode {
    FacingCameraMode NONE = (quaternion, camera, pos) -> quaternion.set(0.0f, 0.0f, 0.0f, camera.rotation().w);
    FacingCameraMode XYZ = (quaternion, camera, pos) -> quaternion.set(camera.rotation());
    FacingCameraMode HORIZONTAL = (quaternion, camera, pos) -> quaternion.set(
        MathHelper.eulerAnglesToQuaternion(
            0f,
            (float) Math.toRadians(180 - camera.yRot()),
            0f
        )
    );

    void rotate(Quaternionf quaternion, Camera camera, Vec3 pos);
}
