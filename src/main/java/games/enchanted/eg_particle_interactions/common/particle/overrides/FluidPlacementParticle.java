package games.enchanted.eg_particle_interactions.common.particle.overrides;

import games.enchanted.eg_particle_interactions.common.config.categories.FluidPlacementOptions;
import games.enchanted.eg_particle_interactions.common.particle.ParticleTypesRegistry;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum FluidPlacementParticle {
    NONE("none"),
    TINTED_WATER("tinted_water", ParticleTypesRegistry.WATER_BUCKET_TINTED_SPLASH, true),
    LAVA("lava", ParticleTypesRegistry.LAVA_BUCKET_SPLASH, false),
    GENERIC("generic", ParticleTypesRegistry.GENERIC_FLUID_BUCKET_SPLASH, true);

    private final String name;
    @Nullable private ParticleOptions particleType;
    @Nullable private ParticleType<BlockParticleOption> blockParticleType;
    private final boolean isBlockStateParticle;

    FluidPlacementParticle(String overrideName, boolean isBlockStateParticle) {
        this.name = overrideName;
        this.isBlockStateParticle = isBlockStateParticle;
    }
    FluidPlacementParticle(String overrideName) {
        this(overrideName, false);
        this.particleType = null;
        this.blockParticleType = null;
    }
    FluidPlacementParticle(String overrideName, @Nullable ParticleOptions particle, boolean isBlockStateParticle) {
        this(overrideName, isBlockStateParticle);
        this.particleType = particle;
        this.blockParticleType = null;
    }
    FluidPlacementParticle(String overrideName, @Nullable ParticleType<BlockParticleOption> particle, boolean isBlockStateParticle) {
        this(overrideName, isBlockStateParticle);
        this.particleType = null;
        this.blockParticleType = particle;
    }

    /**
     * @return the {@link ParticleOptions} or null if this particle type is a blockstate particle.
     * @see #getBlockParticleOption
     * @see #isBlockStateParticle
     */
    public @Nullable ParticleOptions getParticleOption() {
        return this.particleType;
    }

    /**
     * @param blockState the {@link BlockState} to use when creating the particle options
     * @return a new {@link BlockParticleOption} or null if this particle type is not a blockstate particle.
     * @see #getParticleOption
     * @see #isBlockStateParticle
     */
    public @Nullable BlockParticleOption getBlockParticleOption(BlockState blockState) {
        if(this.blockParticleType == null) return null;
        return new BlockParticleOption(this.blockParticleType, blockState);
    }

    public boolean isBlockStateParticle() {
        return this.isBlockStateParticle;
    }

    @Override
    public @NotNull String toString() {
        return this.name;
    }

    public static FluidPlacementParticle getParticleForFluid(Fluid fluid) {
        return getParticleForFluid(fluid, true);
    }

    public static FluidPlacementParticle getParticleForFluid(Fluid fluid, boolean isFluidBeingPlaced) {
        Identifier fluidLoc = RegistryHelpers.getLocationFromFluid(fluid);
        if (FluidPlacementOptions.WATER_SPLASH_FLUIDS.getValue().contains(fluidLoc) && shouldHaveParticle(isFluidBeingPlaced, FluidPlacementOptions.WATER_SPLASH_ENABLED.getValue())) {
            return TINTED_WATER;
        } else if (FluidPlacementOptions.LAVA_SPLASH_FLUIDS.getValue().contains(fluidLoc) && shouldHaveParticle(isFluidBeingPlaced, FluidPlacementOptions.LAVA_SPLASH_ENABLED.getValue())) {
            return LAVA;
        } else if (FluidPlacementOptions.GENERIC_SPLASH_FLUIDS.getValue().contains(fluidLoc) && shouldHaveParticle(isFluidBeingPlaced, FluidPlacementOptions.GENERIC_SPLASH_ENABLED.getValue())) {
            return GENERIC;
        }
        return NONE;
    }

    private static boolean shouldHaveParticle(boolean isFluidBeingPlaced, boolean spawnOnFluidPlace) {
        if(isFluidBeingPlaced) {
            return spawnOnFluidPlace;
        }
        return false;
    }

    public static int getParticleMultiplier(FluidPlacementParticle fluidPlacementParticle, boolean isBlockBeingPlaced) {
        switch (fluidPlacementParticle) {
            case TINTED_WATER -> {
                return getAppropriateMultiplier(isBlockBeingPlaced, FluidPlacementOptions.WATER_SPLASH_AMOUNT_ON_PLACE.getValue(), 0);
            }
            case LAVA -> {
                return getAppropriateMultiplier(isBlockBeingPlaced, FluidPlacementOptions.LAVA_SPLASH_AMOUNT_ON_PLACE.getValue(), 0);
            }
            case GENERIC -> {
                return getAppropriateMultiplier(isBlockBeingPlaced, FluidPlacementOptions.GENERIC_SPLASH_AMOUNT_ON_PLACE.getValue(), 0);
            }
            default -> {
                return 2;
            }
        }
    }

    private static int getAppropriateMultiplier(boolean isBlockBeingPlaced, int blockPlaceMultiplier, int blockBreakMultiplier) {
        if(isBlockBeingPlaced) {
            return blockPlaceMultiplier;
        }
        return blockBreakMultiplier;
    }
}
