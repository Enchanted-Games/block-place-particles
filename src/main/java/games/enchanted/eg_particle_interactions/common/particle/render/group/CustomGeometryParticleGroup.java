package games.enchanted.eg_particle_interactions.common.particle.render.group;

import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
import net.minecraft.world.phys.AABB;

//? if minecraft: >= 26.1 < 26.2 {
/*import games.enchanted.eg_particle_interactions.common.particle.render.state.mc26_1.CustomParticleGeometryRenderState;
*///? } else {
import games.enchanted.eg_particle_interactions.common.particle.render.state.mc26_2.CustomParticleGeometryRenderState;
//? }

public class CustomGeometryParticleGroup extends ParticleGroup<ParticleInteractionsParticle> {
    private final CustomParticleGeometryRenderState state = new CustomParticleGeometryRenderState();

    public CustomGeometryParticleGroup(ParticleEngine particleEngine) {
        super(particleEngine);
    }

    @Override
    public ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTicks) {
        for (ParticleInteractionsParticle particle : this.particles) {
            AABB bb = particle.getCullingBox(partialTicks);
            if (!frustum.isVisible(bb)) continue;
            try {
                particle.extract(this.state, camera, partialTicks);
            }
            catch (Throwable throwable) {
                CrashReport report = CrashReport.forThrowable(throwable, "[Particle Interactions] Rendering Particle");
                CrashReportCategory crashReportCategory = report.addCategory("Particle being extracted");
                crashReportCategory.setDetail("Particle", particle::toString);
                throw new ReportedException(report);
            }
        }
        return this.state;
    }
}