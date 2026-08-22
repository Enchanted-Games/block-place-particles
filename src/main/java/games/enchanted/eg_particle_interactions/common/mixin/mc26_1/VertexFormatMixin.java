//? if minecraft: <= 26.1 {
/*package games.enchanted.eg_particle_interactions.common.mixin.mc26_1;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import games.enchanted.eg_particle_interactions.common.particle.render.vertex.PIVertexFormats;
import it.unimi.dsi.fastutil.ints.IntList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(VertexFormat.class)
public class VertexFormatMixin {
    @Shadow
    @Final
    private int[] offsetsByElement;
    @Unique
    private int eg_particle_interactions$maskUvIndex;

    @Inject(
        at = @At("TAIL"),
        method = "<init>"
    )
    private void eg_particle_interactions$fixCustomOffsets(List<VertexFormatElement> elements, List<String> names, IntList offsets, int vertexSize, CallbackInfo ci) {
        if(!names.contains(PIVertexFormats.MASK_UV_SEMANTIC_NAME)) return;
        this.eg_particle_interactions$maskUvIndex = names.indexOf(PIVertexFormats.MASK_UV_SEMANTIC_NAME);

        for(int id = 0; id < this.offsetsByElement.length; ++id) {
            if(id == this.eg_particle_interactions$maskUvIndex) {
                this.offsetsByElement[id] = this.offsetsByElement[id - 1] + PIVertexFormats.MASK_UV_ELEMENT.byteSize();
                continue;
            }
            VertexFormatElement element = VertexFormatElement.byId(id);
            int index = element != null ? elements.indexOf(element) : -1;
            this.offsetsByElement[id] = index != -1 ? offsets.getInt(index) : -1;
        }
    }

    @WrapMethod(
        method = "getOffset"
    )
    private int eg_particle_interactions$returnCorrectOffset(VertexFormatElement element, Operation<Integer> original) {
        if(element == PIVertexFormats.MASK_UV_ELEMENT) return this.offsetsByElement[this.eg_particle_interactions$maskUvIndex];
        return original.call(element);
    }
}
*///? }
