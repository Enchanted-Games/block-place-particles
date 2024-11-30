package games.enchanted.blockplaceparticles.mixin.particles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.blockplaceparticles.config.ConfigHandler;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.particle.TerrainParticle.class)
public abstract class TerrainParticle extends TextureSheetParticle {
    @Unique private static final float MIN_UV = 0.0000001f;
    @Unique private float block_place_particle$quadSizePixels = 1;

    @Mutable @Shadow @Final private float uo;
    @Mutable @Shadow @Final private float vo;

    protected TerrainParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Unique
    private void block_place_particle$recalculatePixelQuadSizes() {
        this.block_place_particle$quadSizePixels =
            this.quadSize <= 0.04 ?
                (float) 1 / this.sprite.contents().width() :
                MathHelpers.ceilWithResolution(this.quadSize + 0.0625, this.sprite.contents().width());

        if(this.uo + this.block_place_particle$quadSizePixels > 1) this.uo = 1 - this.block_place_particle$quadSizePixels;
        if(this.vo + this.block_place_particle$quadSizePixels > 1) this.vo = 1 - this.block_place_particle$quadSizePixels;
    }

    @Inject(
        at = @At("TAIL"),
        method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V"
    )
    protected void terrainParticleInit(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState state, BlockPos pos, CallbackInfo ci) {
        if(ConfigHandler.general_pixelConsistentTerrainParticles) {
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
        if(!ConfigHandler.general_pixelConsistentTerrainParticles) {
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
        if(!ConfigHandler.general_pixelConsistentTerrainParticles) {
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
        if(!ConfigHandler.general_pixelConsistentTerrainParticles) {
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
        if(!ConfigHandler.general_pixelConsistentTerrainParticles) {
            return original.call(instance, v);
        }
        this.block_place_particle$recalculatePixelQuadSizes();
        return this.sprite.getV(this.vo + this.block_place_particle$quadSizePixels) + MIN_UV;
    }
}
