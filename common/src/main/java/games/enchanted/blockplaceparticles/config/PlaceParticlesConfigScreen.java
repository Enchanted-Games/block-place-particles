package games.enchanted.blockplaceparticles.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PlaceParticlesConfigScreen {
    public static Screen createConfigScreen(Screen parentScreen) {
        return YetAnotherConfigLib.createBuilder()
            .title(Component.literal("placeholder config title"))
            .category(ConfigCategory.createBuilder()
                .name(Component.literal("Name of the category"))
                .tooltip(Component.literal("This text will appear as a tooltip when you hover or focus the button with Tab. There is no need to add \n to wrap as YACL will do it for you."))

                .build())
            .build()
        .generateScreen(parentScreen);
    }
}
