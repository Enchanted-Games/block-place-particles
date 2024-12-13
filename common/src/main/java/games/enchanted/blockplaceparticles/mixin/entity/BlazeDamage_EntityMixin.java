package games.enchanted.blockplaceparticles.mixin.entity;

import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.entity.Entity.class)
public abstract class BlazeDamage_EntityMixin {
    @Shadow public abstract Level level();
    @Shadow public abstract double getRandomX(double scale);
    @Shadow public abstract double getRandomY();
    @Shadow public abstract double getRandomZ(double scale);

    @Inject(
        at = @At("TAIL"),
        method = "hurtClient"
    )
    private void spawnParticlesOnHurt(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if(!(this.level() instanceof ClientLevel)) return;
        if((Object) this instanceof Blaze) {
            SpawnParticles.spawnBlazeHurtParticles((ClientLevel) this.level(), this.getRandomX(0.6), this.getRandomY(), this.getRandomZ(0.6));
        }
    }
}
