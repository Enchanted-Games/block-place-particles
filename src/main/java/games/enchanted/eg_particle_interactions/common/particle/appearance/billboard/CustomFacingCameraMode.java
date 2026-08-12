package games.enchanted.eg_particle_interactions.common.particle.appearance.billboard;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JavaOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.util.math.MathHelper;
import net.minecraft.client.Camera;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.jspecify.annotations.NonNull;

public class CustomFacingCameraMode {
    public static final CustomFacingCameraMode XYZ = new CustomFacingCameraMode(new RotationComponent(-1, CameraValue.PITCH), new RotationComponent(-1, CameraValue.YAW));

    private static final Codec<RotationComponent> COMPONENT_CODEC = Codec.FLOAT.xmap(
        value -> new RotationComponent(value, CameraValue.NONE),
        RotationComponent::value
    ).withAlternative(
        Codec.STRING.xmap(
            s -> {
                DataResult<Pair<CameraValue, Object>> decodedCameraValue = CameraValue.CODEC.decode(JavaOps.INSTANCE, s);
                if(!decodedCameraValue.isSuccess() || decodedCameraValue.result().isEmpty()) {
                    throw new IllegalArgumentException("Invalid variable '" + s + "'.");
                }
                CameraValue val = decodedCameraValue.result().get().getFirst();
                if(!val.isACameraVariable) {
                    throw new IllegalArgumentException("Invalid variable '" + s + "'.");
                }
                return new RotationComponent(-1, val);
            },
            rotationComponent -> rotationComponent.cameraValue().getSerializedName()
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

    private enum CameraValue implements StringRepresentable {
        NONE("none", false),
        PITCH("camera_pitch", true),
        YAW("camera_yaw", true),
        PITCH_POINT_TOWARDS("point_towards_camera_pitch", true, true),
        YAW_POINT_TOWARDS("point_towards_camera_yaw", true, true);

        static final Codec<CameraValue> CODEC = StringRepresentable.fromEnum(CameraValue::values);

        final String name;
        final boolean isACameraVariable;
        final boolean pointTowards;

        CameraValue(String name, boolean isACameraVariable) {
            this(name, isACameraVariable, false);
        }

        CameraValue(String name, boolean isACameraVariable, boolean pointTowards) {
            this.name = name;
            this.isACameraVariable = isACameraVariable;
            this.pointTowards = pointTowards;
        }

        @Override
        public @NonNull String getSerializedName() {
            return this.name;
        }
    }
}
