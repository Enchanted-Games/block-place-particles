package games.enchanted.eg_particle_interactions.common.particle.vanilla;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.ParticleSpawner;
import games.enchanted.eg_particle_interactions.common.particle.definition.ParticleDefinitionManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

public class ParticleSpawnerVanillaParticle extends Particle {
    PIVanillaParticleOptions options;

    public ParticleSpawnerVanillaParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, PIVanillaParticleOptions options) {
        super(level, x, y, z, xa, ya, za);
        this.xd = xa;
        this.yd = ya;
        this.zd = za;
        this.lifetime = 1;
        this.options = options;
    }

    @Override
    public void tick() {
        if(this.removed) return;

        ParticleSpawner.spawn(
            ParticleDefinitionManager.INSTANCE.getOrFallback(this.options.definitionId),
            this.options.components,
            ParticleContext.plain(this.level, BlockPos.containing(this.x, this.y, this.z)),
            this.x,
            this.y,
            this.z,
            this.xd,
            this.yd,
            this.zd
        );

        this.remove();
    }

    @Override
    public ParticleRenderType getGroup() {
        return ParticleRenderType.NO_RENDER;
    }

    public static class Provider implements ParticleProvider<PIVanillaParticleOptions> {
        public Provider(SpriteSet spriteSet) {
        }

        @Override
        public @Nullable Particle createParticle(PIVanillaParticleOptions options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new ParticleSpawnerVanillaParticle(level, x, y, z, xAux, yAux, zAux, options);
        }
    }
}
