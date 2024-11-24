package games.enchanted.blockplaceparticles.mixin.particles;

import games.enchanted.blockplaceparticles.config.ConfigHandler;
import games.enchanted.blockplaceparticles.mixin.accessor.TextureAtlasAccessor;
import games.enchanted.blockplaceparticles.util.MathHelpers;
import games.enchanted.blockplaceparticles.util.TextureHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.client.particle.TerrainParticle.class)
public abstract class TerrainParticle extends TextureSheetParticle {
    @Shadow @Final private float uo;
    @Shadow @Final private float vo;

    protected TerrainParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Inject(
        at = @At("HEAD"),
        method = "getU0",
        cancellable = true
    )
    protected void getU0(CallbackInfoReturnable<Float> cir) {
        if(!ConfigHandler.general_pixelConsistentTerrainParticles) {
            return;
        }
        TextureAtlas atlas = TextureHelpers.getTextureAtlas(this.sprite.atlasLocation());
        if(atlas == null) {
            return;
        }
        float spriteU = this.sprite.getU((this.uo + 1) * (this.quadSize * 2));
        cir.setReturnValue(MathHelpers.ceilWithResolution(Math.max(0.001f, spriteU), ((TextureAtlasAccessor) atlas).getWidth()));
    }

    @Inject(
        at = @At("HEAD"),
        method = "getU1",
        cancellable = true
    )
    protected void getU1(CallbackInfoReturnable<Float> cir) {
        if(!ConfigHandler.general_pixelConsistentTerrainParticles) {
            return;
        }
        TextureAtlas atlas = TextureHelpers.getTextureAtlas(this.sprite.atlasLocation());
        if(atlas == null) {
            return;
        }
        float spriteU = this.sprite.getU(this.uo * (this.quadSize * 2));
        cir.setReturnValue(MathHelpers.ceilWithResolution(Math.max(0.001f, spriteU), ((TextureAtlasAccessor) atlas).getWidth()));
    }

    @Inject(
        at = @At("HEAD"),
        method = "getV0",
        cancellable = true
    )
    protected void getV0(CallbackInfoReturnable<Float> cir) {
        if(!ConfigHandler.general_pixelConsistentTerrainParticles) {
            return;
        }
        TextureAtlas atlas = TextureHelpers.getTextureAtlas(this.sprite.atlasLocation());
        if(atlas == null) {
            return;
        }
        float spriteV = this.sprite.getV(this.vo * (this.quadSize * 2));
        cir.setReturnValue(MathHelpers.ceilWithResolution(Math.max(0.001f, spriteV), ((TextureAtlasAccessor) atlas).getHeight()));
    }

    @Inject(
        at = @At("HEAD"),
        method = "getV1",
        cancellable = true
    )
    protected void getV1(CallbackInfoReturnable<Float> cir) {
        if(!ConfigHandler.general_pixelConsistentTerrainParticles) {
            return;
        }
        TextureAtlas atlas = TextureHelpers.getTextureAtlas(this.sprite.atlasLocation());
        if(atlas == null) {
            return;
        }
        float spriteV = this.sprite.getV((this.vo + 1) * (this.quadSize * 2));
        cir.setReturnValue(MathHelpers.ceilWithResolution(Math.max(0.001f, spriteV), ((TextureAtlasAccessor) atlas).getHeight()));
    }
}
