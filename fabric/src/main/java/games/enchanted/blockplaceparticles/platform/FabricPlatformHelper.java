package games.enchanted.blockplaceparticles.platform;

import games.enchanted.blockplaceparticles.platform.services.PlatformHelperInterface;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.block.Block;

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
}
