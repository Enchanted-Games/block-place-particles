package games.enchanted.eg_particle_interactions.common.particle.types.constant_motion;

import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

//? if minecraft: > 1.21.8 {
import net.minecraft.util.RandomSource;
//?}

public class LavaPop extends ConstantMotionAnimatedParticle {
    protected LavaPop(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, int lifetime, float quadSize, boolean transparency) {
        super(level, x, y, z, spriteSet, lifetime, quadSize, transparency);
        this.billboardYOffset = 1.0f;
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
