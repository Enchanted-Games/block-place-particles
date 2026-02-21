package games.enchanted.eg_particle_interactions.common.particle.types.drip;

import games.enchanted.eg_particle_interactions.common.duck.ParticleAccess;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.options.DripParticleOption;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import net.minecraft.client.particle.Particle;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class DripAndLandParticle extends ParticleInteractionsParticle {
    protected boolean hasLanded = false;
    protected final int startFallingAtTicks;
    protected final boolean translucent;

    protected float u0;
    protected float u1;
    protected float v0;
    protected float v1;

    DripAndLandParticle(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, DripParticleOption dripParticleOption, boolean translucent) {
        super(context, appearance, x, y, z);
        this.translucent = translucent;

        this.setSize(0.01F, 0.01F);
        this.setScale(0.15f);
        this.gravity = dripParticleOption.getGravity() + (level.getRandom().nextFloat() * dripParticleOption.getGravityRandomness());

        this.startFallingAtTicks = dripParticleOption.getStartFallingTicks();

        this.u0 = this.currentSprite.getU0();
        this.u1 = this.currentSprite.getU1();
        this.v0 = this.currentSprite.getV0();
        float v1 = this.currentSprite.getV1();
        float halfHeight = Math.abs(this.v0 - v1) / 2;
        this.v1 = v1 - halfHeight;

        this.lifetime = level.getRandom().nextIntBetweenInclusive(350, 500);
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

        if (this.startFallingAtTicks - this.age > 30) return;

        if (this.age < this.startFallingAtTicks) {
            this.yd -= 0.004 / this.startFallingAtTicks;
            this.move(this.xd, this.yd, this.zd);
            return;
        }

        this.yd -= this.gravity;
        this.move(this.xd, this.yd, this.zd);

        if (this.onGround) {
            land();
        }

        this.xd *= 0.98F;
        this.yd *= 0.98F;
        this.zd *= 0.98F;
    }

    protected void land() {
        if (this.hasLanded) return;
        this.hasLanded = true;

        float v0 = this.currentSprite.getV0();
        this.v1 = this.currentSprite.getV1();
        float halfHeight = Math.abs(v0 - this.v1) / 2;
        this.v0 = v0 + halfHeight;

        this.lifetime = this.age + level.getRandom().nextInt(30, 60);

        ((ParticleAccess) this).eg_particle_interactions$moveUpBecauseParticleLanded();
        this.billboardYOffset = 1.0f;
    }

    @Override
    protected float getU0() {
        return this.u0;
    }

    @Override
    protected float getU1() {
        return this.u1;
    }

    @Override
    protected float getV0() {
        return this.v0;
    }

    @Override
    protected float getV1() {
        return this.v1;
    }

    @Override
    protected @NonNull ParticleLayer getParticleLayer() {
        return this.translucent ? ParticleLayer.TRANSLUCENT : ParticleLayer.CUTOUT;
    }

    public static class UntintedDropProvider implements PIParticleProvider<DripParticleOption> {
        public UntintedDropProvider() {
        }

        @Override
        public @Nullable Particle createParticle(DripParticleOption options, ParticleContext context, ParticleAppearance particleAppearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new DripAndLandParticle(context, particleAppearance, x, y, z, options, true);
        }
    }
}
