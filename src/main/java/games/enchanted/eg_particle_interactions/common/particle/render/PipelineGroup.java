package games.enchanted.eg_particle_interactions.common.particle.render;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.renderer.oit.OitPipelineSet;
import org.jspecify.annotations.Nullable;

public record PipelineGroup(RenderPipeline pipeline, @Nullable OitPipelineSet oitSet) {
}
