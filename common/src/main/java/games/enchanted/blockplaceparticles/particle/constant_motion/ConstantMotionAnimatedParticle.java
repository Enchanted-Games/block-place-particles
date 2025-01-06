package games.enchanted.blockplaceparticles.particle.constant_motion;

import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConstantMotionAnimatedParticle extends TextureSheetParticle {
    SpriteSet sprites;
    boolean transparency = false;

    protected ConstantMotionAnimatedParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, int lifetime, float quadSize, boolean transparency) {
        super(level, x, y, z);
        this.sprites = spriteSet;
        this.setSpriteFromAge(sprites);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;

        this.transparency = transparency;
        this.lifetime = lifetime;
        this.quadSize = quadSize;
    }
    protected ConstantMotionAnimatedParticle(ClientLevel level, double x, double y, double z, double constantXSpeed, double constantYSpeed, double constantZSpeed, SpriteSet spriteSet, int lifetime, float quadSize, boolean transparency) {
        this(level, x, y, z, spriteSet, lifetime, quadSize, transparency);
        this.xd = constantXSpeed;
        this.yd = constantYSpeed;
        this.zd = constantZSpeed;
    }

    @Override
    public void tick() {
        if(this.removed) return;
        if(this.age > this.lifetime){
            this.remove();
        }

        this.setSpriteFromAge(sprites);
        ++this.age;

        this.x -= this.xd;
        this.y -= this.yd;
        this.z -= this.zd;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return this.transparency ? ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT : ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
}
