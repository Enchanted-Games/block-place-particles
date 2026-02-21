package games.enchanted.eg_particle_interactions.common.particle.types.shatter;

import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class BlockShatter extends AbstractShatter {
    protected final @Nullable Direction facingDirection;

    protected BlockShatter(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed);

        if (context.blockContext() != null) {
            BlockState state = context.blockContext().state();
            this.facingDirection = state.hasProperty(NetherPortalBlock.AXIS) ? state.getValue(NetherPortalBlock.AXIS).getPositive() : null;
        } else {
            this.facingDirection = null;
        }
    }

    @Override
    protected @Nullable Direction getParticleFacingDirection() {
        return this.facingDirection;
    }

    public static class BlockShatterProvider implements PIParticleProvider<PIParticleType.Simple> {
        public BlockShatterProvider() {
        }

        @Override
        public @Nullable Particle createParticle(
            PIParticleType.Simple type,
            ParticleContext context,
            ParticleAppearance appearance,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new BlockShatter(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
