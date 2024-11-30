package games.enchanted.blockplaceparticles.mixin;

import games.enchanted.blockplaceparticles.config.ConfigHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Particle.class)
public abstract class ParticleMixin {
    @Shadow protected double y;
    @Shadow protected boolean hasPhysics;
    @Shadow private boolean stoppedByCollision;

    @Shadow @Final protected ClientLevel level;
    @Unique protected boolean block_place_particle$hasLanded;

    /**
     * Move particle upwards a tiny bit when it lands, hacky fix for MC-91873
     */
    @Inject(at = @At("TAIL"), method = "tick()V")
    public void adjustParticleYWhenLanded(CallbackInfo ci) {
        if(!ConfigHandler.general_particleZFightingFix) return;
        if ( this.hasPhysics && this.stoppedByCollision && !this.block_place_particle$hasLanded ) {
            this.block_place_particle$hasLanded = true;
            this.y += Mth.randomBetween(this.level.random, 0.0001f, 0.0003f);
        }
    }
}
