package games.enchanted.eg_particle_interactions.common.particle.types.constant_motion;

import games.enchanted.eg_particle_interactions.common.particle.render.geometry.QuadConsumer;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

//? if minecraft: > 1.21.8 {
import net.minecraft.util.RandomSource;
//?}

public class LavaPop extends ConstantMotionAnimatedParticle {
    protected LavaPop(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, int lifetime, float quadSize, boolean transparency) {
        super(level, x, y, z, spriteSet, lifetime, quadSize, transparency);
    }

    @Override
    protected void extractGeometry(QuadConsumer consumer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        float scale = this.getScale();
        int packedLight = this.getLightColor(partialTicks);
        float uo = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();

        consumer.startQuad();
        consumer.addVertex(quaternion, x, y, z,  1.0F, 0.0F, scale, u1, v1, packedLight);
        consumer.addVertex(quaternion, x, y, z,  1.0F, 2.0F, scale, u1, v0, packedLight);
        consumer.addVertex(quaternion, x, y, z, -1.0F, 2.0F, scale, uo, v0, packedLight);
        consumer.addVertex(quaternion, x, y, z, -1.0F, 0.0F, scale, uo, v1, packedLight);
        consumer.finishQuad();
    }

    public static class LavaPopProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public LavaPopProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(
            SimpleParticleType type,
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
            //? if minecraft: > 1.21.8 {
            , RandomSource random
            //?}
        ) {
            return new LavaPop(level, x, y, z, spriteSet, MathHelpers.randomBetween(26, 32), 2/8f, false);
        }
    }
}
