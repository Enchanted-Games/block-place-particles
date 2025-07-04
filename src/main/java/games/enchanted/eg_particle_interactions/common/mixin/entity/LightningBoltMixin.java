package games.enchanted.eg_particle_interactions.common.mixin.entity;

import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningBolt.class)
public abstract class LightningBoltMixin extends Entity {
    public LightningBoltMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V", ordinal = 0),
        method = "tick"
    )
    private void eg_particle_interactions$spawnLightningImpactSparks(CallbackInfo ci) {
        if(!(this.level() instanceof ClientLevel clientLevel)) return;
        SpawnParticles.spawnLightningImpactSparks(clientLevel, this.getX(), this.getY(), this.getZ());
    }
}
