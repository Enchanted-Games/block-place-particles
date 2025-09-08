package games.enchanted.eg_particle_interactions.common.particle.drip;

import com.mojang.blaze3d.vertex.VertexConsumer;
import games.enchanted.eg_particle_interactions.common.duck.ParticleAccess;
import games.enchanted.eg_particle_interactions.common.mixin.accessor.client.ParticleAccessor;
import games.enchanted.eg_particle_interactions.common.particle.option.DripParticleOption;
import games.enchanted.eg_particle_interactions.common.util.RenderingUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class GenericDripAndLandParticle extends TextureSheetParticle {
    protected boolean hasLanded = false;
    protected final int startFallingAtTicks;

    protected float uo;
    protected float u1;
    protected float v0;
    protected float v1;

    GenericDripAndLandParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, DripParticleOption dripParticleOption) {
        super(level, x, y, z);
        this.pickSprite(sprites);
        this.setSize(0.01F, 0.01F);
        this.quadSize = 0.15f;
        this.gravity = dripParticleOption.getGravity();

        this.startFallingAtTicks = dripParticleOption.getStartFallingTicks();

        this.uo = this.getU0();
        this.u1 = this.getU1();
        this.v0 = this.getV0();
        float v1 = this.getV1();
        float halfHeight = Math.abs(this.v0 - v1) / 2;
        this.v1 = v1 - halfHeight;

        this.lifetime = level.random.nextIntBetweenInclusive(350, 500);
    }

    @Override
    public void tick() {
        if (this.removed) return;

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
        }

        if(this.startFallingAtTicks - this.age > 30) return;

        if(this.age < this.startFallingAtTicks) {
            this.yd -= 0.004 / this.startFallingAtTicks;
            this.move(this.xd, this.yd, this.zd);
            return;
        }

        this.yd -= this.gravity;
        this.move(this.xd, this.yd, this.zd);

        if(this.onGround) {
            land();
        }

        this.xd *= 0.98F;
        this.yd *= 0.98F;
        this.zd *= 0.98F;
    }

    protected void land() {
        if(this.hasLanded) return;
        this.hasLanded = true;

        float v0 = this.getV0();
        this.v1 = this.getV1();
        float halfHeight = Math.abs(v0 - this.v1) / 2;
        this.v0 = v0 + halfHeight;

        this.lifetime = this.age + level.random.nextInt(30, 60);

        ((ParticleAccess) this).eg_particle_interactions$moveUpBecauseParticleLanded();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    protected void renderRotatedQuad(@NotNull VertexConsumer buffer, @NotNull Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        float scale = this.getQuadSize(partialTicks);
        int packedLight = this.getLightColor(partialTicks);
        this.renderVertex(buffer, quaternion, x, y, z, 1.0F, -1.0F, scale, this.u1, this.v1, packedLight);
        this.renderVertex(buffer, quaternion, x, y, z, 1.0F, 1.0F, scale, this.u1, this.v0, packedLight);
        this.renderVertex(buffer, quaternion, x, y, z, -1.0F, 1.0F, scale, this.uo, this.v0, packedLight);
        this.renderVertex(buffer, quaternion, x, y, z, -1.0F, -1.0F, scale, this.uo, this.v1, packedLight);
    }

    private void renderVertex(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float scale, float u, float v, int packedLight) {
        yOffset += hasLanded ? 1f : 0f;
        RenderingUtil.addVertexToConsumer(buffer, quaternion, x, y, z, xOffset, yOffset, scale, u, v, packedLight, this.rCol, this.gCol, this.bCol, this.alpha);
    }

    public static class UntintedDropProvider implements ParticleProvider<DripParticleOption> {
        SpriteSet sprites;

        public UntintedDropProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(DripParticleOption options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new GenericDripAndLandParticle(level, x, y, z, sprites, options);
        }
    }
}
