package games.enchanted.blockplaceparticles.mixin.particles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import games.enchanted.blockplaceparticles.config.ConfigHandler;
import games.enchanted.blockplaceparticles.mixin.accessor.TextureAtlasAccessor;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import games.enchanted.blockplaceparticles.util.TextureHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(net.minecraft.client.particle.BreakingItemParticle.class)
public abstract class BreakingItemParticle extends TextureSheetParticle {
    @Unique private static final float MIN_UV = 0.0000001f;
    @Shadow @Final private float uo;
    @Shadow @Final private float vo;

    protected BreakingItemParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;getU(F)F"),
        method = "getU0"
    )
    protected float alignU0(TextureAtlasSprite instance, float u, Operation<Float> original) {
        if(!ConfigHandler.general_pixelConsistentTerrainParticles) {
            return original.call(instance, u);
        }
        TextureAtlas atlas = TextureHelpers.getTextureAtlas(this.sprite.atlasLocation());
        if(atlas == null) {
            return original.call(instance, u);
        }
        float spriteU = this.sprite.getU((this.uo + 1) * (this.quadSize * 2));
        return MathHelpers.ceilWithResolution(Math.max(MIN_UV, spriteU), ((TextureAtlasAccessor) atlas).getWidth());
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;getU(F)F"),
        method = "getU1"
    )
    protected float alignU1(TextureAtlasSprite instance, float u, Operation<Float> original) {
        if(!ConfigHandler.general_pixelConsistentTerrainParticles) {
            return original.call(instance, u);
        }
        TextureAtlas atlas = TextureHelpers.getTextureAtlas(this.sprite.atlasLocation());
        if(atlas == null) {
            return original.call(instance, u);
        }
        float spriteU = this.sprite.getU(this.uo * (this.quadSize * 2));
        return MathHelpers.ceilWithResolution(Math.max(MIN_UV, spriteU), ((TextureAtlasAccessor) atlas).getWidth());
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;getV(F)F"),
        method = "getV0"
    )
    protected float alignV0(TextureAtlasSprite instance, float v, Operation<Float> original) {
        if(!ConfigHandler.general_pixelConsistentTerrainParticles) {
            return original.call(instance, v);
        }
        TextureAtlas atlas = TextureHelpers.getTextureAtlas(this.sprite.atlasLocation());
        if(atlas == null) {
            return original.call(instance, v);
        }
        float spriteV = this.sprite.getV(this.vo * (this.quadSize * 2));
        return MathHelpers.ceilWithResolution(Math.max(MIN_UV, spriteV), ((TextureAtlasAccessor) atlas).getHeight());
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;getV(F)F"),
        method = "getV1"
    )
    protected float alignV1(TextureAtlasSprite instance, float v, Operation<Float> original) {
        if(!ConfigHandler.general_pixelConsistentTerrainParticles) {
            return original.call(instance, v);
        }
        TextureAtlas atlas = TextureHelpers.getTextureAtlas(this.sprite.atlasLocation());
        if(atlas == null) {
            return original.call(instance, v);
        }
        float spriteV = this.sprite.getV((this.vo + 1) * (this.quadSize * 2));
        return MathHelpers.ceilWithResolution(Math.max(MIN_UV, spriteV), ((TextureAtlasAccessor) atlas).getHeight());
    }
}
