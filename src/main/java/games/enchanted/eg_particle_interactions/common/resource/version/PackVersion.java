package games.enchanted.eg_particle_interactions.common.resource.version;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

import java.util.List;
import java.util.function.BiFunction;

public record PackVersion(int major, int minor, int patch) {
    public static final PackVersion UNSPECIFIED = new PackVersion(-1, -1, -1);
    public static final int WILDCARD = Integer.MIN_VALUE;

    private static final Codec<Integer> COMPONENT_CODEC = Codec.withAlternative(
        Codec.string(1,1)
            .validate(s -> s.equals("*") ? DataResult.success(s) : DataResult.error(() -> "Invalid wildcard: " + s))
            .xmap(
                s -> WILDCARD,
                integer -> "*"
            ),
        ExtraCodecs.NON_NEGATIVE_INT
    );

    private static final Codec<PackVersion> MAJOR_MINOR_PATCH_CODEC = RecordCodecBuilder.create(i -> i
        .group(
            COMPONENT_CODEC.fieldOf("major").forGetter(PackVersion::major),
            COMPONENT_CODEC.fieldOf("minor").forGetter(PackVersion::minor),
            COMPONENT_CODEC.fieldOf("patch").forGetter(PackVersion::patch)
        ).apply(
            i,
            PackVersion::new
        )
    );

    private static final Codec<PackVersion> LIST_CODEC = Codec.list(COMPONENT_CODEC, 3, 3).xmap(
        ints -> new PackVersion(ints.get(0), ints.get(1), ints.get(2)),
        packVersion -> List.of(packVersion.major(), packVersion.minor(), packVersion.patch())
    );

    public static final Codec<PackVersion> CODEC = Codec.withAlternative(MAJOR_MINOR_PATCH_CODEC, LIST_CODEC);

    public boolean is(PackVersion version) {
        return this.major() == version.major() && this.minor() == version.minor() && this.patch() == version.patch();
    }

    public boolean isLessThan(PackVersion version) {
        return compare(version, false, (thisNum, thatNum) -> thisNum < thatNum);
    }

    public boolean isGreaterThan(PackVersion version) {
        return compare(version, true, (thisNum, thatNum) -> thisNum > thatNum);
    }

    private boolean compare(PackVersion version, boolean includeWildcards, BiFunction<Integer, Integer, Boolean> numberComparison) {
        int majorA = this.major();
        int minorA = this.minor();
        int patchA = this.patch();
        int majorB = version.major();
        int minorB = version.minor();
        int patchB = version.patch();

        if(includeWildcards && (checkWildcards(Component.MAJOR) || version.checkWildcards(Component.MAJOR))) return false;
        if(majorA != majorB) return numberComparison.apply(majorA, majorB);

        if(includeWildcards && (checkWildcards(Component.MINOR) || version.checkWildcards(Component.MINOR))) return false;
        if(minorA != minorB) return numberComparison.apply(minorA, minorB);

        if(includeWildcards && (checkWildcards(Component.MINOR) || version.checkWildcards(Component.PATCH))) return false;
        return numberComparison.apply(patchA, patchB);
    }

    public boolean hasWildcards() {
        return checkWildcards(Component.MAJOR) || checkWildcards(Component.MINOR) || checkWildcards(Component.PATCH);
    }

    public boolean checkWildcards(Component component) {
        if(component == Component.MAJOR && this.major() == WILDCARD) return true;
        if(component == Component.MINOR && this.minor() == WILDCARD) return true;
        if(component == Component.PATCH && this.patch() == WILDCARD) return true;
        return false;
    }

    public enum Component {
        MAJOR,
        MINOR,
        PATCH
    }
}
