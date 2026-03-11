package games.enchanted.eg_particle_interactions.common.mixin.client.accessor;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BucketItem.class)
public interface BucketItemAccessor {
    @Accessor("content")
    Fluid eg_particle_interactions$getContent();
}