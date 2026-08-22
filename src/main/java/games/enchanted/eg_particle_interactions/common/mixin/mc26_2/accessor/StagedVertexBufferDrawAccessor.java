//? if minecraft: >= 26.2 {
package games.enchanted.eg_particle_interactions.common.mixin.mc26_2.accessor;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.StagedVertexBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StagedVertexBuffer.Draw.class)
public interface StagedVertexBufferDrawAccessor {
    @Accessor("primitiveTopology")
    PrimitiveTopology eg_particle_interactions$primitiveTopology();

    @Accessor("format")
    VertexFormat eg_particle_interactions$format();
}
//? }
