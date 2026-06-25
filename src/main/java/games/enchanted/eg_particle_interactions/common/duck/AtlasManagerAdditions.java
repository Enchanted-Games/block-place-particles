package games.enchanted.eg_particle_interactions.common.duck;

import net.minecraft.resources.Identifier;

public interface AtlasManagerAdditions {
    Identifier eg_particle_interactions$atlasIdFromTexturePath(Identifier texturePath);
    Identifier eg_particle_interactions$texturePathFromAtlasId(Identifier atlasId);
}
