package games.enchanted.eg_particle_interactions.common.particle.types.emitter.arc;

import games.enchanted.eg_particle_interactions.common.registry.particle.ParticleTypes;
import games.enchanted.eg_particle_interactions.common.particle.options.ArcEmitterOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class ArcEmitter extends AbstractArcEmitter {
    protected ArcEmitter(ClientLevel level, double x, double y, double z, ArcEmitterOptions options) {
        super(level, x, y, z, 0, 0, 0, options);
    }

    @Override
    protected @Nullable ParticleOptions getParticleToEmit(ClientLevel level, double x, double y, double z) {
        return level.getRandom().nextFloat() > ((float) this.age / this.lifetime) ? ParticleTypes.LIGHTNING_FLASH : null;
    }

    public static class Provider implements ParticleProvider<ArcEmitterOptions> {
        public Provider(SpriteSet spriteSet) {}

        @Override
        public @Nullable Particle createParticle(
            ArcEmitterOptions options,
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
            return new ArcEmitter(level, x, y, z, options);
        }
    }
}
