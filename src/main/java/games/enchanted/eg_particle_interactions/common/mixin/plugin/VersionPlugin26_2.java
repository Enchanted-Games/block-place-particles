package games.enchanted.eg_particle_interactions.common.mixin.plugin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class VersionPlugin26_2 implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        //? if minecraft: >= 26.2 {
        return true;
        //? } else {
        /*return false;
        *///? }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        //? if minecraft: >= 26.2 {
        return List.of(
            "FeatureRendererDispatcherMixin",
            "SubmitNodeCollectionMixin",
            "SubmitNodeStorageMixin",
            "entity.SulfurCubeMixin"
        );
        //? } else {
        /*return null;
        *///? }
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
