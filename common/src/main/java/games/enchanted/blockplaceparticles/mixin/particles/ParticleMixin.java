package games.enchanted.blockplaceparticles.mixin.particles;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import games.enchanted.blockplaceparticles.config.ConfigHandler;
import games.enchanted.blockplaceparticles.duck.ParticleAccess;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.Mth;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Particle.class)
public class ParticleMixin implements ParticleAccess {
    @Shadow protected double y;
    @Shadow protected boolean hasPhysics;
    @Shadow private boolean stoppedByCollision;

    @Shadow @Final protected ClientLevel level;
    @Unique protected boolean block_place_particle$hasLanded;
    @Unique protected boolean block_place_particle$bypassMovementCollisionCheck = false;

    /**
     * Move particle upwards a tiny bit when it lands, hacky fix for MC-91873
     */
    @Inject(
        at = @At("TAIL"),
        method = "tick()V"
    )
    public void adjustParticleYWhenLanded(CallbackInfo ci) {
        if(!ConfigHandler.general_particleZFightingFix) return;
        if ( this.hasPhysics && this.stoppedByCollision && !this.block_place_particle$hasLanded ) {
            this.block_place_particle$hasLanded = true;
            this.y += Mth.randomBetween(this.level.random, 0.0001f, 0.0003f);
        }
    }

    /**
     * Adds a way for particles to bypass the collision check
     */
    @ModifyExpressionValue(
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/particle/Particle;stoppedByCollision:Z", opcode = Opcodes.GETFIELD, ordinal = 0),
        method = "move"
    )
    private boolean bypassMovementCollisionCheck(boolean original) {
        return !block_place_particle$bypassMovementCollisionCheck && original;
    }

    @Override
    public void setBypassMovementCollisionCheck(boolean newValue) {
        this.block_place_particle$bypassMovementCollisionCheck = newValue;
        if(!newValue) stoppedByCollision = false;
    }

    @Override
    public boolean getBypassMovementCollisionCheck() {
        return this.block_place_particle$bypassMovementCollisionCheck;
    }
}
