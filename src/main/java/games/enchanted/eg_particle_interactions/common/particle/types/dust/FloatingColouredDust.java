package games.enchanted.eg_particle_interactions.common.particle.types.dust;

import games.enchanted.eg_particle_interactions.common.particle.ParticleTypesRegistry;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.resource.texture_source.colour.BlockTextureColourSource;
import games.enchanted.eg_particle_interactions.common.util.ParticleUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FloatingColouredDust extends AbstractDust {
    protected final BlockState dustBlockState;

    protected FloatingColouredDust(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockPos blockPos, BlockState blockState, SpriteSet spriteSet, float gravityMultiplier, boolean spawnSpecks, boolean spriteFromAge) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet, gravityMultiplier, spawnSpecks, spriteFromAge);

        this.dustBlockState = blockState;

        var colourSource = new BlockTextureColourSource(0);
        int[] colour = colourSource.getARGB(new ParticleContext(
            level,
            new ParticleContext.BlockContext(blockState, blockPos),
            null
        ));
        this.setRGBA(
            (float)colour[1] / 255f,
            (float)colour[2] / 255f,
            (float)colour[3] / 255f,
            (float)colour[0] / 255f
        );
    }

    @Override
    public @NotNull ParticleOptions getSpeckParticle() {
        return new BlockParticleOption(ParticleTypesRegistry.TINTED_DUST_SPECK, this.dustBlockState);
    }

    public static class TintedDustProvider implements ParticleProvider<BlockParticleOption> {
        private final SpriteSet spriteSet;

        public TintedDustProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(
            BlockParticleOption type,
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
            return new FloatingColouredDust(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), this.spriteSet, 0.7f, true, true);
        }
    }

    public static class TintedDustSpeckProvider implements ParticleProvider<BlockParticleOption>  {
        private final SpriteSet spriteSet;

        public TintedDustSpeckProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(
            BlockParticleOption type,
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
            return new FloatingColouredDust(level, x, y, z, xSpeed, ySpeed, zSpeed, BlockPos.containing(x, y, z), type.getState(), this.spriteSet, 0.35f, false, false);
        }
    }

    public static class RedstoneProvider implements ParticleProvider<BlockParticleOption> {
        private final SpriteSet spriteSet;

        public RedstoneProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(
            BlockParticleOption type,
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
            // TODO: replace this with better particle palette system
            BlockState state = type.getState();
            int powerLevel = 15;
            if(state.hasProperty(RedstoneTorchBlock.LIT)) {
                powerLevel = state.getValue(RedstoneTorchBlock.LIT) ? 15 : 0;
            }
            else if (state.hasProperty(ComparatorBlock.MODE)) {
                powerLevel = state.getValue(ComparatorBlock.MODE) == ComparatorMode.SUBTRACT ? 15 : 0;
            }
            else if (state.hasProperty(RedStoneWireBlock.POWER)) {
                powerLevel = Math.clamp(state.getValue(RedStoneWireBlock.POWER), 0, 15);
            }
            else if (state.hasProperty(RepeaterBlock.POWERED)) {
                powerLevel = state.getValue(RepeaterBlock.POWERED) ? 15 : 0;
            }
            state = Blocks.REDSTONE_WIRE.defaultBlockState().setValue(RedStoneWireBlock.POWER, powerLevel);

            FloatingColouredDust particle = new FloatingColouredDust(level, x, y, z, xSpeed, ySpeed, zSpeed, ParticleUtil.getPosFromBlockParticleOption(type), state, this.spriteSet, -0.0f, false, true);
            particle.roll = 0;
            particle.prevRoll = 0;
            particle.lifetime = (int)(particle.lifetime * 0.4f);
            particle.friction = 0.9f;
            return particle;
        }
    }
}
