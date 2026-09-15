package games.enchanted.eg_particle_interactions.common.particle.appearance;

import games.enchanted.eg_particle_interactions.common.particle.render.ModRenderPipelines;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.StringRepresentable;

//? if minecraft: <= 26.2 {
/*import com.mojang.blaze3d.pipeline.RenderPipeline;
 *///? } else {
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.renderer.oit.OitPipelineSet;
import org.jspecify.annotations.Nullable;
//? }

public enum LayerDefinition implements StringRepresentable {
    CUTOUT(
        "cutout",
        false,
        false,
        RenderPipelines.OPAQUE_PARTICLE
    ),
    TRANSLUCENT(
        "translucent",
        true,
        false,
        RenderPipelines.TRANSLUCENT_PARTICLE
        //? if minecraft: >= 26.3 {
        , RenderPipelines.OIT_PARTICLE
        //? }
    ),
    CUTOUT_BACKFACE(
        "cutout_backface",
        false,
        true,
        ModRenderPipelines.BACKFACE_CUTOUT_PARTICLE
    ),
    TRANSLUCENT_BACKFACE(
        "translucent_backface",
        true,
        true,
        ModRenderPipelines.BACKFACE_TRANSLUCENT_PARTICLE
        //? if minecraft: >= 26.3 {
        , ModRenderPipelines.BACKFACE_OIT_PARTICLE
        //? }
    );

    final String name;
    final boolean translucent;
    final boolean backface;
    final RenderPipeline pipeline;
    //? if minecraft: >= 26.3 {
    final @Nullable OitPipelineSet oitPipelineSet;
    //? }

    LayerDefinition(String name, boolean translucent, boolean backface, RenderPipeline pipeline) {
        this.name = name;
        this.translucent = translucent;
        this.backface = backface;
        this.pipeline = pipeline;
        //? if minecraft: >= 26.3 {
        this.oitPipelineSet = null;
        //? }
    }

    //? if minecraft: >= 26.3 {
    LayerDefinition(String name, boolean translucent, boolean backface, RenderPipeline pipeline, @Nullable OitPipelineSet oitPipelineSet) {
        this.name = name;
        this.translucent = translucent;
        this.backface = backface;
        this.pipeline = pipeline;
        this.oitPipelineSet = oitPipelineSet;
    }
    //? }

    public boolean isTranslucent() {
        return this.translucent;
    }

    public boolean showBackface() {
        return this.backface;
    }

    public RenderPipeline pipeline() {
        return pipeline;
    }

    //? if minecraft: >= 26.3 {
    public @Nullable OitPipelineSet oitPipelineSet() {
        return oitPipelineSet;
    }
    //? }

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
