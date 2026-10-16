package games.enchanted.eg_particle_interactions.common.mixin.client.entity.projectile_particles;

import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile {
    @Shadow
    protected abstract boolean isInGround();

    public AbstractArrowMixin(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    @Inject(
        at = @At("RETURN"),
        method = "tick"
    )
    private void eg_particle_interactions$emitArrowParticles(CallbackInfo ci) {
        if(this.level() instanceof ClientLevel clientLevel) {
            SpawnParticles.spawnArrowTrail(clientLevel, (AbstractArrow) (Object) this, this.isInGround());
        }
    }
}
