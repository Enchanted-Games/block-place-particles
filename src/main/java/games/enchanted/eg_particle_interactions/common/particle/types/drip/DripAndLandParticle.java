package games.enchanted.eg_particle_interactions.common.particle.types.drip;

import games.enchanted.eg_particle_interactions.common.duck.ParticleAccess;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.particle.options.DripParticleOption;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.QuadConsumer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

//? if minecraft: > 1.21.8 {
import net.minecraft.util.RandomSource;
//?}

public class DripAndLandParticle extends ParticleInteractionsParticle {
    protected boolean hasLanded = false;
    protected final int startFallingAtTicks;
    protected final boolean translucent;

    protected float uo;
    protected float u1;
    protected float v0;
    protected float v1;

    DripAndLandParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, DripParticleOption dripParticleOption, boolean translucent) {
        super(level, x, y, z, sprites.get(level.random));
        this.translucent = translucent;

        this.setSize(0.01F, 0.01F);
        this.setScale(0.15f);
        this.gravity = dripParticleOption.getGravity() + (level.random.nextFloat() * dripParticleOption.getGravityRandomness());

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
    protected ParticleLayer getParticleLayer() {
        return this.translucent ? ParticleLayer.TRANSLUCENT : ParticleLayer.OPAQUE;
    }

    @Override
    protected void extractGeometry(QuadConsumer consumer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        float scale = this.getScale();
        int packedLight = this.getLightColor(partialTicks);
        float yOffset = hasLanded ? 1f : 0f;
        
        consumer.startQuad();
        consumer.addVertex(quaternion, x, y, z,  1.0F, -1.0F + yOffset, scale, this.u1, this.v1, packedLight);
        consumer.addVertex(quaternion, x, y, z,  1.0F,  1.0F + yOffset, scale, this.u1, this.v0, packedLight);
        consumer.addVertex(quaternion, x, y, z, -1.0F,  1.0F + yOffset, scale, this.uo, this.v0, packedLight);
        consumer.addVertex(quaternion, x, y, z, -1.0F, -1.0F + yOffset, scale, this.uo, this.v1, packedLight);
        consumer.finishQuad();
    }

    public static class UntintedDropProvider implements ParticleProvider<DripParticleOption> {
        SpriteSet sprites;

        public UntintedDropProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(
            DripParticleOption options,
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
            return new DripAndLandParticle(level, x, y, z, sprites, options, true);
        }
    }
}
