package games.enchanted.blockplaceparticles.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class FallingPetalProvider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet spriteSet;

    public FallingPetalProvider(SpriteSet spriteSet) {
        this.spriteSet = spriteSet;
    }

    @Nullable
    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return new FallingPetal(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
    }
}
