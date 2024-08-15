package games.enchanted.blockplaceparticles.platform;

import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import games.enchanted.blockplaceparticles.platform.services.PlatformHelperInterface;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

public class FabricPlatformHelper implements PlatformHelperInterface {
    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public SimpleParticleType createNewSimpleParticle(boolean alwaysShow) {
        return FabricParticleTypes.simple(alwaysShow);
    }

    @Override
    public <T extends ParticleOptions> void registerParticleProvider(ParticleType<T> particleType, ModParticleTypes.SpriteParticleProviderRegistration<T> particleProvider) {
        ParticleFactoryRegistry.getInstance().register(particleType, particleProvider::create);
    }
}
