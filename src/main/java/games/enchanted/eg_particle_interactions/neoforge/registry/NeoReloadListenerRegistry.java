//? if neoforge {
/*package games.enchanted.eg_particle_interactions.neoforge.registry;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;

import java.util.ArrayList;
import java.util.List;

public class NeoReloadListenerRegistry {
    private static final List<PendingListener> PENDING_LISTENERS = new ArrayList<>();

    public static void registerListener(PreparableReloadListener listener, Identifier id) {
        PENDING_LISTENERS.add(new PendingListener(listener, id));
    }

    public static void register(AddClientReloadListenersEvent event) {
        for (PendingListener listener : PENDING_LISTENERS) {
            event.getRegistry().put(listener.id, listener.listener());
        }
    }

    private record PendingListener(PreparableReloadListener listener, Identifier id) {
    }
}

*///?}