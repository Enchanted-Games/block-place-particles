package games.enchanted.eg_particle_interactions.common.particle.render.geometry;

import net.minecraft.client.particle.SingleQuadParticle;

public interface QuadConsumerProvider {
    QuadConsumer getConsumer(SingleQuadParticle.Layer layer);
}
