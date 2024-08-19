package games.enchanted.blockplaceparticles.config.util;

import dev.isxander.yacl3.gui.utils.ItemRegistryHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class BlockItemUtil {
    public static BlockItem getDefaultedBlockItem(String identifier, BlockItem defaultItem) {
        Item itemFromIdentifier = ItemRegistryHelper.getItemFromName(identifier, defaultItem);
        if(itemFromIdentifier instanceof BlockItem) {
            return (BlockItem) itemFromIdentifier;
        }
        return defaultItem;
    }
}
