package games.enchanted.eg_particle_interactions.common.resource.version;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.Constants;
import net.minecraft.server.packs.OverlayMetadataSection;

public record ParticleInteractionsPackOverlay(PackVersionRange range, boolean ignoreVanillaFormats) {
    private static final Codec<ParticleInteractionsPackOverlay> VERSION_AND_BYPASS_FORMAT = RecordCodecBuilder.create(i -> i
        .group(
            PackVersionRange.CODEC.fieldOf("version").forGetter(ParticleInteractionsPackOverlay::range),
            Codec.BOOL.fieldOf("ignore_vanilla_formats").forGetter(ParticleInteractionsPackOverlay::ignoreVanillaFormats)
        ).apply(
            i,
            ParticleInteractionsPackOverlay::new
        )
    );

    private static final Codec<ParticleInteractionsPackOverlay> CODEC = VERSION_AND_BYPASS_FORMAT
        .optionalFieldOf(Constants.MOD_ID, new ParticleInteractionsPackOverlay(new PackVersionRange(PackVersion.UNSPECIFIED, PackVersion.UNSPECIFIED), false))
        .codec();

    public static Codec<OverlayMetadataSection.OverlayEntry.IntermediateEntry> wrapOverlayCodec(Codec<OverlayMetadataSection.OverlayEntry.IntermediateEntry> original) {
        return Codec.pair(CODEC, original).xmap(
            pair -> {
                OverlayMetadataSection.OverlayEntry.IntermediateEntry overlayEntry = pair.getSecond();
                ((PackOverlayDuck) (Object) overlayEntry).eg_particle_interactions$setCustomOverlay(pair.getFirst());
                return overlayEntry;
            },
            overlayEntry -> Pair.of(((PackOverlayDuck) (Object) overlayEntry).eg_particle_interactions$getCustomOverlay(), overlayEntry)
        );
    }
}
