package games.enchanted.eg_particle_interactions.common.particle.render.layer;

import games.enchanted.eg_particle_interactions.common.compat.iris.IrisApiUser;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.appearance.render.RenderConfig;
import games.enchanted.eg_particle_interactions.common.particle.appearance.texture.TextureConfig;
import games.enchanted.eg_particle_interactions.common.particle.render.PipelineGroup;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public record ParticleLayer(
    boolean translucent,
    Identifier atlasTexture,
    @Nullable Identifier maskAtlasTexture,
    PipelineGroup pipeline,
    PipelineGroup maskPipeline
) {
    private static final Map<Identity, ParticleLayer> EXISTING_LAYERS = new HashMap<>();

    public static ParticleLayer fromAppearance(ParticleContext context, ParticleAppearance appearance) {
        TextureConfig texture = appearance.textureConfig();
        TextureConfig maskTexture = appearance.maskConfig();
        RenderConfig renderConfig = appearance.renderConfig();

        Identity identity = new Identity(
            texture.getAtlas(context).texturePath(),
            maskTexture.getAtlas(context).texturePath(),
            renderConfig
        );
        if(EXISTING_LAYERS.containsKey(identity)) {
            return EXISTING_LAYERS.get(identity);
        }

        boolean hasMask = appearance.maskConfig().containsValidMaskSprites();
        ParticleLayer layer = new ParticleLayer(
            renderConfig.isTranslucent(),
            texture.getAtlas(context).texturePath(),
            hasMask ? maskTexture.getAtlas(context).texturePath() : null,
            renderConfig.toPipeline(false),
            renderConfig.toPipeline(true)
        );
        EXISTING_LAYERS.put(identity, layer);
        return layer;
    }

    public boolean renderWithMask() {
        return this.maskAtlasTexture != null && !IrisApiUser.INSTANCE.shaderpackActive();
    }

    private record Identity(Identifier atlasTexture, @Nullable Identifier maskAtlasTexture, RenderConfig config) {
    }
}
