package games.enchanted.eg_particle_interactions.common.particle_group;

//? if minecraft: > 1.21.8 {
import games.enchanted.eg_particle_interactions.common.particle.compat.CustomGeometryParticle;
import games.enchanted.eg_particle_interactions.common.rendering.state.CustomParticleGeometryRenderState;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.ParticleGroupRenderState;
import org.jetbrains.annotations.NotNull;

public class CustomGeometryParticleGroup extends ParticleGroup<CustomGeometryParticle> {
    private final CustomParticleGeometryRenderState state = new CustomParticleGeometryRenderState();

    public CustomGeometryParticleGroup(ParticleEngine particleEngine) {
        super(particleEngine);
    }

    @Override
    public @NotNull ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTicks) {
        for (CustomGeometryParticle particle : this.particles) {
            if (!frustum.isVisible(particle.getCullingBox(partialTicks))) continue;
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
//?}