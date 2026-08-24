package games.enchanted.eg_particle_interactions.common.particle.render.layer;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.LayerDefinition;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.appearance.texture.TextureConfig;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public record ParticleLayer(boolean translucent, Identifier atlasTexture, @Nullable Identifier maskAtlasTexture, RenderPipeline pipeline) {
    private static final Map<Identity, ParticleLayer> EXISTING_LAYERS = new HashMap<>();

    public static ParticleLayer fromAppearance(ParticleContext context, ParticleAppearance appearance) {
        TextureConfig config = appearance.textureConfig();
        TextureConfig maskConfig = appearance.maskConfig();

        Identity identity = new Identity(
            config.getAtlas(context).texturePath(),
            maskConfig.getAtlas(context).texturePath(),
            config.getLayerDefinition(context)
        );
        if(EXISTING_LAYERS.containsKey(identity)) {
            return EXISTING_LAYERS.get(identity);
        }

        boolean hasMask = appearance.maskConfig().containsValidMaskSprites();
        LayerDefinition layerDefinition = config.getLayerDefinition(context);
        ParticleLayer layer = new ParticleLayer(
            layerDefinition.isTranslucent(),
            config.getAtlas(context).texturePath(),
            hasMask ? maskConfig.getAtlas(context).texturePath() : null,
            hasMask ? layerDefinition.maskPipeline() : layerDefinition.pipeline()
        );
        EXISTING_LAYERS.put(identity, layer);
        return layer;
    }

    private record Identity(Identifier atlasTexture, @Nullable Identifier maskAtlasTexture, LayerDefinition layerDefinition) {
    }
}
