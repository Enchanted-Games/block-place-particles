package games.enchanted.eg_particle_interactions.common.particle.types.vanilla;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.types.PIParticleProvider;
import games.enchanted.eg_particle_interactions.common.particle.types.options.SimpleParticleOptions;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import org.jspecify.annotations.Nullable;

public class BlockParticleOptionWrapper extends ParticleOptionWrapper {
    protected final ParticleType<BlockParticleOption> type;

    protected BlockParticleOptionWrapper(ParticleContext context, ParticleType<BlockParticleOption> type, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(context, x, y, z, xSpeed, ySpeed, zSpeed);
        this.type = type;
    }

    @Override
    protected @Nullable ParticleOptions makeParticle(ParticleContext context) {
        ParticleContext.BlockContext blockContext = context.blockContext();
        if (blockContext == null) return null;
        return new BlockParticleOption(ParticleTypes.FALLING_DUST, context.blockContext().state());
    }

    public static class FallingDustProvider implements PIParticleProvider<SimpleParticleOptions> {
        @Override
        public @Nullable Particle createParticle(
            SimpleParticleOptions options,
            ParticleComponentMap components,
            ParticleAppearance appearance,
            ParticleContext context,
            double x,
            double z,
            double y,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new BlockParticleOptionWrapper(
                context,
                ParticleTypes.FALLING_DUST,
                x,
                y,
                z,
                xSpeed,
                ySpeed,
                zSpeed
            );
        }
    }
}
