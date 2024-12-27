package games.enchanted.blockplaceparticles.particle.shatter;

import com.mojang.blaze3d.vertex.VertexConsumer;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class PortalShatter extends AbstractShatter {
    protected PortalShatter(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState blockState) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, blockState);
    }

    @Override
    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float partialTicks) {
        Quaternionf quaternionf = new Quaternionf();
        this.getFacingMode(this.blockState.hasProperty(NetherPortalBlock.AXIS) ? this.blockState.getValue(NetherPortalBlock.AXIS).getNegative() : null).setRotation(quaternionf, camera, partialTicks);
        if (this.roll != 0.0F) {
            quaternionf.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
        }
        this.renderQuads(vertexConsumer, camera, quaternionf, partialTicks);

        Quaternionf quaternionf2 = new Quaternionf();
        this.getFacingMode(this.blockState.hasProperty(NetherPortalBlock.AXIS) ? this.blockState.getValue(NetherPortalBlock.AXIS).getPositive() : null).setRotation(quaternionf2, camera, partialTicks);
        if (this.roll != 0.0F) {
            quaternionf2.rotateZ(Mth.lerp(partialTicks, ((float) Math.toRadians(180f) - this.oRoll), ((float) Math.toRadians(180f) - this.roll)));
        }
        this.renderQuads(vertexConsumer, camera, quaternionf2, partialTicks);
    }


    public static class NetherPortalShatterProvider implements ParticleProvider<BlockParticleOption> {
        public NetherPortalShatterProvider(SpriteSet spriteSet) {}

        @Nullable
        @Override
        public Particle createParticle(@NotNull BlockParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PortalShatter(level, x, y, z, xSpeed, ySpeed, zSpeed, type.getState());
        }
    }
}
