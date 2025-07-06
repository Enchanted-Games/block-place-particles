package games.enchanted.eg_particle_interactions.common.particle.emitter.arc;

import games.enchanted.eg_particle_interactions.common.particle.ModParticleTypes;
import games.enchanted.eg_particle_interactions.common.particle.option.ArcEmitterOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArcEmitter extends AbstractArcEmitter {
    protected ArcEmitter(ClientLevel level, double x, double y, double z, ArcEmitterOptions options) {
        super(level, x, y, z, 0, 0, 0, options);
    }

    @Override
    protected @Nullable ParticleOptions getParticleToEmit(ClientLevel level, double x, double y, double z) {
        return level.random.nextFloat() > 0.5 ? ModParticleTypes.SPARK_FLASH : null;
    }

    public static class Provider implements ParticleProvider<ArcEmitterOptions> {
        public Provider(SpriteSet spriteSet) {}

        @Nullable
        @Override
        public Particle createParticle(@NotNull ArcEmitterOptions options, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ArcEmitter(level, x, y, z, options);
        }
    }
}
