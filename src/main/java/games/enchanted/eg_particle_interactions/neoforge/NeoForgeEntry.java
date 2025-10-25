//? if neoforge {
/*package games.enchanted.eg_particle_interactions.neoforge;

import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.config.ConfigScreen;
import games.enchanted.eg_particle_interactions.common.particle.ModParticleTypes;
import games.enchanted.eg_particle_interactions.common.resource.ClientResourceReload;
import games.enchanted.eg_particle_interactions.neoforge.registry.NeoParticleProviderRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

/^*
 * This is the entry point for your mod's forge side.
 ^/
@Mod(value = "eg_particle_interactions", dist = Dist.CLIENT)
public class NeoForgeEntry {
    public final IEventBus eventBus;

    public NeoForgeEntry(IEventBus bus) {
        this.eventBus = bus;
        ParticleInteractionsMod.startOfModLoading();

        // register stuff
        bus.addListener((RegisterEvent event) -> {
            if(event.getRegistry().key().equals(Registries.PARTICLE_TYPE)) {
                ModParticleTypes.registerParticles();
            }
        });

        // register client resource reload listener
        bus.addListener((AddClientReloadListenersEvent event) -> {
            ParticleInteractionsMod.createReloadListeners(Minecraft.getInstance().getTextureManager()).forEach(resourceLocationAndListenerPair -> {
                event.addListener(resourceLocationAndListenerPair.key(), resourceLocationAndListenerPair.value());
            });
            event.addListener(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "clear_cache_listener"), new SimplePreparableReloadListener<Void>() {
                @Override
                protected @NotNull Void prepare(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
                    ClientResourceReload.onReload(resourceManager);
                    return null;
                }

                @Override
                protected void apply(@NotNull Void object, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {}
            });
        });

        // register particle providers
        bus.addListener(NeoParticleProviderRegistry::registerParticleProviders);
        // register config screen
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (client, parent) -> ConfigScreen.createConfigScreen(parent));

        ParticleInteractionsMod.endOfModLoading();
    }
}
*///?}