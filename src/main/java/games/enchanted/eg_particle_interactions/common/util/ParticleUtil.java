package games.enchanted.eg_particle_interactions.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;

public class ParticleUtil {
    public static BlockPos getPosFromBlockParticleOption(BlockParticleOption option) {
        //? if fabric {
        return option.getBlockPos();
        //?} else {
        /*return option.getPos();
        *///?}
    }
}
