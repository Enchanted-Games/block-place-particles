package games.enchanted.eg_particle_interactions.common.particle.render.layer;

import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
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
        //? if minecraft: < 26.1 {
        /*SingleQuadParticle.Layer.TERRAIN
         *///? } else {
        SingleQuadParticle.Layer.TRANSLUCENT_TERRAIN
        //? }
    );
    public static final ParticleLayer BACKFACE_TERRAIN = new ParticleLayer(
        ParticleInteractionsParticle.BACKFACE_TERRAIN_LAYER
    );

    public static ParticleLayer fromAppearance(ParticleAppearance appearance) {
        ParticleAppearance.TextureConfig config = appearance.textureConfig();
        if(config == null) return CUTOUT;

        Identity identity = new Identity(config.atlas().texturePath(), config.layer());
        if(EXISTING_LAYERS.containsKey(identity)) {
            return EXISTING_LAYERS.get(identity);
        }

        ParticleLayer layer = new ParticleLayer(createVanillaLayer(config));
        EXISTING_LAYERS.put(identity, layer);
        return layer;
    }

    private static SingleQuadParticle.Layer createVanillaLayer(ParticleAppearance.TextureConfig config) {
        return new SingleQuadParticle.Layer(config.layer().isTranslucent(), config.atlas().texturePath(), config.layer().pipeline());
    }

    private record Identity(Identifier atlasTexture, ParticleAppearance.LayerDefinition layerDefinition) {
    }
}
