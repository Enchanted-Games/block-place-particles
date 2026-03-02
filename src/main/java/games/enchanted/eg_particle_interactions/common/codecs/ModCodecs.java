package games.enchanted.eg_particle_interactions.common.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import net.minecraft.resources.Identifier;

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
}
