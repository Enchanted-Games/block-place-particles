package games.enchanted.blockplaceparticles.mixin.particles;

import games.enchanted.blockplaceparticles.particle.spark.FlyingSpark;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FlyingSpark.class)
public abstract class FlyingSparkNeoForge extends TextureSheetParticle {
    protected FlyingSparkNeoForge(ClientLevel p_108323_, double p_108324_, double p_108325_, double p_108326_) {
        super(p_108323_, p_108324_, p_108325_, p_108326_);
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(float partialTicks) {
        return super.getRenderBoundingBox(partialTicks).inflate( Math.abs(new Vec3(this.x, this.y, this.z).distanceTo(new Vec3(this.xo, this.yo, this.zo)) / 1.5) );
    }
}
