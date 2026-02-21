package games.enchanted.eg_particle_interactions.common.particle.types.bubble;

import games.enchanted.eg_particle_interactions.common.duck.ParticleAccess;
import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import org.jetbrains.annotations.Nullable;

public class UnderwaterRisingBubble extends ParticleInteractionsParticle {
    protected UnderwaterRisingBubble(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed);
        this.gravity = -0.35F;
        this.friction = 0.85F;
        this.setSize(0.02F, 0.02F);
        this.setScale(this.getScale() * (this.random.nextFloat() * 0.6F + 0.2F));
        this.xd = xSpeed * 0.2F + (Math.random() * 2.0 - 1.0) * 0.02F;
        this.yd = ySpeed * 0.2F + (Math.random() * 2.0 - 1.0) * 0.02F;
        this.zd = zSpeed * 0.2F + (Math.random() * 2.0 - 1.0) * 0.02F;
        this.lifetime = MathHelpers.randomBetween(100, 600);

        ((ParticleAccess) this).eg_particle_interactions$setBypassMovementCollisionCheck(true);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age > 1 && !this.removed && !this.level.getFluidState(BlockPos.containing(this.x, this.y + 0.125f, this.z)).is(FluidTags.WATER)) {
            this.popAndRemove();
        } else if (this.age >= this.lifetime - 1 || ((ParticleAccess) this).eg_particle_interactions$isStoppedByCollision()) {
            this.popAndRemove();
        }
        this.xd *= 0.9;
        this.zd *= 0.9;

        // if moving upwards
        if (this.yd > 0 && ((ParticleAccess) this).eg_particle_interactions$getBypassMovementCollisionCheck()) {
            ((ParticleAccess) this).eg_particle_interactions$setBypassMovementCollisionCheck(false);
        }
    }

    private void popAndRemove() {
        this.level.addParticle(ParticleTypes.BUBBLE_POP, this.x, this.y, this.z, 0, 0, 0);
        this.remove();
    }

    public static class Provider implements PIParticleProvider<PIParticleType.Simple> {
        public Provider() {
        }

        @Override
        public @Nullable Particle createParticle(
            PIParticleType.Simple options,
            ParticleContext context, ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new UnderwaterRisingBubble(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }

    public static class SmallProvider implements PIParticleProvider<PIParticleType.Simple> {
        public SmallProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            PIParticleType.Simple options,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            UnderwaterRisingBubble particle = new UnderwaterRisingBubble(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.setScale(MathHelpers.randomBetween(0.02f, 0.05f));
            return particle;
        }
    }
}
