package games.enchanted.eg_particle_interactions.common.particle.render.layer;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.LayerDefinition;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.appearance.texture.TextureConfig;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ParticleLayer {
    private static final Map<Identity, ParticleLayer> EXISTING_LAYERS = new HashMap<>();

    final SingleQuadParticle.Layer layer;

    ParticleLayer(SingleQuadParticle.Layer layer) {
        this.layer = layer;
    }

    public SingleQuadParticle.Layer vanillaLayer() {
        return this.layer;
    }

    public static ParticleLayer fromAppearance(ParticleContext context, ParticleAppearance appearance) {
        TextureConfig config = appearance.textureConfig();

        Identity identity = new Identity(config.getAtlas(context).texturePath(), config.getLayerDefinition(context));
        if(EXISTING_LAYERS.containsKey(identity)) {
            return EXISTING_LAYERS.get(identity);
        }

        ParticleLayer layer = new ParticleLayer(createVanillaLayer(context, config));
        EXISTING_LAYERS.put(identity, layer);
        return layer;
    }

    private static SingleQuadParticle.Layer createVanillaLayer(ParticleContext context, TextureConfig config) {
        return new SingleQuadParticle.Layer(
            config.getLayerDefinition(context).isTranslucent(),
            config.getAtlas(context).texturePath(),
            config.getLayerDefinition(context).pipeline()
        );
    }

    private record Identity(Identifier atlasTexture, LayerDefinition layerDefinition) {
    }
}
