package games.enchanted.blockplaceparticles.particle.emitter;

import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import games.enchanted.blockplaceparticles.particle.option.ParticleEmitterOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UnderwaterBubbleEmitter extends AbstractParticleEmitter {
    protected UnderwaterBubbleEmitter(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ParticleEmitterOptions emitterOptions) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, emitterOptions);
    }

    @Override
    protected ParticleOptions getParticleToEmit(ClientLevel level, double x, double y, double z) {
        return ModParticleTypes.UNDERWATER_RISING_BUBBLE_SMALL;
    }

    public static class Provider implements ParticleProvider<ParticleEmitterOptions> {
        public Provider(SpriteSet spriteSet) {}

        @Nullable
        @Override
        public Particle createParticle(@NotNull ParticleEmitterOptions emitterOptions, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new UnderwaterBubbleEmitter(level, x, y, z, xSpeed, ySpeed, zSpeed, emitterOptions);
        }
    }
}
