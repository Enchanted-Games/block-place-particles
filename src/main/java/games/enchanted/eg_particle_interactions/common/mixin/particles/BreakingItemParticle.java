package games.enchanted.eg_particle_interactions.common.mixin.particles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if minecraft: <= 1.21.8 {
/*import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.item.ItemStackRenderState;
 *///?} else {
import net.minecraft.client.particle.SingleQuadParticle;
//?}

@Mixin(net.minecraft.client.particle.BreakingItemParticle.class)
public abstract class BreakingItemParticle
    //? if minecraft: <= 1.21.8 {
    /*extends TextureSheetParticle
     *///?} else {
    extends SingleQuadParticle
    //?}
{
    @Unique private static final float MIN_UV = 0.0000001f;
    @Unique private float block_place_particle$quadSizePixels = 1;

    @Mutable
    @Shadow @Final private float uo;
    @Mutable @Shadow @Final private float vo;

    //? if minecraft: <=1.21.8 {
    /*protected BreakingItemParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }
    *///?} else {
    protected BreakingItemParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
    }
    //?}

    @Unique
    private void block_place_particle$recalculatePixelQuadSizes() {
        this.block_place_particle$quadSizePixels =
            this.quadSize <= 0.04 ?
                (float) 1 / this.sprite.contents().width() :
                MathHelpers.ceilWithResolution(this.quadSize + 0.0625, this.sprite.contents().width());

        if(this.uo + this.block_place_particle$quadSizePixels > 1) this.uo = 1 - this.block_place_particle$quadSizePixels;
        if(this.vo + this.block_place_particle$quadSizePixels > 1) this.vo = 1 - this.block_place_particle$quadSizePixels;
    }

    //? if minecraft: <= 1.21.8 {
    /*@Inject(
        at = @At("TAIL"),
        method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/client/renderer/item/ItemStackRenderState;)V"
    )
    protected void block_place_particle$terrainParticleInit(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ItemStackRenderState renderState, CallbackInfo ci) {
    *///?} else {
    @Inject(
        at = @At("TAIL"),
        method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDLnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V"
    )
    protected void block_place_particle$terrainParticleInit(ClientLevel level, double x, double y, double z, TextureAtlasSprite textureAtlasSprite, CallbackInfo ci) {
    //?}
        if(GeneralOptions.PIXEL_CONSISTENT_TERRAIN_PARTICLES.getValue()) {
            this.uo = (float) MathHelpers.randomBetween(0, this.sprite.contents().width()) / this.sprite.contents().width();
            this.vo = (float) MathHelpers.randomBetween(0, this.sprite.contents().height()) / this.sprite.contents().height();

            this.block_place_particle$recalculatePixelQuadSizes();
        }
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;getU(F)F"),
        method = "getU0"
    )
    protected float alignU0(TextureAtlasSprite instance, float u, Operation<Float> original) {
        if(!GeneralOptions.PIXEL_CONSISTENT_TERRAIN_PARTICLES.getValue()) {
            return original.call(instance, u);
        }
        this.block_place_particle$recalculatePixelQuadSizes();
        return this.sprite.getU(this.uo) + MIN_UV;
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;getU(F)F"),
        method = "getU1"
    )
    protected float alignU1(TextureAtlasSprite instance, float u, Operation<Float> original) {
        if(!GeneralOptions.PIXEL_CONSISTENT_TERRAIN_PARTICLES.getValue()) {
            return original.call(instance, u);
        }
        this.block_place_particle$recalculatePixelQuadSizes();
        return this.sprite.getU(this.uo + this.block_place_particle$quadSizePixels) + MIN_UV;
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;getV(F)F"),
        method = "getV0"
    )
    protected float alignV0(TextureAtlasSprite instance, float v, Operation<Float> original) {
        if(!GeneralOptions.PIXEL_CONSISTENT_TERRAIN_PARTICLES.getValue()) {
            return original.call(instance, v);
        }
        this.block_place_particle$recalculatePixelQuadSizes();
        return this.sprite.getV(this.vo) + MIN_UV;
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;getV(F)F"),
        method = "getV1"
    )
    protected float alignV1(TextureAtlasSprite instance, float v, Operation<Float> original) {
        if(!GeneralOptions.PIXEL_CONSISTENT_TERRAIN_PARTICLES.getValue()) {
            return original.call(instance, v);
        }
        this.block_place_particle$recalculatePixelQuadSizes();
        return this.sprite.getV(this.vo + this.block_place_particle$quadSizePixels) + MIN_UV;
    }
}
