package games.enchanted.eg_particle_interactions.common.particle.overrides;

import games.enchanted.eg_particle_interactions.common.config.categories.BlockOverrideOptions;
import games.enchanted.eg_particle_interactions.common.config.option.BoolOption;
import games.enchanted.eg_particle_interactions.common.config.option.ConfigOption;
import games.enchanted.eg_particle_interactions.common.registry.BlockOrTagLocation;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import games.enchanted.eg_particle_interactions.common.registry.TagUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// TODO: rewrite this, its gotten to a point where there are way too many arguments per constructor
// possibly change it to each block/tag/fluid/whatever specifying the override it uses, rather than the override
//   specifying the block/tag/fluid/whatever it applies to
public class BlockParticleOverride {
    public static final BlockParticleOverride NONE = new BlockParticleOverride("none");
    public static final BlockParticleOverride VANILLA = new BlockParticleOverride(
        "vanilla_particle",
        "vanilla_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos, ParticleOrigin overrideOrigin) -> new BlockParticleOption(ParticleTypes.BLOCK, blockState),
        BlockOverrideOptions.VANILLA_BLOCK_PARTICLE,
        1,
        List.of()
    );
    private static final ArrayList<BlockParticleOverride> blockParticleOverrides = new ArrayList<>();

    private final String name;
    private final String groupName;
    @NotNull final BlockParticleOverride.ReplaceParticleFromOriginSupplier shouldReplaceParticleFromOrigin;
    @NotNull final BlockParticleOverride.ParticleSupplier particleSupplier;
    @Nullable final ConfigOption<List<BlockOrTagLocation>> supportedBlocksOption;
    final ConfigOption<Boolean> isEnabledOption;
    final ConfigOption<Integer> maxParticleOnPlaceOption;
    final ConfigOption<Integer> maxParticleOnBreakOption;
    final List<OptionToggle> extraToggles;

    final float particleVelocityMultiplier;

    /**
     * Instantiates a new Block Particle Override
     *
     * @param overrideName                            The override name
     * @param groupName                               The group name
     * @param particleSupplier                        A {@link ParticleSupplier} that returns a {@link ParticleOptions} to spawn when this override is enabled
     * @param optionSet                               A {@link BlockOverrideOptions.BlockParticleOptionSet}
     * @param particleVelocityMultiplier              An amount to multiply the velocity by when spawning a particle for this override
     * @param extraToggles                            Extra options that will appear next to this override in the config menu
     */
    BlockParticleOverride(
        String overrideName,
        String groupName,
        @NotNull BlockParticleOverride.ParticleSupplier particleSupplier,
        BlockOverrideOptions.BlockParticleOptionSet optionSet,
        float particleVelocityMultiplier,
        List<OptionToggle> extraToggles
    ) {
        this(overrideName, groupName, particleSupplier, (ParticleOrigin overrideOrigin) -> true, optionSet, particleVelocityMultiplier, extraToggles);
    }

    /**
     * Instantiates a new Block Particle Override
     *
     * @param overrideName                            The override name
     * @param groupName                               The group name
     * @param particleSupplier                        A {@link ParticleSupplier} that returns a {@link ParticleOptions} to spawn when this override is enabled
     * @param shouldReplaceParticleFromOrigin         A {@link ReplaceParticleFromOriginSupplier} that returns if this override should apply in certain contexts.
     *                                                This acts differently from disabling the override entirely. Returning false here will use the vanilla particles, instead of particles from an override "underneath" this one (if any).
     * @param optionSet                               A {@link BlockOverrideOptions.BlockParticleOptionSet}
     * @param particleVelocityMultiplier              An amount to multiply the velocity by when spawning a particle for this override
     * @param extraToggles                            Extra options that will appear next to this override in the config menu
     */
    BlockParticleOverride(
        String overrideName,
        String groupName,
        @NotNull BlockParticleOverride.ParticleSupplier particleSupplier,
        @NotNull BlockParticleOverride.ReplaceParticleFromOriginSupplier shouldReplaceParticleFromOrigin,
        BlockOverrideOptions.BlockParticleOptionSet optionSet,
        float particleVelocityMultiplier,
        List<OptionToggle> extraToggles
    ) {
        this.name = overrideName;
        this.groupName = groupName;
        this.shouldReplaceParticleFromOrigin = shouldReplaceParticleFromOrigin;
        this.particleSupplier = particleSupplier;
        this.supportedBlocksOption = optionSet.blocksOption();
        this.isEnabledOption = optionSet.enabledOption();
        this.maxParticleOnPlaceOption = optionSet.maxOnPlaceOption();
        this.maxParticleOnBreakOption = optionSet.maxOnBreakOption();
        this.particleVelocityMultiplier = particleVelocityMultiplier;
        this.extraToggles = extraToggles;
    }

