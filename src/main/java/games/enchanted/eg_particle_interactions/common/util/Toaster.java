package games.enchanted.eg_particle_interactions.common.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;

public class Toaster {
    static final SystemToast.SystemToastId PI_TOAST = new SystemToast.SystemToastId(10000L);

    public static void showToast(Component title, Component body) {
        var toastManager =
            //? if minecraft: >= 26.2 {
            Minecraft.getInstance().gui.toastManager();
            //? } else {
            /*Minecraft.getInstance().getToastManager();
            *///? }

        toastManager.addToast(new SystemToast(PI_TOAST, title, body));
    }
}
