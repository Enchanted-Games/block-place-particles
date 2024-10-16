package games.enchanted.blockplaceparticles.config.controller.generic;

import dev.isxander.yacl3.api.Binding;
import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import games.enchanted.blockplaceparticles.config.type.TwoTypesInterface;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public abstract class HybridOptionController<A, B, T extends TwoTypesInterface<A, B>> implements Controller<T> {
    private final Option<T> option;
    @NotNull A currentTypeAValue;
    @NotNull Option<A> typeAOption;
    @NotNull B currentTypeBValue;
    @NotNull Option<B> typeBOption;

    public HybridOptionController(Option<T> option) {
        this.option = option;
        this.currentTypeAValue = option.pendingValue().getTypeA();
        this.typeAOption = createOptionForTypeA();
        this.currentTypeBValue = option.pendingValue().getTypeB();
        this.typeBOption = createOptionForTypeB();
        this.option.addListener(this::updateCurrentValues);
    }

    public @NotNull Option<A> getTypeAOption() {
        return typeAOption;
    }
    public @NotNull Option<B> getTypeBOption() {
        return typeBOption;
    }

    /**
     * Creates the {@link Option} for the first type (A).
     * The binding for the option returned by this method should be created via the respective helper function to ensure the values are saved properly
     *
     * @see HybridOptionController#getTypeABinding
     */
    protected abstract Option<A> createOptionForTypeA();
    protected Binding<A> getTypeABinding(A defaultValue) {
        return Binding.generic(defaultValue, () -> this.currentTypeAValue, this::setTypeA);
    }

    /**
     * Creates the {@link Option} for the second type (B).
     * The binding for the option returned by this method should be created via the respective helper function to ensure the values are saved properly
     *
     * @see HybridOptionController#getTypeBBinding
     */
    protected abstract Option<B> createOptionForTypeB();
    protected Binding<B> getTypeBBinding(B defaultValue) {
        return Binding.generic(defaultValue, () -> this.currentTypeBValue, this::setTypeB);
    }

    protected void setTypeA(A location) {
        this.currentTypeAValue = location;
        this.setPendingValue();
    }
    protected void setTypeB(B colour) {
        this.currentTypeBValue = colour;
        this.setPendingValue();
    }
    protected void setPendingValue() {
        this.option.requestSet(this.getValueToSetAsPending(this.currentTypeAValue, this.currentTypeBValue));
    }

    protected abstract T getValueToSetAsPending(A typeA, B typeB);

    // updates the values of typeAOption and typeBOption when the option is changed
    protected void updateCurrentValues(Option<T> changedOption, T pending) {
        this.typeAOption.requestSet(pending.getTypeA());
        this.typeAOption.applyValue();
        this.typeBOption.requestSet(pending.getTypeB());
        this.typeBOption.applyValue();
    }

    @Override
    public Option<T> option() {
        return this.option;
    }

    @Override
    public Component formatValue() {
        return Component.literal("");
    }

    @Override
    public abstract AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension);
}
