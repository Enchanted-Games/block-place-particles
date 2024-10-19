package games.enchanted.blockplaceparticles.particle_spawning;

import games.enchanted.blockplaceparticles.config.ConfigHandler;
import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import games.enchanted.blockplaceparticles.util.RegistryHelper;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public enum BlockParticleOverride {
    NONE("none"),
    BLOCK(
        "vanilla_particle",
        "generic_block_override",
        ParticleTypes.BLOCK,
        true,
        null,
        () -> ConfigHandler.block_enabled,
        () -> ConfigHandler.maxBlock_onPlace,
        () -> ConfigHandler.maxBlock_onBreak,
        null,
        (val) -> ConfigHandler.block_enabled = val,
        (val) -> ConfigHandler.maxBlock_onPlace = val,
        (val) -> ConfigHandler.maxBlock_onBreak = val,
        List.of(),
        ConfigHandler.block_enabled_DEFAULT,
        ConfigHandler.maxBlock_onPlace_DEFAULT,
        ConfigHandler.maxBlock_onBreak_DEFAULT
    ),
    SNOW_POWDER(
        "snowflake",
        "generic_block_override",
        ParticleTypes.SNOWFLAKE,
        false,
        () -> ConfigHandler.snowflake_Blocks,
        () -> ConfigHandler.snowflake_enabled,
        () -> ConfigHandler.maxSnowflakes_onPlace,
        () -> ConfigHandler.maxSnowflakes_onBreak,
        (val) -> ConfigHandler.snowflake_Blocks = val,
        (val) -> ConfigHandler.snowflake_enabled = val,
        (val) -> ConfigHandler.maxSnowflakes_onPlace = val,
        (val) -> ConfigHandler.maxSnowflakes_onBreak = val,
        ConfigHandler.snowflake_Blocks_DEFAULT,
        ConfigHandler.snowflake_enabled_DEFAULT,
        ConfigHandler.maxSnowflakes_onPlace_DEFAULT,
        ConfigHandler.maxSnowflakes_onBreak_DEFAULT
    ),
    CHERRY_LEAF(
        "cherry_petal",
        "generic_block_override",
        ModParticleTypes.FALLING_CHERRY_PETAL,
        false,
        () -> ConfigHandler.cherryPetal_Blocks,
        () -> ConfigHandler.cherryPetal_enabled,
        () -> ConfigHandler.maxCherryPetals_onPlace,
        () -> ConfigHandler.maxCherryPetals_onBreak,
        (val) -> ConfigHandler.cherryPetal_Blocks = val,
        (val) -> ConfigHandler.cherryPetal_enabled = val,
        (val) -> ConfigHandler.maxCherryPetals_onPlace = val,
        (val) -> ConfigHandler.maxCherryPetals_onBreak = val,
        ConfigHandler.cherryPetal_Blocks_DEFAULT,
        ConfigHandler.cherryPetal_enabled_DEFAULT,
        ConfigHandler.maxCherryPetals_onPlace_DEFAULT,
        ConfigHandler.maxCherryPetals_onBreak_DEFAULT
    ),
    AZALEA_LEAF(
        "azalea_leaf",
        "generic_block_override",
        ModParticleTypes.FALLING_AZALEA_LEAF,
        false,
        () -> ConfigHandler.azaleaLeaf_Blocks,
        () -> ConfigHandler.azaleaLeaf_enabled,
        () -> ConfigHandler.maxAzaleaLeaves_onPlace,
        () -> ConfigHandler.maxAzaleaLeaves_onBreak,
        (val) -> ConfigHandler.azaleaLeaf_Blocks = val,
        (val) -> ConfigHandler.azaleaLeaf_enabled = val,
        (val) -> ConfigHandler.maxAzaleaLeaves_onPlace = val,
        (val) -> ConfigHandler.maxAzaleaLeaves_onBreak = val,
        ConfigHandler.azaleaLeaf_Blocks_DEFAULT,
        ConfigHandler.azaleaLeaf_enabled_DEFAULT,
        ConfigHandler.maxAzaleaLeaves_onPlace_DEFAULT,
        ConfigHandler.maxAzaleaLeaves_onBreak_DEFAULT
    ),
    FLOWERING_AZALEA_LEAF(
        "flowering_azalea_leaf",
        "generic_block_override",
        ModParticleTypes.FALLING_FLOWERING_AZALEA_LEAF,
        false,
        () -> ConfigHandler.floweringAzaleaLeaf_Blocks,
        () -> ConfigHandler.floweringAzaleaLeaf_enabled,
        () -> ConfigHandler.maxFloweringAzaleaLeaves_onPlace,
        () -> ConfigHandler.maxFloweringAzaleaLeaves_onBreak,
        (val) -> ConfigHandler.floweringAzaleaLeaf_Blocks = val,
        (val) -> ConfigHandler.floweringAzaleaLeaf_enabled = val,
        (val) -> ConfigHandler.maxFloweringAzaleaLeaves_onPlace = val,
        (val) -> ConfigHandler.maxFloweringAzaleaLeaves_onBreak = val,
        ConfigHandler.floweringAzaleaLeaf_Blocks_DEFAULT,
        ConfigHandler.floweringAzaleaLeaf_enabled_DEFAULT,
        ConfigHandler.maxFloweringAzaleaLeaves_onPlace_DEFAULT,
        ConfigHandler.maxFloweringAzaleaLeaves_onBreak_DEFAULT
    ),
    TINTED_LEAF(
        "biome_leaf",
        "biome_leaf",
        ModParticleTypes.FALLING_TINTED_LEAF,
        true,
        () -> ConfigHandler.tintedLeaves_Blocks,
        () -> ConfigHandler.tintedLeaves_enabled,
        () -> ConfigHandler.maxTintedLeaves_onPlace,
        () -> ConfigHandler.maxTintedLeaves_onBreak,
        (val) -> ConfigHandler.tintedLeaves_Blocks = val,
        (val) -> ConfigHandler.tintedLeaves_enabled = val,
        (val) -> ConfigHandler.maxTintedLeaves_onPlace = val,
        (val) -> ConfigHandler.maxTintedLeaves_onBreak = val,
        ConfigHandler.tintedLeaves_Blocks_DEFAULT,
        ConfigHandler.tintedLeaves_enabled_DEFAULT,
        ConfigHandler.maxTintedLeaves_onPlace_DEFAULT,
        ConfigHandler.maxTintedLeaves_onBreak_DEFAULT
    );

    private final String name;
    private final String groupName;
    @Nullable private ParticleType<?> particleType;
    private final boolean isBlockStateParticle;
    @Nullable final Supplier<List<ResourceLocation>> supportedBlockResourceLocations_getter;
    final Supplier<Boolean> overrideEnabled_getter;
    final Supplier<Integer> maxParticlesOnPlace_getter;
    final Supplier<Integer> maxParticlesOnBreak_getter;
    @Nullable final Consumer<List<ResourceLocation>> supportedBlockResourceLocations_setter;
    final Consumer<Boolean> overrideEnabled_setter;
    final Consumer<Integer> maxParticlesOnPlace_setter;
    final Consumer<Integer> maxParticlesOnBreak_setter;
    @Nullable final List<ResourceLocation> supportedBlockResourceLocations_default;
    final Boolean overrideEnabled_default;
    final Integer maxParticlesOnPlace_default;
    final Integer maxParticlesOnBreak_default;

    private BlockParticleOverride(
        String overrideName,
        String groupName,
        boolean isBlockStateParticle,
        @NotNull Supplier<List<ResourceLocation>> supportedBlockResourceLocations_getter,
        Supplier<Boolean> overrideEnabled_getter,
        Supplier<Integer> maxParticlesOnPlace_getter,
        Supplier<Integer> maxParticlesOnBreak_getter,
        @NotNull Consumer<List<ResourceLocation>> supportedBlockResourceLocations_setter,
        Consumer<Boolean> overrideEnabled_setter,
        Consumer<Integer> maxParticlesOnPlace_setter,
        Consumer<Integer> maxParticlesOnBreak_setter,
        @NotNull List<ResourceLocation> supportedBlockResourceLocations_default,
        Boolean overrideEnabled_default,
        Integer maxParticlesOnPlace_default,
        Integer maxParticlesOnBreak_default
    ) {
        this.name = overrideName;
        this.groupName = groupName;
        this.isBlockStateParticle = isBlockStateParticle;
        this.supportedBlockResourceLocations_getter = supportedBlockResourceLocations_getter;
        this.overrideEnabled_getter = overrideEnabled_getter;
        this.maxParticlesOnPlace_getter = maxParticlesOnPlace_getter;
        this.maxParticlesOnBreak_getter = maxParticlesOnBreak_getter;
        this.supportedBlockResourceLocations_setter = supportedBlockResourceLocations_setter;
        this.overrideEnabled_setter = overrideEnabled_setter;
        this.maxParticlesOnPlace_setter = maxParticlesOnPlace_setter;
        this.maxParticlesOnBreak_setter = maxParticlesOnBreak_setter;
        this.supportedBlockResourceLocations_default = supportedBlockResourceLocations_default;
        this.overrideEnabled_default = overrideEnabled_default;
        this.maxParticlesOnPlace_default = maxParticlesOnPlace_default;
        this.maxParticlesOnBreak_default = maxParticlesOnBreak_default;
    }
    // normal particle type constructor
    BlockParticleOverride(
        String overrideName,
        String groupName,
        @Nullable ParticleType<?> particle,
        boolean isBlockStateParticle,
        @NotNull Supplier<List<ResourceLocation>> supportedBlockResourceLocations_getter,
        @NotNull Supplier<Boolean> overrideEnabled_getter,
        @NotNull Supplier<Integer> maxParticlesOnPlace_getter,
        @NotNull Supplier<Integer> maxParticlesOnBreak_getter,
        @NotNull Consumer<List<ResourceLocation>> supportedBlockResourceLocations_setter,
        @NotNull Consumer<Boolean> overrideEnabled_setter,
        @NotNull Consumer<Integer> maxParticlesOnPlace_setter,
        @NotNull Consumer<Integer> maxParticlesOnBreak_setter,
        @NotNull List<ResourceLocation> supportedBlockResourceLocations_default,
        boolean overrideEnabled_default,
        int maxParticlesOnPlace_default,
        int maxParticlesOnBreak_default
    ) {
        this(
            overrideName,
            groupName,
            isBlockStateParticle,
            supportedBlockResourceLocations_getter,
            overrideEnabled_getter,
            maxParticlesOnPlace_getter,
            maxParticlesOnBreak_getter,
            supportedBlockResourceLocations_setter,
            overrideEnabled_setter,
            maxParticlesOnPlace_setter,
            maxParticlesOnBreak_setter,
            supportedBlockResourceLocations_default,
            overrideEnabled_default,
            maxParticlesOnPlace_default,
            maxParticlesOnBreak_default
        );
        this.particleType = particle;
    }
    private BlockParticleOverride(String overrideName) {
        this.name = overrideName;
        this.groupName = overrideName;
        this.isBlockStateParticle = false;
        this.supportedBlockResourceLocations_getter = null;
        this.overrideEnabled_getter = null;
        this.maxParticlesOnPlace_getter = null;
        this.maxParticlesOnBreak_getter = null;
        this.supportedBlockResourceLocations_setter = null;
        this.overrideEnabled_setter = null;
        this.maxParticlesOnPlace_setter = null;
        this.maxParticlesOnBreak_setter = null;
        this.supportedBlockResourceLocations_default = null;
        this.overrideEnabled_default = null;
        this.maxParticlesOnPlace_default = null;
        this.maxParticlesOnBreak_default = null;
    }

    /**
     * @param blockState the {@link BlockState} to use when creating the particle options
     */
    public @Nullable ParticleOptions getParticleOptionForState(BlockState blockState) {
        if(this.isBlockStateParticle) {
            return new BlockParticleOption((ParticleType<BlockParticleOption>) this.particleType, blockState);
        };
        return (ParticleOptions) particleType;
    }

    @Override
    public @NotNull String toString() {
        return this.name;
    }

    public static BlockParticleOverride getOverrideForBlockState(BlockState blockState) {
        return getOverrideForBlockState(blockState, true);
    }

    public static BlockParticleOverride getOverrideForBlockState(BlockState blockState, boolean isBlockBeingPlaced) {
        Block block = blockState.getBlock();
        if(blockState.isAir()) return NONE;
        ResourceLocation blockLocation = RegistryHelper.getLocationFromBlock(block);

        BlockParticleOverride[] overrides = BlockParticleOverride.values();
        for (BlockParticleOverride override : overrides) {
            if (override.supportedBlockResourceLocations_getter == null) continue;
            List<ResourceLocation> locations = override.supportedBlockResourceLocations_getter.get();

            boolean overrideContainsThisBlock = locations.contains(blockLocation);
            if(!overrideContainsThisBlock) continue;

            if(override.overrideEnabled_getter.get()) {
                return override;
            }
        }
        if(BLOCK.overrideEnabled_getter.get()) return BLOCK;
        return NONE;
    }

    public static int getParticleMultiplierForOverride(BlockParticleOverride override, boolean isBlockBeingPlaced) {
        if(Objects.equals(override.name, "none")) return 0;
        return getAppropriateMultiplier(isBlockBeingPlaced, override.maxParticlesOnPlace_getter.get(), override.maxParticlesOnBreak_getter.get());
    }

    private static int getAppropriateMultiplier(boolean isBlockBeingPlaced, int blockPlaceMultiplier, int blockBreakMultiplier) {
        if(isBlockBeingPlaced) {
            return blockPlaceMultiplier;
        }
        return blockBreakMultiplier;
    }

    public String getName() {
        return name;
    }

    public String getGroupName() {
        return groupName;
    }

    public @Nullable Supplier<List<ResourceLocation>> getSupportedBlockResourceLocations_getter() {
        return supportedBlockResourceLocations_getter;
    }

    public Supplier<Boolean> getOverrideEnabled_getter() {
        return overrideEnabled_getter;
    }

    public Supplier<Integer> getMaxParticlesOnPlace_getter() {
        return maxParticlesOnPlace_getter;
    }

    public Supplier<Integer> getMaxParticlesOnBreak_getter() {
        return maxParticlesOnBreak_getter;
    }

    public @Nullable Consumer<List<ResourceLocation>> getSupportedBlockResourceLocations_setter() {
        return supportedBlockResourceLocations_setter;
    }

    public Consumer<Boolean> getOverrideEnabled_setter() {
        return overrideEnabled_setter;
    }

    public Consumer<Integer> getMaxParticlesOnPlace_setter() {
        return maxParticlesOnPlace_setter;
    }

    public Consumer<Integer> getMaxParticlesOnBreak_setter() {
        return maxParticlesOnBreak_setter;
    }

    public Integer getMaxParticlesOnBreak_default() {
        return maxParticlesOnBreak_default;
    }

    public Integer getMaxParticlesOnPlace_default() {
        return maxParticlesOnPlace_default;
    }

    public Boolean getOverrideEnabled_default() {
        return overrideEnabled_default;
    }

    public @Nullable List<ResourceLocation> getSupportedBlockResourceLocations_default() {
        return supportedBlockResourceLocations_default;
    }
}
