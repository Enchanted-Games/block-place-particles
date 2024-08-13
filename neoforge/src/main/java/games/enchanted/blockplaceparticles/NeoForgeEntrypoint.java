package games.enchanted.blockplaceparticles;

import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
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
            }
        });
    }
}