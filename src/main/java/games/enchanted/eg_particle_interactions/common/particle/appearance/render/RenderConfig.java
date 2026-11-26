package games.enchanted.eg_particle_interactions.common.particle.appearance.render;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.particle.render.PIRenderPipelines;
import games.enchanted.eg_particle_interactions.common.particle.render.PipelineGroup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.StringRepresentable;

public record RenderConfig(AlphaHandling alphaHandling, boolean backfaceCulling) {
    public static final RenderConfig DEFAULT = new RenderConfig(AlphaHandling.CUTOUT, true);

    public static final Codec<RenderConfig> CODEC = RecordCodecBuilder.create(i -> i
        .group(
            StringRepresentable.fromEnum(AlphaHandling::values).optionalFieldOf("alpha_handling", DEFAULT.alphaHandling).forGetter(RenderConfig::alphaHandling),
            Codec.BOOL.optionalFieldOf("backface_culling", DEFAULT.backfaceCulling).forGetter(RenderConfig::backfaceCulling)
        )
        .apply(
            i,
            RenderConfig::new
        )
    );

    public boolean isTranslucent() {
        return this.alphaHandling() == AlphaHandling.TRANSLUCENT;
    }

    public PipelineGroup toPipeline(boolean maskTexture) {
        if(this.alphaHandling() == AlphaHandling.CUTOUT) {
            if(this.backfaceCulling()) {
                if(maskTexture) {
                    return new PipelineGroup(PIRenderPipelines.MASK_CUTOUT_PARTICLE, null);
                } else {
                    return new PipelineGroup(RenderPipelines.OPAQUE_PARTICLE, null);
                }
            }
            if(maskTexture) {
                return new PipelineGroup(PIRenderPipelines.MASK_BACKFACE_CUTOUT_PARTICLE, null);
            } else {
                return new PipelineGroup(PIRenderPipelines.BACKFACE_CUTOUT_PARTICLE, null);
            }
        }

        if(this.backfaceCulling()) {
            if(maskTexture) {
                return new PipelineGroup(PIRenderPipelines.MASK_TRANSLUCENT_PARTICLE, PIRenderPipelines.MASK_OIT_PARTICLE);
            } else {
                return new PipelineGroup(RenderPipelines.TRANSLUCENT_PARTICLE, RenderPipelines.OIT_PARTICLE);
            }
        }
        if(maskTexture) {
            return new PipelineGroup(PIRenderPipelines.MASK_BACKFACE_TRANSLUCENT_PARTICLE, PIRenderPipelines.MASK_BACKFACE_OIT_PARTICLE);
        } else {
            return new PipelineGroup(PIRenderPipelines.BACKFACE_TRANSLUCENT_PARTICLE, PIRenderPipelines.BACKFACE_OIT_PARTICLE);
        }
    }
}
