package games.enchanted.eg_particle_interactions.common.particle.types.dust;

import games.enchanted.eg_particle_interactions.common.particle.PIParticleType;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.ParticleTypesRegistry;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.provider.PIParticleProvider;
import net.minecraft.client.particle.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FloatingColouredDust extends AbstractDust {
    protected FloatingColouredDust(ParticleContext context, ParticleAppearance appearance, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float gravityMultiplier, boolean spawnSpecks) {
        super(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, gravityMultiplier, spawnSpecks);

        int[] colour = appearance.colourSource().getARGB(context);
        this.setRGBA(
            (float) colour[1] / 255f,
            (float) colour[2] / 255f,
            (float) colour[3] / 255f,
            (float) colour[0] / 255f
        );
    }

    @Override
    public @NotNull PIParticleOptions getSpeckParticle() {
        return ParticleTypesRegistry.TINTED_DUST_SPECK;
    }

    public static class TintedDustProvider implements PIParticleProvider<PIParticleType.Simple> {
        public TintedDustProvider() {
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
            return new FloatingColouredDust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 0.7f, true);
        }
    }

    public static class TintedDustSpeckProvider implements PIParticleProvider<PIParticleType.Simple> {
        public TintedDustSpeckProvider() {
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
            return new FloatingColouredDust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, 0.35f, false);
        }
    }

    public static class RedstoneProvider implements PIParticleProvider<PIParticleType.Simple> {
        public RedstoneProvider() {
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
            // TODO: replace this with better particle palette system
//            BlockState state = type.getState();
//            int powerLevel = 15;
//            if(state.hasProperty(RedstoneTorchBlock.LIT)) {
//                powerLevel = state.getValue(RedstoneTorchBlock.LIT) ? 15 : 0;
//            }
//            else if (state.hasProperty(ComparatorBlock.MODE)) {
//                powerLevel = state.getValue(ComparatorBlock.MODE) == ComparatorMode.SUBTRACT ? 15 : 0;
//            }
//            else if (state.hasProperty(RedStoneWireBlock.POWER)) {
//                powerLevel = Math.clamp(state.getValue(RedStoneWireBlock.POWER), 0, 15);
//            }
//            else if (state.hasProperty(RepeaterBlock.POWERED)) {
//                powerLevel = state.getValue(RepeaterBlock.POWERED) ? 15 : 0;
//            }
//            state = Blocks.REDSTONE_WIRE.defaultBlockState().setValue(RedStoneWireBlock.POWER, powerLevel);

            FloatingColouredDust particle = new FloatingColouredDust(context, appearance, x, y, z, xSpeed, ySpeed, zSpeed, -0.0f, false);
            particle.roll = 0;
            particle.prevRoll = 0;
            particle.lifetime = (int) (particle.lifetime * 0.4f);
            particle.friction = 0.9f;
            return particle;
        }
    }
}
