package games.enchanted.eg_particle_interactions.common.particle.types.swirling;

import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class WaterVapour extends SwirlingParticle {
    protected final SpriteSet sprites;

    protected WaterVapour(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, boolean shouldSwirl) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, shouldSwirl);
        this.sprites = spriteSet;
        this.setSpriteFromAge(sprites);
        this.setInitialVelocity(xSpeed, ySpeed, zSpeed, 0.015f);

        this.gravity = MathHelpers.randomBetween(-0.04f, -0.07f);

        this.lifetime = MathHelpers.randomBetween(4, 15);

        this.rotSpeed = 0f;
        this.spinAcceleration = 0f;
        this.swirlStrength = MathHelpers.randomBetween(3f, 6f) * (level.random.nextBoolean() ? -1 : 1);
        this.swirlPeriod = MathHelpers.randomBetween(1, 4);

        this.setScale(2f/32f);
        this.setSize(1f/32f, 1f/32f);
    }

    @Override
    public void tick() {
        setSpriteFromAge(this.sprites);
        super.tick();
    }

    public static class WaterVapourProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public WaterVapourProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(
            SimpleParticleType options,
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
            return new WaterVapour(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, true);
        }
    }
}
