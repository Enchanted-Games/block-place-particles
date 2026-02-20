//? if neoforge {
/*package games.enchanted.eg_particle_interactions.neoforge.registry;

import games.enchanted.eg_particle_interactions.common.particle.types.ModParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import java.util.ArrayList;

public class NeoParticleProviderRegistry {
    private static final ArrayList<PendingProvider<? extends ParticleOptions>> pendingProviders = new ArrayList<>();

    private static class PendingProvider<T extends ParticleOptions>  {
        ParticleType<? extends ParticleOptions> pType;
        ModParticleTypes.SpriteProviderReg<?> pProvider;
        public PendingProvider(ParticleType<T> particleType, ModParticleTypes.SpriteProviderReg<T> particleProvider) {
            this.pType = particleType;
            this.pProvider = particleProvider;
        }
    }

    public static <T extends ParticleOptions> void registerProviderWhenReady(ParticleType<T> particleType, ModParticleTypes.SpriteProviderReg<T> particleProvider) {
        pendingProviders.addLast(new PendingProvider<>(particleType, particleProvider));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        for (PendingProvider pendingProvider : pendingProviders) {
            event.registerSpriteSet(pendingProvider.pType, pendingProvider.pProvider::create);
        }
    }
}
*///? }