package games.enchanted.eg_particle_interactions.common.gui.debug;

import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.config.ConfigOptions;
import games.enchanted.eg_particle_interactions.common.config.categories.DebugOptions;
import games.enchanted.eg_particle_interactions.common.config.option.ConfigOption;
import games.enchanted.eg_particle_interactions.common.gui.debug.actions.ReloadCustomAssetsAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class DebugScreen extends Screen {
    private static final String DEBUG_TRANSLATION_PREFIX = "debug.eg_particle_interactions.";

    private static final List<Action> ACTIONS = List.of(
        new Action("reload_custom_assets", new ReloadCustomAssetsAction())
    );
    private static final List<ConfigOption<Boolean>> TOGGLES = List.of(
        DebugOptions.DEBUG_KEYBIND_ENABLED,
        DebugOptions.DEBUG_EMITTER_BOUNDS,
        DebugOptions.DEBUG_PARTICLE_TICK_BOUNDING_BOXES,
        DebugOptions.DEBUG_PARTICLE_RENDER_BOUNDING_BOXES,
        DebugOptions.DEBUG_EXTRA_INFO_ON_PARTICLE_PACKET_ERROR,
        DebugOptions.DEBUG_TEXTURE_LOGGING
    );

    private final @Nullable Screen parentScreen;

    private HeaderAndFooterLayout layout;
    private GridLayout grid;

    DebugScreen(@Nullable Screen parent) {
        super(Component.literal(Constants.MOD_NAME + " Debug"));
        this.parentScreen = parent;
    }

    @Override
    protected void init() {
        this.layout = new HeaderAndFooterLayout(this, HeaderAndFooterLayout.DEFAULT_HEADER_AND_FOOTER_HEIGHT, HeaderAndFooterLayout.DEFAULT_HEADER_AND_FOOTER_HEIGHT);
        this.grid = new GridLayout(0, this.layout.getHeaderHeight());
        this.layout.addToContents(this.grid);

        for (int i = 0; i < ACTIONS.size(); i++) {
            int column = i % 2;
            int row = i - column;

            this.grid.addChild(
                makeActionButton(ACTIONS.get(i))
                    .bounds((this.width / 2) - (Button.DEFAULT_WIDTH / 2), (this.height / 2), Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT)
                    .build(),
                row,
                column
            );
        }

        for (int i = 0; i < TOGGLES.size(); i++) {
            ConfigOption<Boolean> option = TOGGLES.get(i);
            int i2 = i + ACTIONS.size();
            int column = i2 % 2;
            int row = i2 - column;

            this.grid.addChild(
                makeBooleanValueButton(option)
                    .bounds((this.width / 2) - (Button.DEFAULT_WIDTH / 2), (this.height / 2), Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT)
                    .build(),
                row,
                column
            );
        }

        this.grid.newCellSettings().alignHorizontallyCenter();
        this.grid.columnSpacing(8);
        this.grid.rowSpacing(2);
        this.grid.arrangeElements();
        this.grid.visitWidgets(this::addRenderableWidget);

        // done button
        AbstractWidget doneButton = (
            Button.builder(
                    CommonComponents.GUI_DONE,
                    button -> onClose()
                )
                .bounds((this.width / 2) - (Button.DEFAULT_WIDTH / 2), this.height - 60, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT)
                .build());

        this.layout.addToFooter(doneButton);
        this.addRenderableWidget(doneButton);

        this.layout.arrangeElements();
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        super.extractRenderState(context, mouseX, mouseY, delta);
        context.textRenderer().accept(TextAlignment.CENTER, this.width / 2, this.layout.getHeaderHeight(), this.title);
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(this.parentScreen);
        ConfigOptions.saveConfig();
    }

    @Override
    public boolean isPauseScreen() {
        if(this.parentScreen == null) return false;
        return this.parentScreen.isPauseScreen();
    }

    @Override
    protected void repositionElements() {
        super.repositionElements();
        this.layout.arrangeElements();
        this.grid.arrangeElements();
    }

    private Button.Builder makeBooleanValueButton(ConfigOption<Boolean> value) {
        String id = value.getJsonKey();
        Component buttonContent = Component.translatableWithFallback(DEBUG_TRANSLATION_PREFIX + id, id);
        Component buttonTooltipText = Component.translatableWithFallback(DEBUG_TRANSLATION_PREFIX + id + ".tooltip", id);

        return Button.builder(CommonComponents.optionNameValue(buttonContent, trueFalseOptionStatus(value.getPendingOrCurrentValue())),
                button -> {
                    boolean newValue = !value.getPendingOrCurrentValue();
                    value.setPendingValue(newValue);
                    value.applyPendingValue();

                    Component buttonValue = trueFalseOptionStatus(newValue);

                    button.setMessage(CommonComponents.optionNameValue(buttonContent, buttonValue));
                })
            .tooltip(Tooltip.create(buttonTooltipText));
    }

    private Button.Builder makeActionButton(Action action) {
        String id = action.id();
        Component buttonContent = Component.translatableWithFallback(DEBUG_TRANSLATION_PREFIX + id, id);
        Component buttonTooltipText = Component.translatableWithFallback(DEBUG_TRANSLATION_PREFIX + id + ".tooltip", id);

        return Button.builder(buttonContent, button -> action.runnable().run())
            .tooltip(Tooltip.create(buttonTooltipText));
    }

    public static Component trueFalseOptionStatus(boolean value) {
        return value ? CommonComponents.GUI_YES : CommonComponents.GUI_NO;
    }

    public static void show() {
        Screen parentScreen = Minecraft.getInstance().gui.screen();
        Minecraft.getInstance().gui.setScreen(new DebugScreen(parentScreen));
    }

    protected record Action(String id, Runnable runnable) {
    }
}
