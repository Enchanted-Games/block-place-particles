package games.enchanted.eg_particle_interactions.common.gui.debug.actions;

import games.enchanted.eg_particle_interactions.common.resource.texture.TexturePalettes;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.SimpleReloadInstance;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ReloadCustomAssetsAction implements Runnable {
    public static final List<PreparableReloadListener> LISTENERS = new ArrayList<>();

    @Override
    public void run() {
        Minecraft minecraft = Minecraft.getInstance();
        SimpleReloadInstance.create(
            minecraft.getResourceManager(),
            LISTENERS,
            Util.backgroundExecutor(),
            minecraft,
            CompletableFuture.completedFuture(Unit.INSTANCE),
            false
        ).done();
        TexturePalettes.invalidateCaches();
    }
}
