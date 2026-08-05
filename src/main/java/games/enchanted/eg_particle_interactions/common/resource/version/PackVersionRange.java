package games.enchanted.eg_particle_interactions.common.resource.version;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record PackVersionRange(PackVersion min, PackVersion max) {
    public static final Codec<PackVersionRange> CODEC = RecordCodecBuilder.<PackVersionRange>create(i -> i
        .group(
            PackVersion.CODEC.fieldOf("min").forGetter(PackVersionRange::min),
            PackVersion.CODEC.fieldOf("max").forGetter(PackVersionRange::max)
        ).apply(
            i,
            PackVersionRange::new
        )
    ).validate(range -> {
        if(range.min().hasWildcards()) {
            return DataResult.error(() -> "Min version cannot have wildcards");
        }
        if(range.min().isGreaterThan(range.max())) {
            return DataResult.error(() -> "Min version cannot be larger than max version");
        }
        return DataResult.success(range);
    });

    public boolean containsVersion(PackVersion version) {
        if(version.is(PackVersion.UNSPECIFIED)) return false;
        if(this.min().is(PackVersion.UNSPECIFIED) || this.max().is(PackVersion.UNSPECIFIED)) return false;

        return !version.isLessThan(this.min()) && !version.isGreaterThan(this.max());
    }
}
