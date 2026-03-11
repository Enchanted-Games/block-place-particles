package games.enchanted.eg_particle_interactions.common.mixin.client.particles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.particle.SingleQuadParticle;

@Mixin(net.minecraft.client.particle.TerrainParticle.class)
public abstract class TerrainParticle extends SingleQuadParticle {
    @Unique private static final float MIN_UV = 0.0000001f;
    @Unique private float eg_particle_interactions$quadSizePixels = 1;

    @Mutable @Shadow @Final private float uo;
    @Mutable @Shadow @Final private float vo;

    protected TerrainParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
    }

    @Unique
    private void eg_particle_interactions$recalculatePixelQuadSizes() {
        this.eg_particle_interactions$quadSizePixels =
            this.quadSize <= 0.04 ?
                (float) 1 / this.sprite.contents().width() :
                MathHelpers.ceilWithResolution(this.quadSize + 0.0625, this.sprite.contents().width());

        if(this.uo + this.eg_particle_interactions$quadSizePixels > 1) this.uo = 1 - this.eg_particle_interactions$quadSizePixels;
        if(this.vo + this.eg_particle_interactions$quadSizePixels > 1) this.vo = 1 - this.eg_particle_interactions$quadSizePixels;
    }

    @Inject(
        at = @At("TAIL"),
        method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V"
    )
    protected void eg_particle_interactions$init(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState state, BlockPos pos, CallbackInfo ci) {
        if(GeneralOptions.PIXEL_CONSISTENT_TERRAIN_PARTICLES.getValue()) {
            this.uo = (float) MathHelpers.randomBetween(0, this.sprite.contents().width()) / this.sprite.contents().width();
            this.vo = (float) MathHelpers.randomBetween(0, this.sprite.contents().height()) / this.sprite.contents().height();

            this.eg_particle_interactions$recalculatePixelQuadSizes();
        }
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;getU(F)F"),
        method = "getU0"
    )
    protected float eg_particle_interactions$alignU0(TextureAtlasSprite instance, float u, Operation<Float> original) {
        if(!GeneralOptions.PIXEL_CONSISTENT_TERRAIN_PARTICLES.getValue()) {
            return original.call(instance, u);
        }
        this.eg_particle_interactions$recalculatePixelQuadSizes();
        return this.sprite.getU(this.uo) + MIN_UV;
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;getU(F)F"),
        method = "getU1"
    )
    protected float eg_particle_interactions$alignU1(TextureAtlasSprite instance, float u, Operation<Float> original) {
        if(!GeneralOptions.PIXEL_CONSISTENT_TERRAIN_PARTICLES.getValue()) {
            return original.call(instance, u);
        }
        this.eg_particle_interactions$recalculatePixelQuadSizes();
        return this.sprite.getU(this.uo + this.eg_particle_interactions$quadSizePixels) + MIN_UV;
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;getV(F)F"),
        method = "getV0"
    )
    protected float eg_particle_interactions$alignV0(TextureAtlasSprite instance, float v, Operation<Float> original) {
        if(!GeneralOptions.PIXEL_CONSISTENT_TERRAIN_PARTICLES.getValue()) {
            return original.call(instance, v);
        }
        this.eg_particle_interactions$recalculatePixelQuadSizes();
        return this.sprite.getV(this.vo) + MIN_UV;
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;getV(F)F"),
        method = "getV1"
    )
    protected float eg_particle_interactions$alignV1(TextureAtlasSprite instance, float v, Operation<Float> original) {
        if(!GeneralOptions.PIXEL_CONSISTENT_TERRAIN_PARTICLES.getValue()) {
            return original.call(instance, v);
        }
        this.eg_particle_interactions$recalculatePixelQuadSizes();
        return this.sprite.getV(this.vo + this.eg_particle_interactions$quadSizePixels) + MIN_UV;
    }
}
