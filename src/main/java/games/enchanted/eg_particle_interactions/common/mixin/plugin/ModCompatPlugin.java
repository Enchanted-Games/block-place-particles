package games.enchanted.eg_particle_interactions.common.mixin.plugin;

import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ModCompatPlugin implements IMixinConfigPlugin {
    private static String mixinPackagePrefix;

    private static final ModPackage YACL_PACKAGE = new ModPackage("yet_another_config_lib_v3", "yacl");
    protected boolean isYaclLoaded = false;

    @Override
    public void onLoad(String mixinPackage) {
        isYaclLoaded = PlatformHelper.isModLoadedEarly(YACL_PACKAGE.modId());
        mixinPackagePrefix = mixinPackage + ".";
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if(shouldApplyPackage(mixinClassName, YACL_PACKAGE) && isYaclLoaded) return true;
        return false;
    }

    private boolean shouldApplyPackage(String mixinClassName, ModPackage modPackage) {
        String modPackageName = mixinClassName.replace(mixinPackagePrefix, "");
        return modPackage.packageMatches(modPackageName);
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
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    record ModPackage(String modId, String packageName) {
        boolean packageMatches(String packageName) {
            return packageName.startsWith(this.packageName + ".");
        }
    }
}
