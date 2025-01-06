package games.enchanted.blockplaceparticles.particle.constant_motion;

import com.mojang.blaze3d.vertex.VertexConsumer;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import games.enchanted.blockplaceparticles.util.RenderingUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class LavaPop extends ConstantMotionAnimatedParticle {
    protected LavaPop(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, int lifetime, float quadSize, boolean transparency) {
        super(level, x, y, z, spriteSet, lifetime, quadSize, transparency);
    }

    @Override
    protected void renderRotatedQuad(@NotNull VertexConsumer buffer, @NotNull Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        float scale = this.getQuadSize(partialTicks);
        float uo = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int packedLight = this.getLightColor(partialTicks);
        this.renderVertex(buffer, quaternion, x, y, z, 1.0F, -1.0F, scale, u1, v1, packedLight);
        this.renderVertex(buffer, quaternion, x, y, z, 1.0F, 1.0F, scale, u1, v0, packedLight);
        this.renderVertex(buffer, quaternion, x, y, z, -1.0F, 1.0F, scale, uo, v0, packedLight);
        this.renderVertex(buffer, quaternion, x, y, z, -1.0F, -1.0F, scale, uo, v1, packedLight);
    }

    private void renderVertex(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float scale, float u, float v, int packedLight) {
        yOffset += 1f;
        RenderingUtil.addVertexToConsumer(buffer, quaternion, x, y, z, xOffset, yOffset, scale, u, v, packedLight);
    }

    public static class LavaPopProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public LavaPopProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new LavaPop(level, x, y, z, spriteSet, MathHelpers.randomBetween(16, 20), 2/8f, false);
        }
    }
}
