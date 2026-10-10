package games.enchanted.eg_particle_interactions.common.particle.render;

//? if minecraft: >= 26.3 {
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.renderer.oit.OitPipelineSet;
import org.jspecify.annotations.Nullable;
//? } else {
/*import com.mojang.blaze3d.pipeline.RenderPipeline;
*///? }

public record PipelineGroup(
    RenderPipeline pipeline
    //? if minecraft: >= 26.3 {
    , @Nullable OitPipelineSet oitSet
    //? }
) {
}
