package games.enchanted.blockplaceparticles;

import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import games.enchanted.blockplaceparticles.particle.RegParticleProvidersNeoForge;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(value = BlockPlaceParticlesConstants.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeClientEntrypoint {
    public final IEventBus eventBus;

    public NeoForgeClientEntrypoint(IEventBus bus, ModContainer container) {
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