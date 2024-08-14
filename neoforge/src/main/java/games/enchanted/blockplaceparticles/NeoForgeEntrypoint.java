package games.enchanted.blockplaceparticles;

import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import games.enchanted.blockplaceparticles.particle.RegParticleProvidersNeoForge;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(BlockPlaceParticlesConstants.MOD_ID)
public class NeoForgeEntrypoint {
    public final IEventBus eventBus;

    public NeoForgeEntrypoint(IEventBus bus) {
        this.eventBus = bus;
        CommonEntrypoint.initBeforeRegistration();

        // register stuff
        bus.addListener((RegisterEvent event) -> {
            if(event.getRegistry().key().equals(Registries.PARTICLE_TYPE)) {
                ModParticleTypes.registerParticles();
            }
        });

        // register particle providers
        bus.addListener(RegParticleProvidersNeoForge::registerParticleProviders);
    }
}