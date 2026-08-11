package games.enchanted.eg_particle_interactions.common.particle.appearance.billboard;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.util.math.MathHelper;
import net.minecraft.client.Camera;

public class FixedCameraMode extends FacingCameraModeDefinition {
    private static final Codec<RotationComponent> COMPONENT_CODEC = Codec.FLOAT.xmap(
        value -> new RotationComponent(value, CameraValue.NONE),
        RotationComponent::value
    ).withAlternative(
        Codec.STRING.xmap(
            s -> {
                if(s.equals("camera_pitch")) {
                    return new RotationComponent(-1, CameraValue.PITCH);
                } else if(s.equals("camera_yaw")) {
                    return new RotationComponent(-1, CameraValue.YAW);
                }
                throw new IllegalArgumentException("Must be either 'camera_pitch' or 'camera_yaw'");
            },
            rotationComponent -> rotationComponent.cameraValue().serializedName()
        )
    );

    public static final MapCodec<FixedCameraMode> CODEC = RecordCodecBuilder.mapCodec(i -> i
        .group(
            COMPONENT_CODEC.optionalFieldOf("pitch", new RotationComponent(0f, CameraValue.NONE)).forGetter(d -> d.pitch),
            COMPONENT_CODEC.optionalFieldOf("yaw", new RotationComponent(0f, CameraValue.NONE)).forGetter(d -> d.yaw)
        ).apply(
            i,
            FixedCameraMode::new
        )
    );

    final RotationComponent pitch;
    final RotationComponent yaw;
    final FacingCameraMode mode;

    FixedCameraMode(RotationComponent pitch, RotationComponent yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.mode = (quaternion, camera) -> quaternion.set(MathHelper.eulerAnglesToQuaternion(
            (float) Math.toRadians(pitch.getValueDegrees(camera)),
            (float) Math.toRadians(yaw.getValueDegrees(camera)),
            0f
        ));
    }

    @Override
    public FacingCameraMode facingCameraMode() {
        return this.mode;
    }

    @Override
    public MapCodec<? extends FacingCameraModeDefinition> codec() {
        return null;
    }

    private record RotationComponent(float value, CameraValue cameraValue) {
        float getValueDegrees(Camera camera) {
            if(this.cameraValue() == CameraValue.PITCH) return 360 - camera.xRot();
            if(this.cameraValue() == CameraValue.YAW) return 180 - camera.yRot();
            return this.value;
        }
    }

    private enum CameraValue {
        NONE("none"),
        PITCH("pitch"),
        YAW("yaw");

        final String name;

        CameraValue(String name) {
            this.name = name;
        }

        public String serializedName() {
            return this.name;
        }
    }
}
