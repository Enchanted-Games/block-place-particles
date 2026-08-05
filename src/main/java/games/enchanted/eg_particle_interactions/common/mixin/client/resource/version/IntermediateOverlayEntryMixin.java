package games.enchanted.eg_particle_interactions.common.mixin.client.resource.version;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.resource.version.PackOverlayDuck;
import games.enchanted.eg_particle_interactions.common.resource.version.ParticleInteractionsPackOverlay;
import net.minecraft.server.packs.OverlayMetadataSection;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OverlayMetadataSection.OverlayEntry.IntermediateEntry.class)
public class IntermediateOverlayEntryMixin implements PackOverlayDuck {
    @Unique
    private ParticleInteractionsPackOverlay eg_particle_interactions$customOverlay = null;

    @ModifyExpressionValue(
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"
        ),
        method = "<clinit>"
    )
    private static Codec<OverlayMetadataSection.OverlayEntry.IntermediateEntry> polytone$decorateCodec(
        Codec<OverlayMetadataSection.OverlayEntry.IntermediateEntry> original) {
        return ParticleInteractionsPackOverlay.wrapOverlayCodec(original);
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
