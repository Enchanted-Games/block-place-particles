package games.enchanted.blockplaceparticles.particle_spawning.override;

import games.enchanted.blockplaceparticles.config.ConfigHandler;
import games.enchanted.blockplaceparticles.util.RegistryHelper;
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
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlockParticleOverride {
    public static final BlockParticleOverride NONE = new BlockParticleOverride("none");
    public static final BlockParticleOverride BLOCK = new BlockParticleOverride(
        "vanilla_particle",
        "generic_block_override",
        (BlockState blockState, ClientLevel level, BlockPos blockPos) -> new BlockParticleOption(ParticleTypes.BLOCK, blockState),
        null,
        null,
        List.of(),
        () -> ConfigHandler.block_enabled,
        (val) -> ConfigHandler.block_enabled = val,
        ConfigHandler.block_enabled_DEFAULT,
        () -> ConfigHandler.maxBlock_onPlace,
        (val) -> ConfigHandler.maxBlock_onPlace = val,
        ConfigHandler.maxBlock_onPlace_DEFAULT,
        () -> ConfigHandler.maxBlock_onBreak,
        (val) -> ConfigHandler.maxBlock_onBreak = val,
        ConfigHandler.maxBlock_onBreak_DEFAULT
    );
    private static final ArrayList<BlockParticleOverride> blockParticleOverrides = new ArrayList<>();

    private final String name;
    private final String groupName;
    @NotNull final GetParticleOptionConsumer getParticleOption;
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

    BlockParticleOverride(
        String overrideName,
        String groupName,
        @NotNull GetParticleOptionConsumer getParticleOption,
        @NotNull Supplier<List<ResourceLocation>> supportedBlockResourceLocations_getter,
        @NotNull Consumer<List<ResourceLocation>> supportedBlockResourceLocations_setter,
        @NotNull List<ResourceLocation> supportedBlockResourceLocations_default,
        Supplier<Boolean> overrideEnabled_getter,
        Consumer<Boolean> overrideEnabled_setter,
        Boolean overrideEnabled_default,
        Supplier<Integer> maxParticlesOnPlace_getter,
        Consumer<Integer> maxParticlesOnPlace_setter,
        Integer maxParticlesOnPlace_default,
        Supplier<Integer> maxParticlesOnBreak_getter,
        Consumer<Integer> maxParticlesOnBreak_setter,
        Integer maxParticlesOnBreak_default
    ) {
        this.name = overrideName;
        this.groupName = groupName;
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
    }
    private BlockParticleOverride(String overrideName) {
        this.name = overrideName;
        this.groupName = overrideName;
        this.getParticleOption = (BlockState state, ClientLevel level, BlockPos pos) -> null;
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
    public @Nullable ParticleOptions getParticleOptionForState(BlockState blockState, ClientLevel level, BlockPos blockPos) {
        return getParticleOption.consume(blockState, level, blockPos);
    }

    @Override
    public @NotNull String toString() {
        return this.name;
    }

    public static BlockParticleOverride getOverrideForBlockState(BlockState blockState) {
        Block block = blockState.getBlock();
        if(blockState.isAir()) return NONE;
        ResourceLocation blockLocation = RegistryHelper.getLocationFromBlock(block);

        for (BlockParticleOverride override : BlockParticleOverride.blockParticleOverrides) {
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

    public interface GetParticleOptionConsumer {
        ParticleOptions consume(BlockState blockState, ClientLevel level, BlockPos blockPos);
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
        blockParticleOverrides.add(override);
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
