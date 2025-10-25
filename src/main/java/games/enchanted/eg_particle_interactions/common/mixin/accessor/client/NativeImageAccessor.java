package games.enchanted.eg_particle_interactions.common.mixin.accessor.client;

import com.mojang.blaze3d.platform.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NativeImage.class)
public interface NativeImageAccessor {
    @Accessor("pixels")
    long eg_particle_interactions$getPixels();
}
