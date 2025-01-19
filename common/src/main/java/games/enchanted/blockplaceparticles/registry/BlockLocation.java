package games.enchanted.blockplaceparticles.registry;

import net.minecraft.resources.ResourceLocation;

public record BlockLocation(ResourceLocation location, boolean isTag) {
    public BlockLocation(ResourceLocation location) {
        this(location, false);
    }

    @Override
    public String toString() {
        return (this.isTag() ? "#" : "") + location.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BlockLocation castedObj)) return false;
        return this.location.equals(castedObj.location);
    }
}
