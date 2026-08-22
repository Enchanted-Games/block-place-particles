package games.enchanted.eg_particle_interactions.common.duck;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.StagedVertexBuffer;

public interface StagedVertexBufferAdditions {
    VertexConsumer eg_particle_interactions$getCustomBuffer(final StagedVertexBuffer.Draw draw);
}
