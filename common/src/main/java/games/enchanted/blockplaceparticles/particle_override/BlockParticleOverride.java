package games.enchanted.blockplaceparticles.particle_override;

import games.enchanted.blockplaceparticles.config.ConfigHandler;
import games.enchanted.blockplaceparticles.registry.BlockOrTagLocation;
import games.enchanted.blockplaceparticles.registry.RegistryHelpers;
import games.enchanted.blockplaceparticles.registry.TagUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlockParticleOverride {
    public static final int ORIGIN_BLOCK_PLACED = 1;
    public static final int ORIGIN_BLOCK_BROKEN = 2;
    public static final int ORIGIN_BLOCK_PARTICLE_OVERRIDDEN = 3;
    public static final int ORIGIN_ITEM_PARTICLE_OVERRIDDEN = 4;
    public static final int ORIGIN_BLOCK_BRUSHED = 5;
    public static final int ORIGIN_BLOCK_CRACK = 6;
    public static final int ORIGIN_FALLING_BLOCK_LANDED = 7;
    public static final int ORIGIN_FALLING_BLOCK_FALLING = 8;
    public static final int ORIGIN_BLOCK_INTERACTED_WITH = 9;
    public static final int ORIGIN_BLOCK_WALKED_THROUGH = 10;

    public static final BlockParticleOverride NONE = new BlockParticleOverride("none");
    public static final BlockParticleOverride VANILLA = new BlockParticleOverride(
        "vanilla_particle",
        "vanilla_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) -> new BlockParticleOption(ParticleTypes.BLOCK, blockState),
        () -> null,
        (val) -> {},
        List.of(),
        () -> ConfigHandler.block_enabled,
        (val) -> ConfigHandler.block_enabled = val,
        ConfigHandler.block_enabled_DEFAULT,
        () -> ConfigHandler.maxBlock_onPlace,
        (val) -> ConfigHandler.maxBlock_onPlace = val,
        ConfigHandler.maxBlock_onPlace_DEFAULT,
        () -> ConfigHandler.maxBlock_onBreak,
        (val) -> ConfigHandler.maxBlock_onBreak = val,
        ConfigHandler.maxBlock_onBreak_DEFAULT,
        1
    );
    private static final ArrayList<BlockParticleOverride> blockParticleOverrides = new ArrayList<>();

    private final String name;
    private final String groupName;
    @NotNull final BlockParticleOverride.ReplaceParticleFromOriginConsumer shouldReplaceParticleFromOrigin_getter;
    @NotNull final GetParticleOptionConsumer getParticleOption;
    @Nullable final Supplier<List<BlockOrTagLocation>> supportedBlockResourceLocations_getter;
    @Nullable final Consumer<List<BlockOrTagLocation>> supportedBlockResourceLocations_setter;
    @Nullable final List<BlockOrTagLocation> supportedBlockResourceLocations_default;
    final Supplier<Boolean> overrideEnabled_getter;
    final Consumer<Boolean> overrideEnabled_setter;
    final boolean overrideEnabled_default;
    final Supplier<Integer> maxParticlesOnPlace_getter;
    final Consumer<Integer> maxParticlesOnPlace_setter;
    final int maxParticlesOnPlace_default;
    final Supplier<Integer> maxParticlesOnBreak_getter;
    final Consumer<Integer> maxParticlesOnBreak_setter;
    final int maxParticlesOnBreak_default;

    final float particleVelocityMultiplier;

    /**
     * Instantiates a new Block Particle Override
     *
     * @param overrideName                            The override name
     * @param groupName                               The group name
     * @param getParticleOption                       A {@link GetParticleOptionConsumer} that returns a {@link ParticleOptions} to spawn when this override is enabled
     * @param supportedBlockResourceLocations_getter  A supplier for a list of block {@link ResourceLocation}s that this override should apply to
     * @param supportedBlockResourceLocations_setter  A consumer to set a list of block {@link ResourceLocation}s that this override should apply to. This is called when the user changes this from the config menu
     * @param supportedBlockResourceLocations_default A default list of block {@link ResourceLocation}s that this override should apply to. Used when the config is reset by the user
     * @param overrideEnabled_getter                  A supplier that returns a boolean if this override is enabled or not
     * @param overrideEnabled_setter                  A consumer that sets a boolean if this override is enabled or not. This is called when the user changes this from the config menu
     * @param overrideEnabled_default                 The default value if this override is enabled. Used when the config is reset by the user
     * @param maxParticlesOnPlace_getter              A supplier that returns the amount of particles to spawn when a block is placed
     * @param maxParticlesOnPlace_setter              A consumer that sets the amount of particles to spawn when a block is placed. This is called when the user changes this from the config menu
     * @param maxParticlesOnPlace_default             The default amount of particles to spawn when a block is placed. Used when the config is reset by the user
     * @param maxParticlesOnBreak_getter              A supplier that returns the amount of particles to spawn when a block is broken
     * @param maxParticlesOnBreak_setter              A consumer that sets the amount of particles to spawn when a block is broken. This is called when the user changes this from the config menu
     * @param maxParticlesOnBreak_default             The default amount of particles to spawn when a block is broken. Used when the config is reset by the user
     * @param particleVelocityMultiplier              An amount to multiply the velocity by when spawning a particle for this override
     */
    BlockParticleOverride(
        String overrideName,
        String groupName,
        @NotNull GetParticleOptionConsumer getParticleOption,
        @NotNull Supplier<List<BlockOrTagLocation>> supportedBlockResourceLocations_getter,
        @NotNull Consumer<List<BlockOrTagLocation>> supportedBlockResourceLocations_setter,
        @NotNull List<BlockOrTagLocation> supportedBlockResourceLocations_default,
        Supplier<Boolean> overrideEnabled_getter,
        Consumer<Boolean> overrideEnabled_setter,
        boolean overrideEnabled_default,
        Supplier<Integer> maxParticlesOnPlace_getter,
        Consumer<Integer> maxParticlesOnPlace_setter,
        int maxParticlesOnPlace_default,
        Supplier<Integer> maxParticlesOnBreak_getter,
        Consumer<Integer> maxParticlesOnBreak_setter,
        int maxParticlesOnBreak_default,
        float particleVelocityMultiplier
    ) {
        this.name = overrideName;
        this.groupName = groupName;
        this.shouldReplaceParticleFromOrigin_getter = (int overrideOrigin) -> true;
        this.getParticleOption = getParticleOption;
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
        this.particleVelocityMultiplier = particleVelocityMultiplier;
    }

    /**
     * Instantiates a new Block Particle Override
     *
     * @param overrideName                            The override name
     * @param groupName                               The group name
     * @param shouldReplaceParticleFromOrigin_getter  A {@link ReplaceParticleFromOriginConsumer} that returns if this override should apply in certain contexts.
     *                                                This acts differently from disabling the override entirely. Returning false here will use the vanilla particles, instead of particles from an override "underneath" this one (if any).
     * @param getParticleOption                       A {@link GetParticleOptionConsumer} that returns a {@link ParticleOptions} to spawn when this override is enabled
     * @param supportedBlockResourceLocations_getter  A supplier for a list of block {@link ResourceLocation}s that this override should apply to
     * @param supportedBlockResourceLocations_setter  A consumer to set a list of block {@link ResourceLocation}s that this override should apply to. This is called when the user changes this from the config menu
     * @param supportedBlockResourceLocations_default A default list of block {@link ResourceLocation}s that this override should apply to. Used when the config is reset by the user
     * @param overrideEnabled_getter                  A supplier that returns a boolean if this override is enabled or not
     * @param overrideEnabled_setter                  A consumer that sets a boolean if this override is enabled or not. This is called when the user changes this from the config menu
     * @param overrideEnabled_default                 The default value if this override is enabled. Used when the config is reset by the user
     * @param maxParticlesOnPlace_getter              A supplier that returns the amount of particles to spawn when a block is placed
     * @param maxParticlesOnPlace_setter              A consumer that sets the amount of particles to spawn when a block is placed. This is called when the user changes this from the config menu
     * @param maxParticlesOnPlace_default             The default amount of particles to spawn when a block is placed. Used when the config is reset by the user
     * @param maxParticlesOnBreak_getter              A supplier that returns the amount of particles to spawn when a block is broken
     * @param maxParticlesOnBreak_setter              A consumer that sets the amount of particles to spawn when a block is broken. This is called when the user changes this from the config menu
     * @param maxParticlesOnBreak_default             The default amount of particles to spawn when a block is broken. Used when the config is reset by the user
     * @param particleVelocityMultiplier              An amount to multiply the velocity by when spawning a particle for this override
     */
    BlockParticleOverride(
        String overrideName,
        String groupName,
        @NotNull BlockParticleOverride.ReplaceParticleFromOriginConsumer shouldReplaceParticleFromOrigin_getter,
        @NotNull GetParticleOptionConsumer getParticleOption,
        @NotNull Supplier<List<BlockOrTagLocation>> supportedBlockResourceLocations_getter,
        @NotNull Consumer<List<BlockOrTagLocation>> supportedBlockResourceLocations_setter,
        @NotNull List<BlockOrTagLocation> supportedBlockResourceLocations_default,
        Supplier<Boolean> overrideEnabled_getter,
        Consumer<Boolean> overrideEnabled_setter,
        boolean overrideEnabled_default,
        Supplier<Integer> maxParticlesOnPlace_getter,
        Consumer<Integer> maxParticlesOnPlace_setter,
        int maxParticlesOnPlace_default,
        Supplier<Integer> maxParticlesOnBreak_getter,
        Consumer<Integer> maxParticlesOnBreak_setter,
        int maxParticlesOnBreak_default,
        float particleVelocityMultiplier
    ) {
        this.name = overrideName;
        this.groupName = groupName;
        this.shouldReplaceParticleFromOrigin_getter = shouldReplaceParticleFromOrigin_getter;
        this.getParticleOption = getParticleOption;
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
        this.particleVelocityMultiplier = particleVelocityMultiplier;
    }

    private BlockParticleOverride(String overrideName) {
        this.name = overrideName;
        this.groupName = overrideName;
        this.shouldReplaceParticleFromOrigin_getter = (int overrideOrigin) -> true;
        this.getParticleOption = (BlockState state, ClientLevel level, BlockPos pos, int overrideOrigin) -> null;
        this.supportedBlockResourceLocations_getter = null;
        this.overrideEnabled_getter = null;
        this.maxParticlesOnPlace_getter = null;
        this.maxParticlesOnBreak_getter = null;
        this.supportedBlockResourceLocations_setter = null;
        this.overrideEnabled_setter = null;
        this.maxParticlesOnPlace_setter = null;
        this.maxParticlesOnBreak_setter = null;
        this.supportedBlockResourceLocations_default = null;
        this.overrideEnabled_default = true;
        this.maxParticlesOnPlace_default = 0;
        this.maxParticlesOnBreak_default = 0;
        this.particleVelocityMultiplier = 1;
    }

    public interface ReplaceParticleFromOriginConsumer {
        boolean consume(int overrideOrigin);
    }
    public interface GetParticleOptionConsumer {
        ParticleOptions consume(BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin);
    }

    /**
     * @param blockState the {@link BlockState} to use when creating the particle options
     */
    public @Nullable ParticleOptions getParticleOptionForState(BlockState blockState, ClientLevel level, BlockPos blockPos, int overrideOrigin) {
        return getParticleOption.consume(blockState, level, blockPos, overrideOrigin);
    }

    @Override
    public @NotNull String toString() {
        return this.name;
    }

    public static BlockParticleOverride getOverrideForBlockState(BlockState blockState, int overrideOrigin) {
        Block block = blockState.getBlock();
        if(blockState.isAir()) return NONE;
        ResourceLocation blockResourceLocation = RegistryHelpers.getLocationFromBlock(block);

        BlockParticleOverride returnOverride = null;
        for (BlockParticleOverride override : BlockParticleOverride.blockParticleOverrides) {
            if (!doesOverrideContainBlock(override, blockResourceLocation)) continue;

            if(override.overrideEnabled_getter.get()) {
                returnOverride = override;
                break;
            }
        }

        if(returnOverride != null && returnOverride.shouldReplaceParticleFromOrigin(overrideOrigin)) {
            return returnOverride;
        }

        if(VANILLA.overrideEnabled_getter.get()) return VANILLA;
        return NONE;
    }

    // TODO: separate this into a general util function
    private static boolean doesOverrideContainBlock(BlockParticleOverride override, ResourceLocation blockResourceLocation) {
        if (override.supportedBlockResourceLocations_getter == null) return false;
        return TagUtil.doesListContainBlock(override.supportedBlockResourceLocations_getter.get(), blockResourceLocation);
    }

    public static int getParticleMultiplierForOverride(BlockParticleOverride override, boolean isBlockBeingPlaced) {
        if(override == NONE) return 0;
        return getAppropriateMultiplier(isBlockBeingPlaced, override.maxParticlesOnPlace_getter.get(), override.maxParticlesOnBreak_getter.get());
    }

    private static int getAppropriateMultiplier(boolean isBlockBeingPlaced, int blockPlaceMultiplier, int blockBreakMultiplier) {
        if(isBlockBeingPlaced) {
            return blockPlaceMultiplier;
        }
        return blockBreakMultiplier;
    }

    /**
     * @return a list of all the current {@link BlockParticleOverride}s that have been added to the mod
     */
    public static List<BlockParticleOverride> getBlockParticleOverrides() {
        return blockParticleOverrides.stream().toList();
    }

    /**
     * Add a {@link BlockParticleOverride} to be used by the mod
     *
     * @param override the override to add
     */
    public static void addBlockParticleOverride(BlockParticleOverride override) {
        if(override == NONE || override == VANILLA) throw new IllegalArgumentException("Cannot call BlockParticleOverride#addBlockParticleOverride with BlockParticleOverride.NONE or BlockParticleOverride.BLOCK");
        blockParticleOverrides.add(override);
    }

    public String getName() {
        return name;
    }

    public String getGroupName() {
        return groupName;
    }

    public @Nullable Supplier<List<BlockOrTagLocation>> getSupportedBlockResourceLocations_getter() {
        return supportedBlockResourceLocations_getter;
    }

    public @Nullable Consumer<List<BlockOrTagLocation>> getSupportedBlockResourceLocations_setter() {
        return supportedBlockResourceLocations_setter;
    }

    public @Nullable List<BlockOrTagLocation> getSupportedBlockResourceLocations_default() {
        return supportedBlockResourceLocations_default;
    }

    public Supplier<Integer> getMaxParticlesOnPlace_getter() {
        return maxParticlesOnPlace_getter;
    }

    public Consumer<Integer> getMaxParticlesOnPlace_setter() {
        return maxParticlesOnPlace_setter;
    }

    public int getMaxParticlesOnPlace_default() {
        return maxParticlesOnPlace_default;
    }

    public Supplier<Integer> getMaxParticlesOnBreak_getter() {
        return maxParticlesOnBreak_getter;
    }

    public Consumer<Integer> getMaxParticlesOnBreak_setter() {
        return maxParticlesOnBreak_setter;
    }

    public int getMaxParticlesOnBreak_default() {
        return maxParticlesOnBreak_default;
    }

    public Supplier<Boolean> getOverrideEnabled_getter() {
        return overrideEnabled_getter;
    }

    public Consumer<Boolean> getOverrideEnabled_setter() {
        return overrideEnabled_setter;
    }

    public boolean getOverrideEnabled_default() {
        return overrideEnabled_default;
    }

    public float getParticleVelocityMultiplier() {
        return particleVelocityMultiplier;
    }

    public boolean shouldReplaceParticleFromOrigin(int overrideOrigin) {
        return this.shouldReplaceParticleFromOrigin_getter.consume(overrideOrigin);
    }
}
