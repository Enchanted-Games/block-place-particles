//? if neoforge {
/*package games.enchanted.eg_particle_interactions.neoforge;

import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.config.compat.ConfigScreenCreator;
import games.enchanted.eg_particle_interactions.common.particle.ParticleTypesRegistry;
import games.enchanted.eg_particle_interactions.neoforge.registry.NeoParticleProviderRegistry;
import games.enchanted.eg_particle_interactions.neoforge.registry.NeoReloadListenerRegistry;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Objects;

/^*
 * This is the entry point for your mod's forge side.
 ^/
@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeEntry {
    public final IEventBus eventBus;

    public NeoForgeEntry(IEventBus bus) {
        this.eventBus = bus;
        ParticleInteractionsMod.startOfModLoading();

        // register stuff
        bus.addListener((RegisterEvent event) -> {
            if(event.getRegistry().key().equals(Registries.PARTICLE_TYPE)) {
                ParticleTypesRegistry.registerParticles();
            }
        });

        // register client resource reload listener
        bus.addListener(NeoReloadListenerRegistry::register);

        // register particle providers
        bus.addListener(NeoParticleProviderRegistry::registerParticleProviders);
        // register config screen
        ConfigScreenCreator screenCreator = ConfigScreenCreator.getScreenCreator();
        if(screenCreator.canCreateScreen()) {
            ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (client, parent) -> Objects.requireNonNull(screenCreator.createScreen(parent)));
        }

        ParticleInteractionsMod.endOfModLoading();
    }
}
*///?}