package games.enchanted.eg_particle_interactions.common.particle.render.layer;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.appearance.LayerDefinition;
import games.enchanted.eg_particle_interactions.common.particle.appearance.texture.TextureConfig;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
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

    public SingleQuadParticle.Layer layer() {
        return this.layer;
    }

    public static final ParticleLayer CUTOUT = new ParticleLayer(
        SingleQuadParticle.Layer.OPAQUE
    );
    public static final ParticleLayer TERRAIN = new ParticleLayer(
        SingleQuadParticle.Layer.TRANSLUCENT_TERRAIN
    );
    public static final ParticleLayer BACKFACE_TERRAIN = new ParticleLayer(
        ParticleInteractionsParticle.BACKFACE_TERRAIN_LAYER
    );

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
