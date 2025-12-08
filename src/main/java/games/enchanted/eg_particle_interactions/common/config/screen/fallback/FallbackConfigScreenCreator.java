package games.enchanted.eg_particle_interactions.common.config.screen.fallback;

import games.enchanted.eg_particle_interactions.common.config.compat.ConfigScreenCreator;
import games.enchanted.eg_particle_interactions.common.localisation.ConfigTranslation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;

import java.net.URI;

public class FallbackConfigScreenCreator implements ConfigScreenCreator {
    @Override
    public @Nullable Screen createScreen(Screen parent) {
        return new ConfirmScreen(
            confirmed -> {
                if(confirmed) {
                    ConfirmLinkScreen.confirmLinkNow(parent, URI.create("https://modrinth.com/mod/yacl"));
                } else {
                    Minecraft.getInstance().setScreen(parent);
                }
            },
            ConfigTranslation.getFallbackConfigTitle().toComponent().copy().withStyle(Style.EMPTY.withBold(true)),
            ConfigTranslation.getFallbackConfigBody().toComponent(),
            ConfigTranslation.getDownloadYACLButtonMessage().toComponent(),
            CommonComponents.GUI_BACK
        );
    }
}
