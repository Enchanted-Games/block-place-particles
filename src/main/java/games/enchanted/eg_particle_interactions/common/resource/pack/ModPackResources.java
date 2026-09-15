package games.enchanted.eg_particle_interactions.common.resource.pack;

import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.world.flag.FeatureFlagSet;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
            //? if minecraft: <= 26.2 {
            /*@Override
            public PackResources openPrimary(PackLocationInfo location) {
                return new ModPackResources(location);
            }

            @Override
            public PackResources openFull(PackLocationInfo location, Pack.Metadata metadata) {
                return new ModPackResources(location);
            }
            *///? } else {
            private ModPackResources openPrimaryResources(final PackLocationInfo location) {
                return new ModPackResources(location);
            }

            @Override
            public PackMetadataResources openMetadata(final PackLocationInfo location) {
                return this.openPrimaryResources(location);
            }

            @Override
            public Stream<PackResources> openResources(final PackLocationInfo location, final Pack.Metadata metadata) {
                return Stream.of(this.openPrimaryResources(location));
            }
            //? }
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
