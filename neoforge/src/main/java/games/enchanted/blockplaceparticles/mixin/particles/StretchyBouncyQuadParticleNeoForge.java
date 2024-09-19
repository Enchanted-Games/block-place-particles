package games.enchanted.blockplaceparticles.mixin.particles;

import games.enchanted.blockplaceparticles.particle.StretchyBouncyQuadParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StretchyBouncyQuadParticle.class)
public abstract class StretchyBouncyQuadParticleNeoForge extends TextureSheetParticle {
    protected StretchyBouncyQuadParticleNeoForge(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(float partialTicks) {
        // expand the render box by the size of the particle and move it to the middle of the current pos and previous pos
        // this is neoforge specific which is why its in a mixin
        double diffX = this.x - this.xo;
        double diffY = this.y - this.yo;
        double diffZ = this.z - this.zo;
        return super.getRenderBoundingBox(partialTicks).move(-diffX / 2, -diffY / 2, -diffZ / 2).inflate( Math.abs(new Vec3(this.x, this.y, this.z).distanceTo(new Vec3(this.xo, this.yo, this.zo)) / 2 ));
    }
}
