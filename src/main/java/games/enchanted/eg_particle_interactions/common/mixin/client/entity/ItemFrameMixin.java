package games.enchanted.eg_particle_interactions.common.mixin.client.entity;

import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrame.class)
public abstract class ItemFrameMixin extends HangingEntity {
    @Shadow protected abstract boolean shouldDamageDropItem(DamageSource damageSource);

    @Shadow public abstract ItemStack getItem();

    protected ItemFrameMixin(EntityType<? extends HangingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        at = @At("RETURN"),
        method = "hurtClient"
    )
    private void eg_particle_interactions$spawnParticlesOnDamage(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (!(this.level() instanceof ClientLevel clientLevel)) return;

        boolean wasHurtSuccessfully = cir.getReturnValue();

        if (!wasHurtSuccessfully) return;
        SpawnParticles.spawnItemFrameInteractionParticles(
            clientLevel,
            this.getX(),
            this.getY(),
            this.getZ(),
            this.getBoundingBox(),
            this.getDirection(),
            this.shouldDamageDropItem(damageSource) ? SpawnParticles.ItemFrameParticleOrigin.HELD_ITEM_REMOVED : SpawnParticles.ItemFrameParticleOrigin.FRAME_KILLED,
            (ItemFrame) (Object) this
        );
    }

    @Inject(
        at = @At("RETURN"),
        method = "interact"
    )
    private void eg_particle_interactions$spawnParticlesOnInteract(Player player, InteractionHand hand, Vec3 location, CallbackInfoReturnable<InteractionResult> cir) {
        if (!(this.level() instanceof ClientLevel clientLevel)) return;

        InteractionResult interactionResult = cir.getReturnValue();
        if(interactionResult != InteractionResult.SUCCESS) return;

        SpawnParticles.spawnItemFrameInteractionParticles(
            clientLevel,
            this.getX(),
            this.getY(),
            this.getZ(),
            this.getBoundingBox(),
            this.getDirection(),
            this.getItem().isEmpty() ? SpawnParticles.ItemFrameParticleOrigin.ITEM_PLACED : SpawnParticles.ItemFrameParticleOrigin.ITEM_ROTATED,
            (ItemFrame) (Object) this
        );
    }
}
