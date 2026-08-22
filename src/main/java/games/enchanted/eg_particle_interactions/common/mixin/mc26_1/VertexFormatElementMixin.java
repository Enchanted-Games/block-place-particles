//? if minecraft: <= 26.1 {
/*package games.enchanted.eg_particle_interactions.common.mixin.mc26_1;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import games.enchanted.eg_particle_interactions.common.particle.render.vertex.PIVertexFormats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VertexFormatElement.class)
public class VertexFormatElementMixin {
    @Definition(id = "BY_ID", field = "Lcom/mojang/blaze3d/vertex/VertexFormatElement;BY_ID:[Lcom/mojang/blaze3d/vertex/VertexFormatElement;")
    @Expression("BY_ID[?] = ?")
    @WrapOperation(
        method = "register",
        at = @At(value = "MIXINEXTRAS:EXPRESSION")
    )
    private static void eg_particle_interactions$dontAddCustomToArray(VertexFormatElement[] array, int index, VertexFormatElement value, Operation<Void> original, int id) {
        if (id == PIVertexFormats.MASK_UV_ID) return;
        original.call(array, index, value);
    }

    @Definition(id = "BY_ID", field = "Lcom/mojang/blaze3d/vertex/VertexFormatElement;BY_ID:[Lcom/mojang/blaze3d/vertex/VertexFormatElement;")
    @Expression("BY_ID[?]")
    @WrapOperation(
        method = "register",
        at = @At(value = "MIXINEXTRAS:EXPRESSION")
    )
    private static VertexFormatElement eg_particle_interactions$dontTryGetCustomFromArray(VertexFormatElement[] array, int index, Operation<VertexFormatElement> original, int id) {
        if (id == PIVertexFormats.MASK_UV_ID) return null;
        return original.call(array, index);
    }

    @Definition(id = "BY_ID", field = "Lcom/mojang/blaze3d/vertex/VertexFormatElement;BY_ID:[Lcom/mojang/blaze3d/vertex/VertexFormatElement;")
    @Expression("BY_ID.length")
    @WrapOperation(
        method = "<init>",
        at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private int eg_particle_interactions$bypassRangeCheckForCustom(VertexFormatElement[] array, Operation<Integer> original, int id) {
        if (id == PIVertexFormats.MASK_UV_ID) return PIVertexFormats.MASK_UV_ID + 1;
        return original.call((Object) array);
    }

    @WrapMethod(
        method = "byId"
    )
    private static VertexFormatElement eg_particle_interactions$wrapById(int id, Operation<VertexFormatElement> original) {
        if(id == PIVertexFormats.MASK_UV_ID) return PIVertexFormats.MASK_UV_ELEMENT;
        return original.call(id);
    }
}
*///? }
