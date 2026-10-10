package games.enchanted.eg_particle_interactions.common.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.config.categories.EntityOptions;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.emitter.rule.EmitterRuleSet;
import games.enchanted.eg_particle_interactions.common.particle_spawning.EmitterRuleSetIds;
import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bee.class)
public abstract class BeeMixin extends Animal {
    protected BeeMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Shadow
    public abstract boolean hasNectar();

    @Unique
    private boolean eg_particle_interactions$nectarLastTick = false;

    @Inject(
        at = @At("TAIL"),
        method = "readAdditionalSaveData"
    )
    private void eg_particle_interactions$setNectarLastTick(ValueInput input, CallbackInfo ci) {
        this.eg_particle_interactions$nectarLastTick = this.hasNectar();
    }

    @Inject(
        at = @At("TAIL"),
        method = "tick"
    )
    private void eg_particle_interactions$beeTick(CallbackInfo ci) {
        if(!(this.level() instanceof ClientLevel clientLevel)) return;
        if(this.eg_particle_interactions$nectarLastTick != this.hasNectar() && this.hasNectar()) {
            SpawnParticles.spawnBeePollenPuff(clientLevel, (Bee) (Object) this);
        }
        this.eg_particle_interactions$nectarLastTick = this.hasNectar();
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"),
        method = "spawnFluidParticle"
    )
    private void eg_particle_interactions$replaceNectarParticleConditionally(
        Level instance,
        ParticleOptions particle,
        double x,
        double y,
        double z,
        double xd,
        double yd,
        double zd,
        Operation<Void> original
    ) {
        if(!(instance instanceof ClientLevel clientLevel)) {
            original.call(instance, particle, x, y, z, xd, yd, zd);
            return;
        }

        if(!EntityOptions.BEE_NECTAR_REPLACE_VANILLA_PARTICLES.getValue()) {
            original.call(instance, particle, x, y, z, xd, yd, zd);
            return;
        }

        ParticleContext context = ParticleContext.entity(clientLevel, (Bee) (Object) this);
        EmitterRuleSet emitters = EmitterRuleSetIds.BEE_FALLING_NECTAR.get();
        emitters.getEmitter(context).spawnParticle(
            context,
            x,
            y,
            z,
            xd,
            yd,
            zd
        );
    }
}
