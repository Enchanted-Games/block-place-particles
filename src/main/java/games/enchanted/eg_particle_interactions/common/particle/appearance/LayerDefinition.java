package games.enchanted.eg_particle_interactions.common.particle.appearance;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import games.enchanted.eg_particle_interactions.common.particle.render.ModRenderPipelines;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.StringRepresentable;

public enum LayerDefinition implements StringRepresentable {
    CUTOUT("cutout", false, RenderPipelines.OPAQUE_PARTICLE),
    TRANSLUCENT("translucent", true, RenderPipelines.TRANSLUCENT_PARTICLE),
    CUTOUT_BACKFACE("cutout_backface", false, ModRenderPipelines.BACKFACE_CUTOUT_PARTICLE),
    TRANSLUCENT_BACKFACE("translucent_backface", true, ModRenderPipelines.BACKFACE_TRANSLUCENT_PARTICLE);

    final String name;
    final boolean translucent;
    final RenderPipeline pipeline;

    LayerDefinition(String name, boolean translucent, RenderPipeline pipeline) {
        this.name = name;
        this.translucent = translucent;
        this.pipeline = pipeline;
    }

    public boolean isTranslucent() {
        return translucent;
    }

    public RenderPipeline pipeline() {
        return pipeline;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public static LayerDefinition fromVanillaSprite(TextureAtlasSprite sprite) {
        return sprite.transparency().hasTranslucent() ? TRANSLUCENT : CUTOUT;
    }
}
