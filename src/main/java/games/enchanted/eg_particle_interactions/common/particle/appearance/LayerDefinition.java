package games.enchanted.eg_particle_interactions.common.particle.appearance;

import games.enchanted.eg_particle_interactions.common.particle.render.PIRenderPipelines;
import games.enchanted.eg_particle_interactions.common.particle.render.PipelineGroup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.StringRepresentable;

//? if minecraft: <= 26.2 {
/*import com.mojang.blaze3d.pipeline.RenderPipeline;
 *///? } else {

//? }

public enum LayerDefinition implements StringRepresentable {
    CUTOUT(
        "cutout",
        false,
        false,
        new PipelineGroup(
            RenderPipelines.OPAQUE_PARTICLE
            //? if minecraft: >= 26.3 {
            , null
            //? }
        ),
        new PipelineGroup(
            PIRenderPipelines.MASK_CUTOUT_PARTICLE
            //? if minecraft: >= 26.3 {
            , null
            //? }
        )
    ),
    TRANSLUCENT(
        "translucent",
        true,
        false,
        new PipelineGroup(
            RenderPipelines.TRANSLUCENT_PARTICLE
            //? if minecraft: >= 26.3 {
            , RenderPipelines.OIT_PARTICLE
            //? }
        ),
        new PipelineGroup(
            PIRenderPipelines.MASK_TRANSLUCENT_PARTICLE
            //? if minecraft: >= 26.3 {
            , PIRenderPipelines.MASK_OIT_PARTICLE
            //? }
        )
    ),
    CUTOUT_BACKFACE(
        "cutout_backface",
        false,
        true,
        new PipelineGroup(
            PIRenderPipelines.BACKFACE_CUTOUT_PARTICLE
            //? if minecraft: >= 26.3 {
            , null
            //? }
        ),
        new PipelineGroup(
            PIRenderPipelines.MASK_BACKFACE_CUTOUT_PARTICLE
            //? if minecraft: >= 26.3 {
            , null
            //? }
        )
    ),
    TRANSLUCENT_BACKFACE(
        "translucent_backface",
        true,
        true,
        new PipelineGroup(
            PIRenderPipelines.BACKFACE_TRANSLUCENT_PARTICLE
            //? if minecraft: >= 26.3 {
            , PIRenderPipelines.BACKFACE_OIT_PARTICLE
            //? }
        ),
        new PipelineGroup(
            PIRenderPipelines.MASK_BACKFACE_TRANSLUCENT_PARTICLE
            //? if minecraft: >= 26.3 {
            , PIRenderPipelines.MASK_BACKFACE_OIT_PARTICLE
            //? }
        )
    );

    final String name;
    final boolean translucent;
    final boolean backface;
    final PipelineGroup pipeline;
    final PipelineGroup maskPipeline;

    LayerDefinition(String name, boolean translucent, boolean backface, PipelineGroup pipeline, PipelineGroup maskPipeline) {
        this.name = name;
        this.translucent = translucent;
        this.backface = backface;
        this.pipeline = pipeline;
        this.maskPipeline = maskPipeline;
    }

    public boolean isTranslucent() {
        return this.translucent;
    }

    public boolean showBackface() {
        return this.backface;
    }

    public PipelineGroup pipeline() {
        return this.pipeline;
    }

    public PipelineGroup maskPipeline() {
        return this.maskPipeline;
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
