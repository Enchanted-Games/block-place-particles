package games.enchanted.blockplaceparticles;

import games.enchanted.blockplaceparticles.config.ConfigScreen;
import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import games.enchanted.blockplaceparticles.particle.RegParticleProvidersNeoForge;
import games.enchanted.blockplaceparticles.resource.ClientResourceReload;
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

        // register client resource reload listener
        bus.addListener((AddClientReloadListenersEvent event) -> {
            event.addListener(ResourceLocation.fromNamespaceAndPath(ParticleInteractionsMod.MOD_ID, "clear_cache_listener"), new SimplePreparableReloadListener<Void>() {
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
        bus.addListener(RegParticleProvidersNeoForge::registerParticleProviders);
        // register config screen
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (client, parent) -> ConfigScreen.createConfigScreen(parent));

        ParticleInteractionsMod.endOfModLoading();
    }
}