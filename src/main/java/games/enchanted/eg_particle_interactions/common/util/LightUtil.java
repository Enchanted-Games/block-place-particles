package games.enchanted.eg_particle_interactions.common.util;

//? if minecraft: < 26.1 {
import net.minecraft.client.renderer.LightTexture;
//? } else {
/*import net.minecraft.util.LightCoordsUtil;
 *///? }

public class LightUtil {
    public static final int FULL_BRIGHT =
        //? if minecraft: < 26.1 {
            LightTexture.FULL_BRIGHT
        //? } else {
            /*LightCoordsUtil.FULL_BRIGHT
        *///? }
    ;

    public static int pack(int block, int sky) {
        return
            //? if minecraft: < 26.1 {
            LightTexture.pack(block, sky)
             //? } else {
            /*LightCoordsUtil.pack(block, sky)
            *///? }
        ;
    }
}
