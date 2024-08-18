package games.enchanted.blockplaceparticles;

import games.enchanted.blockplaceparticles.config.PI_ConfigScreen;
import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import games.enchanted.blockplaceparticles.particle.RegParticleProvidersNeoForge;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(value = ParticleInteractionsMod.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeClientEntrypoint {
    public final IEventBus eventBus;

    public NeoForgeClientEntrypoint(IEventBus bus) {
        this.eventBus = bus;
        ParticleInteractionsMod.startOfModLoading();

        // register stuff
        bus.addListener((RegisterEvent event) -> {
            if(event.getRegistry().key().equals(Registries.PARTICLE_TYPE)) {
                ModParticleTypes.registerParticles();
            }
        });

        // register particle providers
        bus.addListener(RegParticleProvidersNeoForge::registerParticleProviders);
        // register config screen
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (client, parent) -> PI_ConfigScreen.createConfigScreen(parent));

        ParticleInteractionsMod.endOfModLoading();
    }
}