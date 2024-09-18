package games.enchanted.blockplaceparticles.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class StretchyBouncyQuadParticle extends BouncyParticle {
    private double prevPrevX;
    private double prevPrevY;
    private double prevPrevZ;

    /**
     * A single quad particle that stretches between its current and previous position when moving, this particle also has bounce physics
     * Set {@link #physics_canBounce} in your particle constructor to disable bouncing
     *
     * @param level  level
     * @param x      x pos
     * @param y      y pos
     * @param z      z pos
     * @param xSpeed x velocity
     * @param ySpeed y velocity
     * @param zSpeed z velocity
     */
    protected StretchyBouncyQuadParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.prevPrevX = this.xo;
        this.prevPrevY = this.yo;
        this.prevPrevZ = this.zo;
    }

    @Override
    public void tick() {
        this.prevPrevX = this.xo;
        this.prevPrevY = this.yo;
        this.prevPrevZ = this.zo;

        super.tick();
    }

    protected boolean isParticleMoving() {
        return !(this.prevPrevX == this.x && this.prevPrevY == this.y && this.prevPrevZ == this.z);
    }

    @Override
    public @NotNull FacingCameraMode getFacingCameraMode() {
        if(!this.isParticleMoving()) {
            return FacingCameraMode.LOOKAT_XYZ;
        }
        return (quaternionf, camera, d) -> quaternionf.set(0.0f, 0.0f, 0.0f, camera.rotation().w);
    }

    @Override
    protected void renderRotatedQuad(@NotNull VertexConsumer consumer, @NotNull Camera camera, @NotNull Quaternionf quaternionf, float d) {
        Vec3 cameraPosition = camera.getPosition();
        float xPos = (float) ((Mth.lerp(d,  this.xo, this.x) - cameraPosition.x()) );
        float yPos = (float) ((Mth.lerp(d,  this.yo, this.y) - cameraPosition.y()) );
        float zPos = (float) ((Mth.lerp(d,  this.zo, this.z) - cameraPosition.z()) );
        Vector3f pos = new Vector3f(xPos, yPos, zPos);
        float prevXPos = (float) ((Mth.lerp(d,  this.prevPrevX, this.xo) - cameraPosition.x()) );
        float prevYPos = (float) ((Mth.lerp(d,  this.prevPrevY, this.yo) - cameraPosition.y()) );
        float prevZPos = (float) ((Mth.lerp(d,  this.prevPrevZ, this.zo) - cameraPosition.z()) );
        Vector3f prevPos = new Vector3f(prevXPos, prevYPos, prevZPos);
        this.renderSparkQuads(consumer, quaternionf, pos, prevPos, d);
    }

    private void renderSparkQuads(@NotNull VertexConsumer consumer, @NotNull Quaternionf quaternionf, Vector3f pos, Vector3f prevPos, float d) {
        float quadSize = this.getQuadSize(d);
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int lightColor = this.getLightColor(d);

        if(!this.isParticleMoving()) {
            // render a normal quad when the particle isnt moving
            this.renderVertex(consumer, quaternionf, pos.x    , pos.y    , pos.z    ,  1.0f, -1.0f, quadSize, u1, v1, lightColor); // bottom left
            this.renderVertex(consumer, quaternionf, prevPos.x, prevPos.y, prevPos.z,  1.0f,  1.0f, quadSize, u1, v0, lightColor); // top right
            this.renderVertex(consumer, quaternionf, prevPos.x, prevPos.y, prevPos.z, -1.0f,  1.0f, quadSize, u0, v0, lightColor); // top left
            this.renderVertex(consumer, quaternionf, pos.x    , pos.y    , pos.z    , -1.0f, -1.0f, quadSize, u0, v1, lightColor); // bottom right
            return;
        }

        Vector3f particleDir = new Vector3f(0f, 0f, 0f);
        pos.sub(prevPos, particleDir);
        particleDir.x = Math.abs(0f - (Math.clamp(particleDir.x, -0.015f, 0.015f) + 0.015f)) - 0.015f;
        particleDir.y = Math.abs(0f - (Math.clamp(particleDir.y, -0.015f, 0.015f) + 0.015f)) - 0.015f;
        particleDir.z = Math.abs(0f - (Math.clamp(particleDir.z, -0.015f, 0.015f) + 0.015f)) - 0.015f;

        quaternionf.rotateLocalY((float) Math.toRadians(45));
        this.renderVertex(consumer, quaternionf, pos.x     - particleDir.x, pos.y     - particleDir.y, pos.z     - particleDir.z,  1.0f, -1.0f, quadSize, u1, v1, lightColor); // bottom left
        this.renderVertex(consumer, quaternionf, prevPos.x - particleDir.x, prevPos.y - particleDir.y, prevPos.z - particleDir.z,  1.0f,  1.0f, quadSize, u1, v0, lightColor); // top right
        this.renderVertex(consumer, quaternionf, prevPos.x + particleDir.x, prevPos.y + particleDir.y, prevPos.z + particleDir.z, -1.0f,  1.0f, quadSize, u0, v0, lightColor); // top left
        this.renderVertex(consumer, quaternionf, pos.x     + particleDir.x, pos.y     + particleDir.y, pos.z     + particleDir.z, -1.0f, -1.0f, quadSize, u0, v1, lightColor); // bottom right

        this.renderVertex(consumer, quaternionf, pos.x     + particleDir.x, pos.y     + particleDir.y, pos.z     + particleDir.z, -1.0f, -1.0f, quadSize, u0, v1, lightColor); // bottom right
        this.renderVertex(consumer, quaternionf, prevPos.x + particleDir.x, prevPos.y + particleDir.y, prevPos.z + particleDir.z, -1.0f,  1.0f, quadSize, u0, v0, lightColor); // top left
        this.renderVertex(consumer, quaternionf, prevPos.x - particleDir.x, prevPos.y - particleDir.y, prevPos.z - particleDir.z,  1.0f,  1.0f, quadSize, u1, v0, lightColor); // top right
        this.renderVertex(consumer, quaternionf, pos.x     - particleDir.x, pos.y     - particleDir.y, pos.z     - particleDir.z,  1.0f, -1.0f, quadSize, u1, v1, lightColor); // bottom left

        quaternionf.rotateLocalY((float) Math.toRadians(135));
        this.renderVertex(consumer, quaternionf, pos.x     - particleDir.x, pos.y     - particleDir.y, pos.z     - particleDir.z,  1.0f, -1.0f, quadSize, u1, v1, lightColor); // bottom left
        this.renderVertex(consumer, quaternionf, prevPos.x - particleDir.x, prevPos.y - particleDir.y, prevPos.z - particleDir.z,  1.0f,  1.0f, quadSize, u1, v0, lightColor); // top right
        this.renderVertex(consumer, quaternionf, prevPos.x + particleDir.x, prevPos.y + particleDir.y, prevPos.z + particleDir.z, -1.0f,  1.0f, quadSize, u0, v0, lightColor); // top left
        this.renderVertex(consumer, quaternionf, pos.x     + particleDir.x, pos.y     + particleDir.y, pos.z     + particleDir.z, -1.0f, -1.0f, quadSize, u0, v1, lightColor); // bottom right

        this.renderVertex(consumer, quaternionf, pos.x     + particleDir.x, pos.y     + particleDir.y, pos.z     + particleDir.z, -1.0f, -1.0f, quadSize, u0, v1, lightColor); // bottom right
        this.renderVertex(consumer, quaternionf, prevPos.x + particleDir.x, prevPos.y + particleDir.y, prevPos.z + particleDir.z, -1.0f,  1.0f, quadSize, u0, v0, lightColor); // top left
        this.renderVertex(consumer, quaternionf, prevPos.x - particleDir.x, prevPos.y - particleDir.y, prevPos.z - particleDir.z,  1.0f,  1.0f, quadSize, u1, v0, lightColor); // top right
        this.renderVertex(consumer, quaternionf, pos.x     - particleDir.x, pos.y     - particleDir.y, pos.z     - particleDir.z,  1.0f, -1.0f, quadSize, u1, v1, lightColor); // bottom left
    }

    private void renderVertex(VertexConsumer consumer, Quaternionf quaternionf, float x, float y, float z, float width, float height, float sizeMultiplier, float u, float v, int lightColor) {
        Vector3f vertexPos = new Vector3f(width, height, 0.0f).rotate(quaternionf).mul(sizeMultiplier).add(x, y, z);
        consumer.addVertex(vertexPos.x(), vertexPos.y(), vertexPos.z()).setUv(u, v).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(lightColor);
    }
}
