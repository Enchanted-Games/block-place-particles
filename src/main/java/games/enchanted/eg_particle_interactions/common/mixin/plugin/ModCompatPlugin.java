package games.enchanted.eg_particle_interactions.common.mixin.plugin;

import games.enchanted.eg_particle_interactions.common.platform.PlatformHelper;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ModCompatPlugin implements IMixinConfigPlugin {
    private static final String mixinPackage = "games.enchanted.eg_particle_interactions.common.mixin.mod_compat.";

    private static String YACL_ID = "yet_another_config_lib_v3";
    protected boolean isYaclLoaded = false;

    @Override
    public void onLoad(String mixinPackage) {
        isYaclLoaded = PlatformHelper.isModLoadedEarly(YACL_ID);
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String modPackage = mixinClassName.replace(mixinPackage, "");
        if(modPackage.startsWith("yacl.") && isYaclLoaded) return true;
        return false;
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
}
