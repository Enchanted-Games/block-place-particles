package games.enchanted.eg_particle_interactions.common.particle.appearance.billboard;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.util.math.MathHelper;
import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;

public class CustomFacingCameraMode {
    public static final CustomFacingCameraMode XYZ = new CustomFacingCameraMode(new RotationComponent(-1, CameraValue.PITCH), new RotationComponent(-1, CameraValue.YAW));

    private static final Codec<RotationComponent> COMPONENT_CODEC = Codec.FLOAT.xmap(
        value -> new RotationComponent(value, CameraValue.NONE),
        RotationComponent::value
    ).withAlternative(
        Codec.STRING.xmap(
            s -> {
                if(s.equals(CameraValue.PITCH.serializedName())) {
                    return new RotationComponent(-1, CameraValue.PITCH);
                } else if(s.equals(CameraValue.YAW.serializedName())) {
                    return new RotationComponent(-1, CameraValue.YAW);
                } else if(s.equals(CameraValue.PITCH_POINT_TOWARDS.serializedName())) {
                    return new RotationComponent(-1, CameraValue.PITCH_POINT_TOWARDS);
                } else if(s.equals(CameraValue.YAW_POINT_TOWARDS.serializedName())) {
                    return new RotationComponent(-1, CameraValue.YAW_POINT_TOWARDS);
                }
                throw new IllegalArgumentException("Invalid variable '" + s + "'.");
            },
            rotationComponent -> rotationComponent.cameraValue().serializedName()
        )
    );

    public static final Codec<CustomFacingCameraMode> CODEC = RecordCodecBuilder.create(i -> i
        .group(
            COMPONENT_CODEC.optionalFieldOf("pitch", new RotationComponent(0f, CameraValue.NONE)).forGetter(d -> d.pitch),
            COMPONENT_CODEC.optionalFieldOf("yaw", new RotationComponent(0f, CameraValue.NONE)).forGetter(d -> d.yaw)
        ).apply(
            i,
            CustomFacingCameraMode::new
        )
    );

    final RotationComponent pitch;
    final RotationComponent yaw;
    final FacingCameraMode mode;

    CustomFacingCameraMode(RotationComponent pitch, RotationComponent yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.mode = (quaternion, camera, pos) -> quaternion.set(MathHelper.eulerAnglesToQuaternion(
            (float) Math.toRadians(pitch.getValueDegrees(camera, pos)),
            (float) Math.toRadians(yaw.getValueDegrees(camera, pos)),
            0f
        ));
    }

    public FacingCameraMode mode() {
        return this.mode;
    }

    private record RotationComponent(float value, CameraValue cameraValue) {
        float getValueDegrees(Camera camera, Vec3 pos) {
            if(this.cameraValue() == CameraValue.PITCH) return 360 - camera.xRot();
            else if(this.cameraValue() == CameraValue.YAW) return 180 - camera.yRot();
            else if(this.cameraValue().pointTowards) {
                Vector2f angles = MathHelper.getAnglesBetweenPoints(pos.toVector3f(), camera.position().toVector3f());
                return (float) (this.cameraValue() == CameraValue.PITCH_POINT_TOWARDS ? Math.toDegrees(angles.x()) : Math.toDegrees(angles.y()));
            }
            return this.value;
        }
    }

    private enum CameraValue {
        NONE("none"),
        PITCH("camera_pitch"),
        YAW("camera_yaw"),
        PITCH_POINT_TOWARDS("camera_pitch_point", true),
        YAW_POINT_TOWARDS("camera_yaw_point", true);

        final String name;
        final boolean pointTowards;

        CameraValue(String name) {
            this(name, false);
        }

        CameraValue(String name, boolean pointTowards) {
            this.name = name;
            this.pointTowards = pointTowards;
        }


        public String serializedName() {
            return this.name;
        }
    }
}
