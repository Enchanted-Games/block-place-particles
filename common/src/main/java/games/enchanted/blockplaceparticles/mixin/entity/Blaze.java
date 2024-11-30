package games.enchanted.blockplaceparticles.mixin.entity;

import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.entity.monster.Blaze.class)
public abstract class Blaze extends Monster {
    protected Blaze(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        at = @At("HEAD"),
        method = "aiStep"
    )
    private void spawnExtraParticlesOnAiStep(CallbackInfo ci) {
        if(!(this.level() instanceof ClientLevel)) return;
        SpawnParticles.spawnBlazeAmbientParticles((ClientLevel) this.level(), this.getRandomX(0.6), this.getRandomY(), this.getRandomZ(0.6));
    }
}
