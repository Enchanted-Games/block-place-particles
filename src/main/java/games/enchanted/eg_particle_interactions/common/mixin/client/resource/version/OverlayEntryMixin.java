package games.enchanted.eg_particle_interactions.common.mixin.client.resource.version;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.resource.version.PackOverlayDuck;
import games.enchanted.eg_particle_interactions.common.resource.version.ParticleInteractionsPackOverlay;
import net.minecraft.server.packs.OverlayMetadataSection;
import net.minecraft.server.packs.metadata.pack.PackFormat;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OverlayMetadataSection.OverlayEntry.class)
public class OverlayEntryMixin implements PackOverlayDuck {
    @Unique
    private ParticleInteractionsPackOverlay eg_particle_interactions$customOverlay = null;

    @ModifyReturnValue(
        at = @At("RETURN"),
        method = "lambda$listCodecForPackType$1"
    )
    private static OverlayMetadataSection.OverlayEntry eg_particle_interactions$wrapOverlayCreation(OverlayMetadataSection.OverlayEntry original, OverlayMetadataSection.OverlayEntry.IntermediateEntry intermediate) {
        ParticleInteractionsPackOverlay overlay = ((PackOverlayDuck) (Object) intermediate).eg_particle_interactions$getCustomOverlay();
        if(overlay != null) {
            ((PackOverlayDuck) (Object) original).eg_particle_interactions$setCustomOverlay(overlay);
        }
        return original;
    }

    @WrapMethod(
        method = "isApplicable"
    )
    private boolean eg_particle_interactions$wrapApplicable(PackFormat formatToTest, Operation<Boolean> original) {
        if(this.eg_particle_interactions$customOverlay == null || this.eg_particle_interactions$customOverlay.isUnspecified()) {
            return original.call(formatToTest);
        }
        boolean withinSpecifiedVersion = this.eg_particle_interactions$customOverlay.range().containsVersion(Constants.CURRENT_PACK_VERSION);
        if(this.eg_particle_interactions$customOverlay.ignoreVanillaFormats()) {
            return withinSpecifiedVersion;
        }
        return original.call(formatToTest) && withinSpecifiedVersion;
    }

    @Override
    public void eg_particle_interactions$setCustomOverlay(@NonNull ParticleInteractionsPackOverlay range) {
        this.eg_particle_interactions$customOverlay = range;
    }

    @Override
    public @Nullable ParticleInteractionsPackOverlay eg_particle_interactions$getCustomOverlay() {
        return this.eg_particle_interactions$customOverlay;
    }
}
