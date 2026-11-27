package games.enchanted.eg_particle_interactions.common.mixin.plugin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class VersionPlugin26_3 implements IMixinConfigPlugin {
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        //? if minecraft: >= 26.2 {
        return true;
        //? } else {
        /*return false;
        *///? }
    }

    @Override
    public List<String> getMixins() {
        //? if minecraft: >= 26.2 {
        return List.of(
            "FeatureRendererDispatcherMixin",
            "SubmitNodeCollectionMixin",
            "SubmitNodeStorageMixin",
            "blocks.LeavesBlockMixin",
            "entity.SulfurCubeMixin",
            "StagedVertexBufferMixin",
            "accessor.StagedVertexBufferDrawAccessor",
            "items.BlockTransformerMixin"
        );
        //? } else {
        /*return null;
        *///? }
    }
}
