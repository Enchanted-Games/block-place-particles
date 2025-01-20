package games.enchanted.blockplaceparticles.util;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TextUtil {
    /**
     * Takes a resource location and formats it into a {@link Component}
     *
     * @param location the tag location
     * @param prefix a string to append to the start of the namespace
     */
    public static Component formatResourceLocationToChatComponent(ResourceLocation location, String prefix) {
        String[] seperatedString = location.toString().split(":");
        if(seperatedString.length == 1) return Component.literal(seperatedString[0]);

        return Component.empty()
            .append(
                Component.literal(prefix + seperatedString[0] + ":").withColor(0xbababa)
            ).append(
                Component.literal(seperatedString[1])
            );
    }

    /**
     * Takes a resource location and formats it into a {@link Component}
     *
     * @param location the tag location
     */
    public static Component formatResourceLocationToChatComponent(ResourceLocation location) {
        return formatResourceLocationToChatComponent(location, "");
    }
}
