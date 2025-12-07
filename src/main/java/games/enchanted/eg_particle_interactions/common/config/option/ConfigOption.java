package games.enchanted.eg_particle_interactions.common.config.option;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.isxander.yacl3.api.Binding;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ConfigOption<T> {
    private @Nullable T pendingValue;
    protected T value;
    private final T defaultValue;

    private final String jsonKey;

    protected ConfigOption(T initialAndDefaultValue, String jsonKey) {
        this.value = initialAndDefaultValue;
        this.defaultValue = initialAndDefaultValue;
        this.jsonKey = jsonKey;
    }

    public String getJsonKey() {
        return this.jsonKey;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public T getValue() {
        return this.value;
    }

    public @Nullable T getPendingValue() {
        return this.pendingValue;
    }

    public T getPendingOrCurrentValue() {
        return this.pendingValue == null ? this.value : this.pendingValue;
    }

    public void setPendingValue(@NotNull T value) {
        this.pendingValue = value;
    }

    protected void setValueOrPending(T value) {
        if(isDirty()) {
            this.setPendingValue(value);
        } else {
            this.value = value;
        }
    }

    public void clearPendingValue() {
        this.pendingValue = null;
    }

    public void applyPendingValue() {
        if(this.pendingValue == null) return;
        this.value = this.pendingValue;
        this.pendingValue = null;
    }

    /**
     * Resets value to default
     *
     * @param force if true, clears pending value and set current value to default. If false, sets pending value
     *              to default if one is already present
     */
    public void resetToDefault(boolean force) {
        if(force) {
            this.clearPendingValue();
            this.value = this.getDefaultValue();
        } else {
            this.setValueOrPending(getDefaultValue());
        }
    }

    public boolean isDefault() {
        return getValue().equals(getDefaultValue());
    }

    public abstract @Nullable JsonElement toJson();

    public abstract void fromJson(JsonObject json);

    public boolean isDirty() {
        return this.pendingValue != null;
    }

    public Binding<T> createBinding() {
        return Binding.generic(this.getDefaultValue(), this::getPendingOrCurrentValue, this::setPendingValue);
    }
}
