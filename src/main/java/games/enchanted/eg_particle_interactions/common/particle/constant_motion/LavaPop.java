package games.enchanted.eg_particle_interactions.common.particle.constant_motion;

import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import games.enchanted.eg_particle_interactions.common.util.render.RenderingUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

//? if minecraft: > 1.21.8 {
import games.enchanted.eg_particle_interactions.common.rendering.particle.state.CustomParticleGeometryRenderState;
import games.enchanted.eg_particle_interactions.common.util.render.StateAndLayer;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.util.RandomSource;
//?} else {
/*import com.mojang.blaze3d.vertex.VertexConsumer;
*///?}

public class LavaPop extends ConstantMotionAnimatedParticle {
    protected LavaPop(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, int lifetime, float quadSize, boolean transparency) {
        super(level, x, y, z, spriteSet, lifetime, quadSize, transparency);
    }

    @Override
    protected void extractGeometry(
        //? if minecraft: <= 1.21.8 {
        /*VertexConsumer consumer,
        *///?} else {
        CustomParticleGeometryRenderState state,
         //?}
        Quaternionf quaternion, float x, float y, float z, float partialTicks
    ) {
        //? if minecraft: > 1.21.8 {
        SingleQuadParticle.Layer layer = this.getLayer();
        StateAndLayer consumer = new StateAndLayer(state, layer);
        //?}
        float scale = this.getScale();
        int packedLight = this.getLightColor(partialTicks);
        float uo = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();

        //? if minecraft: > 1.21.8 {
        state.startQuad(layer);
        //?}
        RenderingUtil.addVertex(consumer, quaternion, x, y, z,  1.0F, 0.0F, scale, u1, v1, packedLight);
        RenderingUtil.addVertex(consumer, quaternion, x, y, z,  1.0F, 2.0F, scale, u1, v0, packedLight);
        RenderingUtil.addVertex(consumer, quaternion, x, y, z, -1.0F, 2.0F, scale, uo, v0, packedLight);
        RenderingUtil.addVertex(consumer, quaternion, x, y, z, -1.0F, 0.0F, scale, uo, v1, packedLight);
        //? if minecraft: > 1.21.8 {
        state.finishQuad(layer);
        //?}
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
