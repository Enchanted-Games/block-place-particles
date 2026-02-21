package games.enchanted.eg_particle_interactions.common.mixin.client.particles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.duck.ParticleAccess;
import games.enchanted.eg_particle_interactions.common.particle.overrides.BlockParticleOverrides;
import games.enchanted.eg_particle_interactions.common.registry.BlockOrTagLocation;
import games.enchanted.eg_particle_interactions.common.registry.RegistryHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FireflyParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if minecraft: <= 1.21.8 {
/*import net.minecraft.client.particle.TextureSheetParticle;
 *///?} else {
import net.minecraft.client.particle.SingleQuadParticle;
//?}

import java.util.List;

@Mixin(FireflyParticle.class)
public abstract class FireflyParticleMixin
    //? if minecraft: <= 1.21.8 {
    /*extends TextureSheetParticle
     *///?} else {
    extends SingleQuadParticle
//?}
{
    //? if minecraft: <=1.21.8 {
    /*protected FireflyParticleMixin(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }
    *///?} else {
    protected FireflyParticleMixin(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
    }
    //?}

    //? if minecraft: <=1.21.8 {
    /*@Inject(
        at = @At("TAIL"),
        method = "<init>"
    )
    private void block_place_particle$makeFirefliesNotGetStuckOnStuff(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, CallbackInfo ci) {
    *///?} else {
    @Inject(
        at = @At("TAIL"),
        method = "<init>"
    )
    private void block_place_particle$makeFirefliesNotGetStuckOnStuff(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite textureAtlasSprite, CallbackInfo ci) {
    //?}
        if(!GeneralOptions.FIREFLY_FIXES.getValue()) return;
        ((ParticleAccess) this).eg_particle_interactions$setBypassMovementCollisionCheck(true);
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"),
        method = "tick"
    )
    public boolean block_place_particle$makeFirefliesNotDieInFireflyBushes(BlockState state, Operation<Boolean> original) {
        if(!GeneralOptions.FIREFLY_FIXES.getValue()) {
            return original.call(state);
        }
        // TODO: fix firefly override blocks
//        List<BlockOrTagLocation> fireflyOverrideBlocks = BlockParticleOverrides.FIREFLY.getSupportedBlocksAndTags();
//        List<BlockOrTagLocation> grassBladeOverrideBlocks = BlockParticleOverrides.GRASS_BLADE.getSupportedBlocksAndTags();
//        if(fireflyOverrideBlocks == null || grassBladeOverrideBlocks == null) {
//            return original.call(state);
//        }
        if(state.is(Blocks.FIREFLY_BUSH)) {
            return original.call(Blocks.AIR.defaultBlockState());
        }
        return original.call(state);
    }
}
