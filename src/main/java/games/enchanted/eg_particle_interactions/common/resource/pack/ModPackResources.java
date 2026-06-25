package games.enchanted.eg_particle_interactions.common.resource.pack;

import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.flag.FeatureFlagSet;

import java.util.List;
import java.util.Optional;

public class ModPackResources extends PathPackResources {
    public static PackLocationInfo LOCATION = new PackLocationInfo(
        Constants.MOD_ID,
        Component.literal(Constants.MOD_NAME),
        PackSource.BUILT_IN,
        Optional.empty()
    );
    public static Pack.Metadata METADATA = new Pack.Metadata(
        Component.literal(Constants.MOD_NAME).append(" resources"),
        PackCompatibility.COMPATIBLE,
        FeatureFlagSet.of(),
        List.of()
    );
    public static PackSelectionConfig SELECTION_CONFIG = new PackSelectionConfig(
        true,
        Pack.Position.BOTTOM,
        false
    );

    ModPackResources(PackLocationInfo location) {
        super(location, PlatformHelper.getResourcePathFromModJar());
    }

    public static Pack.ResourcesSupplier createResourcesSupplier() {
        return new Pack.ResourcesSupplier() {
            @Override
            public PackResources openPrimary(PackLocationInfo location) {
                return new ModPackResources(location);
            }

            @Override
            public PackResources openFull(PackLocationInfo location, Pack.Metadata metadata) {
                return new ModPackResources(location);
            }
        };
    }

    public static Pack createPack() {
        return new Pack(
            ModPackResources.LOCATION,
            ModPackResources.createResourcesSupplier(),
            ModPackResources.METADATA,
            ModPackResources.SELECTION_CONFIG
        );
    }
}