    private BlockParticleOverride(String overrideName) {
        this.name = overrideName;
        this.groupName = overrideName;
        this.shouldReplaceParticleFromOrigin = (ParticleOrigin overrideOrigin) -> true;
        this.particleSupplier = (BlockState state, ClientLevel level, BlockPos pos, ParticleOrigin overrideOrigin) -> null;
        this.supportedBlocksOption = null;
        this.isEnabledOption = null;
        this.maxParticleOnPlaceOption = null;
        this.maxParticleOnBreakOption = null;
        this.particleVelocityMultiplier = 1;
        this.extraToggles = List.of();
    }

    public interface ReplaceParticleFromOriginSupplier {
        boolean consume(ParticleOrigin overrideOrigin);
    }
    public interface ParticleSupplier {
        ParticleOptions consume(BlockState blockState, ClientLevel level, BlockPos blockPos, ParticleOrigin overrideOrigin);
    }

    /**
     * @param blockState the {@link BlockState} to use when creating the particle options
     */
    public @Nullable ParticleOptions getParticleOptionForState(BlockState blockState, ClientLevel level, BlockPos blockPos, ParticleOrigin overrideOrigin) {
        return particleSupplier.consume(blockState, level, blockPos, overrideOrigin);
    }

    @Override
    public @NotNull String toString() {
        return this.name;
    }

    public static BlockParticleOverride getOverrideForBlockState(BlockState blockState, ParticleOrigin overrideOrigin) {
        Block block = blockState.getBlock();
        if(blockState.isAir()) return NONE;
        Identifier blockIdentifier = RegistryHelpers.getLocationFromBlock(block);

        BlockParticleOverride returnOverride = null;
        for (BlockParticleOverride override : BlockParticleOverride.blockParticleOverrides) {
            if (!doesOverrideContainBlock(override, blockIdentifier)) continue;

            if(override.isEnabled()) {
                returnOverride = override;
                break;
            }
        }

        if(returnOverride != null && returnOverride.shouldReplaceParticleFromOrigin(overrideOrigin)) {
            return returnOverride;
        }

        if(VANILLA.isEnabled()) return VANILLA;
        return NONE;
    }

    private static boolean doesOverrideContainBlock(BlockParticleOverride override, Identifier blockIdentifier) {
        if (override.getSupportedBlocksAndTags() == null) return false;
        return TagUtil.doesListContainBlock(override.getSupportedBlocksAndTags(), blockIdentifier);
    }

    public static int getParticleMultiplierForOverride(BlockParticleOverride override, boolean isBlockBeingPlaced) {
        if(override == NONE) return 0;
        return getAppropriateMultiplier(isBlockBeingPlaced, override.getMaxParticlesOnPlace(), override.getMaxParticlesOnBreak());
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

    /**
     * Gets blocks and tags this override applies to
     *
     * @return the supported blocks and tags, null if VANILLA or NONE override
     */
    public @Nullable List<BlockOrTagLocation> getSupportedBlocksAndTags() {
        if(this.supportedBlocksOption == null) return null;
        return this.supportedBlocksOption.getValue();
    }

    public boolean isEnabled() {
        return this.isEnabledOption.getValue();
    }

    public int getMaxParticlesOnPlace() {
        return this.maxParticleOnPlaceOption.getValue();
    }

    public int getMaxParticlesOnBreak() {
        return this.maxParticleOnBreakOption.getValue();
    }

    public float getParticleVelocityMultiplier() {
        return particleVelocityMultiplier;
    }

    public boolean shouldReplaceParticleFromOrigin(ParticleOrigin overrideOrigin) {
        return this.shouldReplaceParticleFromOrigin.consume(overrideOrigin);
    }

    public @Nullable ConfigOption<List<BlockOrTagLocation>> getSupportedBlocksAndTagsOption() {
        return this.supportedBlocksOption;
    }

    public ConfigOption<Boolean> getEnabledOption() {
        return this.isEnabledOption;
    }

    public ConfigOption<Integer> getMaxParticlesOnPlaceOption() {
        return this.maxParticleOnPlaceOption;
    }

    public ConfigOption<Integer> getMaxParticlesOnBreakOption() {
        return this.maxParticleOnBreakOption;
    }

    public List<OptionToggle> getExtraToggles() {
        return this.extraToggles;
    }

    public record OptionToggle(String labelKey, ConfigOption<Boolean> option) {
    }
}
