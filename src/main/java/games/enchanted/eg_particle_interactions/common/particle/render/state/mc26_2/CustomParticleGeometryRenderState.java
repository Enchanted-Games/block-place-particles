//? if minecraft: >= 26.2 {
package games.enchanted.eg_particle_interactions.common.particle.render.state.mc26_2;

import com.mojang.blaze3d.vertex.VertexConsumer;
import games.enchanted.eg_particle_interactions.common.duck.CustomSubmits;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.QuadConsumer;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.QuadConsumerProvider;
import games.enchanted.eg_particle_interactions.common.particle.render.geometry.mc26_2.CustomParticleGeometryQuadConsumer;
import games.enchanted.eg_particle_interactions.common.particle.render.state.QuadStorage;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CustomParticleGeometryRenderState implements ParticleGroupRenderState, QuadConsumerProvider {
    private final Map<SingleQuadParticle.Layer, QuadStorage> quadStoragePerLayer = new HashMap<>();
    private int vertexAmount = 0;

    @Override
    public void clear() {
        this.quadStoragePerLayer.values().forEach(QuadStorage::clear);
        this.vertexAmount = 0;
    }

    @Override
    public void submit(SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (!this.isEmpty()) {
            ((CustomSubmits) submitNodeCollector).eg_particle_interactions$submitCustomGeometryParticles(this);
        }
    }

    public boolean isEmpty() {
        return this.vertexAmount <= 0;
    }

    public void buildLayer(SingleQuadParticle.Layer layer, VertexConsumer buffer) {
        QuadStorage storage = this.quadStoragePerLayer.get(layer);
        if (storage != null) {
            storage.forEachVertex((x, y, z, u, v, packedLight, argb) -> buffer.addVertex(x, y, z).setUv(u, v).setColor(argb).setLight(packedLight));
        }
    }

    public Set<SingleQuadParticle.Layer> layers() {
        return this.quadStoragePerLayer.keySet();
    }

    public void startQuad(SingleQuadParticle.Layer layer) {
        this.quadStoragePerLayer.computeIfAbsent(layer, (l) -> new QuadStorage()).startQuad();
    }
    public void finishQuad(SingleQuadParticle.Layer layer) {
        this.quadStoragePerLayer.computeIfAbsent(layer, (l) -> new QuadStorage()).finishQuad();
    }

    public void addVertex(SingleQuadParticle.Layer layer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float scale, float u, float v, float maskU, float maskV, int packedLight, float rCol, float gCol, float bCol, float alpha) {
        this.quadStoragePerLayer.computeIfAbsent(layer, (l) -> new QuadStorage()).addVertex(
            quaternion,
            x,
            y,
            z,
            xOffset,
            yOffset,
            scale,
            u,
            v,
            packedLight,
            rCol,
            gCol,
            bCol,
            alpha
        );
        this.vertexAmount++;
    }

    @Override
    public QuadConsumer getConsumer(SingleQuadParticle.Layer layer) {
        return new CustomParticleGeometryQuadConsumer(this, layer);
    }
}
//? }