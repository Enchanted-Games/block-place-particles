package games.enchanted.eg_particle_interactions.common.particle.appearance;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import games.enchanted.eg_particle_interactions.common.particle.render.ModRenderPipelines;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.StringRepresentable;

public enum LayerDefinition implements StringRepresentable {
    CUTOUT("cutout", false, false, RenderPipelines.OPAQUE_PARTICLE),
    TRANSLUCENT("translucent", true, false, RenderPipelines.TRANSLUCENT_PARTICLE),
    CUTOUT_BACKFACE("cutout_backface", false, true, ModRenderPipelines.BACKFACE_CUTOUT_PARTICLE),
    TRANSLUCENT_BACKFACE("translucent_backface", true, true, ModRenderPipelines.BACKFACE_TRANSLUCENT_PARTICLE);

    final String name;
    final boolean translucent;
    final boolean backface;
    final RenderPipeline pipeline;

    LayerDefinition(String name, boolean translucent, boolean backface, RenderPipeline pipeline) {
        this.name = name;
        this.translucent = translucent;
        this.backface = backface;
        this.pipeline = pipeline;
    }

    public boolean isTranslucent() {
        return this.translucent;
    }

    public boolean showBackface() {
        return this.backface;
    }

    public RenderPipeline pipeline() {
        return pipeline;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public static LayerDefinition fromVanillaSprite(TextureAtlasSprite sprite, boolean backface) {
        boolean hasTranslucent = sprite.transparency().hasTranslucent();
        if(backface) {
            return hasTranslucent ? TRANSLUCENT_BACKFACE : CUTOUT_BACKFACE;
        }
        return hasTranslucent ? TRANSLUCENT : CUTOUT;
    }
}
