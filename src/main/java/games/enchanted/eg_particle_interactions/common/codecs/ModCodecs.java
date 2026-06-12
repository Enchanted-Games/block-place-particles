package games.enchanted.eg_particle_interactions.common.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.util.texture.AtlasIdAndTexture;
import net.minecraft.IdentifierException;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.joml.Vector3d;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class ModCodecs {
    public static final Codec<Identifier> IDENTIFIER = Codec.STRING.comapFlatMap(
        id -> {
            int sepIndex = id.indexOf(Identifier.NAMESPACE_SEPARATOR);
            if(sepIndex >= 0) {
                String path = id.substring(sepIndex + 1);
                if(sepIndex == 0) {
                    return DataResult.success(ParticleInteractionsMod.id(path));
                } else {
                    String namespace = id.substring(0, sepIndex);
                    return DataResult.success(Identifier.fromNamespaceAndPath(namespace, path));
                }
            }
            return DataResult.success(ParticleInteractionsMod.id(id));
        },
        Identifier::toString
    );

    public static @Nullable Identifier tryParseIdentifier(String id) {
        try {
            int sepIndex = id.indexOf(Identifier.NAMESPACE_SEPARATOR);
            if(sepIndex >= 0) {
                String path = id.substring(sepIndex + 1);
                if(sepIndex == 0) {
                    return ParticleInteractionsMod.id(path);
                } else {
                    String namespace = id.substring(0, sepIndex);
                    return Identifier.fromNamespaceAndPath(namespace, path);
                }
            }
            return ParticleInteractionsMod.id(id);
        } catch (IdentifierException e) {
            return null;
        }
    }

    public static final Codec<AtlasIdAndTexture> ATLAS = Identifier.CODEC.xmap(
        identifier -> {
            var atlas = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(identifier);
            return new AtlasIdAndTexture(identifier, atlas.location());
        },
        AtlasIdAndTexture::id
    );

    public static final Codec<Vector3d> VECTOR3D = Codec.DOUBLE.listOf().comapFlatMap(
        (input) -> Util.fixedSize(input, 3).map((d) -> new Vector3d(d.get(0), d.get(1), d.get(2))),
        (vec) -> List.of(vec.x(), vec.y(), vec.z())
    );

    public static final Codec<Vector3d> COMPACT_VECTOR3D = VECTOR3D.withAlternative(
        Codec.DOUBLE.xmap(Vector3d::new, Vector3d::x)
    );
}
