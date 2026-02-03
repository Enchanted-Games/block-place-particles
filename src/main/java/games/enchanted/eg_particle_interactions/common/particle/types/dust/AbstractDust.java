package games.enchanted.eg_particle_interactions.common.particle.types.dust;

import games.enchanted.eg_particle_interactions.common.config.categories.GeneralOptions;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import games.enchanted.eg_particle_interactions.common.util.LightUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDust extends ParticleInteractionsParticle {
    public static float MIN_SIZE = 0.095f;
    public static float MAX_SIZE = 0.125f;

    protected boolean spawnSpecks;
    protected boolean spriteFromAge;
    protected SpriteSet spriteSet;
    protected boolean emissive;

    protected AbstractDust(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, float gravityMultiplier, boolean spawnSpecks, boolean spriteFromAge) {
        super(level, x, y, z, spriteSet.get(level.getRandom()));

        this.spawnSpecks = spawnSpecks;
        this.spriteSet = spriteSet;
        this.spriteFromAge = spriteFromAge;

        this.gravity = Mth.randomBetween(this.random, 0.25F, 0.38F);;
        this.friction = 1.0F;
        this.xd = xSpeed + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.yd = ySpeed + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.zd = zSpeed + (Math.random() * 2.0 - 1.0) * 0.05000000074505806;
        this.lifetime = (int)(16.0 / ((double)this.random.nextFloat() * 0.8 + 0.2)) + 2;
        this.roll = (float) Math.toRadians(this.random.nextIntBetweenInclusive(0, 360));
        this.prevRoll = this.roll;

        float particleSize = this.random.nextBoolean() ? MIN_SIZE : MAX_SIZE;
        this.setScale(particleSize);
        this.setSize(particleSize, particleSize);
        this.gravity *= gravityMultiplier;

        if(this.spriteFromAge) {
            this.setSpriteFromAge(this.spriteSet);
        } else {
            this.setSprite(spriteSet.get(this.random));
        }

        this.emissive = false;
    }

    @Override
    public void tick() {
        if(this.spriteFromAge) {
            this.setSpriteFromAge(this.spriteSet);
        }

        this.xd *= 0.949999988079071;
        this.yd *= 0.8999999761581421;
        this.zd *= 0.949999988079071;

        this.gravity = 0.98F * this.gravity;
        this.friction = 0.995F * this.friction;

        super.tick();

        if(!this.spawnSpecks || this.removed || !this.hasPhysics || this.onGround) {
            return;
        }
        if(!GeneralOptions.DUST_SPECKS.getValue()) {
            return;
        }
        if((this.age < 3 && this.random.nextFloat() < 0.23f) || this.random.nextFloat() < 0.01f) {
            ParticleOptions particleOptions = this.getSpeckParticle();
            if(particleOptions != null) {
                this.level.addParticle(particleOptions, this.x, this.y, this.z, this.xd / 2, (this.yd / 2) + 0.05, this.zd / 2);
            }
        }
    }

    @Override
    protected int getLightmapCoords(float partialTick) {
        return this.emissive ? LightUtil.FULL_BRIGHT : super.getLightmapCoords(partialTick);
    }

    protected abstract @Nullable ParticleOptions getSpeckParticle();
}