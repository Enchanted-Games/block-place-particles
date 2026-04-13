package games.enchanted.eg_particle_interactions.common.particle.appearance.colour;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.util.ColourUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ComparatorMode;

public class RedstonePowerLevelSource implements ColourSource {
    public static final MapCodec<RedstonePowerLevelSource> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Codec.intRange(0, 15).optionalFieldOf("fallback_power", 15).forGetter(RedstonePowerLevelSource::getFallbackPower)
        ).apply(
            instance,
            RedstonePowerLevelSource::new
        )
    );

    final int fallbackPower;

    RedstonePowerLevelSource(int fallbackPower) {
        this.fallbackPower = fallbackPower;
    }

    protected int getFallbackPower() {
        return this.fallbackPower;
    }

    @Override
    public int[] getARGB(ParticleContext context) {
        int effectivePower;
        BlockPos pos;
        if(context.blockContext() != null) {
            ParticleContext.BlockContext bContext = context.blockContext();
            pos = bContext.pos();
            effectivePower = getPowerLevelFromState(bContext.state(), this.fallbackPower);
        } else {
            pos = BlockPos.ZERO;
            effectivePower = this.fallbackPower;
        }

        BlockState redstoneState = Blocks.REDSTONE_WIRE.defaultBlockState().setValue(RedStoneWireBlock.POWER, effectivePower);
        var source = Minecraft.getInstance().getBlockColors().getTintSource(redstoneState, 0);
        if(source == null) return new int[]{255, 255, 255, 255};

        int tintColour = source.colorInWorld(redstoneState, context.level(), pos);
        return ColourUtil.RGBint_to_ARGB(tintColour);
    }

    protected int getPowerLevelFromState(BlockState state, int fallback) {
        if(state.hasProperty(RedstoneTorchBlock.LIT)) {
            return state.getValue(RedstoneTorchBlock.LIT) ? 15 : 0;
        }
        else if (state.hasProperty(ComparatorBlock.MODE)) {
            return state.getValue(ComparatorBlock.MODE) == ComparatorMode.SUBTRACT ? 15 : 0;
        }
        else if (state.hasProperty(RedStoneWireBlock.POWER)) {
            return Math.clamp(state.getValue(RedStoneWireBlock.POWER), 0, 15);
        }
        else if (state.hasProperty(RepeaterBlock.POWERED)) {
            return state.getValue(RepeaterBlock.POWERED) ? 15 : 0;
        }
        return fallback;
    }

    @Override
    public MapCodec<? extends ColourSource> codec() {
        return CODEC;
    }
}
