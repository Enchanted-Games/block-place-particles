package games.enchanted.blockplaceparticles.mixin.entity;

import games.enchanted.blockplaceparticles.config.ConfigHandler;
import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.entity.item.FallingBlockEntity.class)
public abstract class FallingBlockEntity extends Entity {
    @Shadow public abstract BlockState getBlockState();

    public FallingBlockEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        at = @At("HEAD"),
        method = "tick()V"
    )
    public void tick(CallbackInfo ci) {
        if(!(this.level() instanceof ClientLevel clientLevel)) return;

        float particleChance = Math.clamp((float) this.getDeltaMovement().length(), 0f, 0.98f);
        if(clientLevel.random.nextFloat() < particleChance) {
            SpawnParticles.spawnFallingBlockRandomFallParticles(clientLevel, this.getBlockState(), this.getX(), this.getY(), this.getZ(), this.getDeltaMovement());
        }

    }
}
