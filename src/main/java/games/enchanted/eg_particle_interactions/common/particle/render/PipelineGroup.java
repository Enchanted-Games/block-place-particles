package games.enchanted.eg_particle_interactions.common.particle.render;

//? if minecraft: >= 26.3 {
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.renderer.oit.OitPipelineSet;
//? } else {

//? }
import org.jspecify.annotations.Nullable;

public record PipelineGroup(
    RenderPipeline pipeline
    //? if minecraft: >= 26.3 {
    , @Nullable OitPipelineSet oitSet
    //? }
) {
}
