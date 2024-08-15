package games.enchanted.blockplaceparticles.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import java.util.ArrayList;

// this feels so cursed but it works so ¯\_(ツ)_/¯
public class RegParticleProvidersNeoForge {
    private static final ArrayList<PendingProvider<? extends ParticleOptions>> pendingProviders = new ArrayList<>();

    private static class PendingProvider<T extends ParticleOptions>  {
        ParticleType<? extends ParticleOptions> pType;
        ModParticleTypes.SpriteParticleProviderRegistration<?> pProvider;
        public PendingProvider(ParticleType<T> particleType, ModParticleTypes.SpriteParticleProviderRegistration<T> particleProvider) {
            this.pType = particleType;
            this.pProvider = particleProvider;
        }
    }

    public static <T extends ParticleOptions> void registerProviderWhenReady(ParticleType<T> particleType, ModParticleTypes.SpriteParticleProviderRegistration<T> particleProvider) {
        pendingProviders.addLast(new PendingProvider<>(particleType, particleProvider));
    }

    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        for (int i = 0; i < pendingProviders.size(); i++) {
            PendingProvider pendingProvider = pendingProviders.get(i);
            event.registerSpriteSet(pendingProvider.pType, pendingProvider.pProvider::create);
        }
    }
}
