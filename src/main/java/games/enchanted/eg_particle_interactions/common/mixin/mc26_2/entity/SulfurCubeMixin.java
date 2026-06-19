//? if minecraft: >= 26.2 {
package games.enchanted.eg_particle_interactions.common.mixin.mc26_2.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import games.enchanted.eg_particle_interactions.common.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SulfurCube.class)
public abstract class SulfurCubeMixin extends AbstractCubeMob {
    protected SulfurCubeMixin(EntityType<? extends AbstractCubeMob> type, Level level) {
        super(type, level);
    }

    @WrapMethod(
        method = "equipItem"
    )
    private boolean eg_particle_interactions$wrapEquipItem(ItemStack heldItem, Operation<Boolean> original) {
        boolean didWork = original.call(heldItem);
        if(!didWork) return false;

        if(!(this.level() instanceof ClientLevel clientLevel)) return didWork;

        if(heldItem.getItem() instanceof BlockItem blockItem) {
            SpawnParticles.spawnSulfurCubeConsumeParticles(
                clientLevel,
                (SulfurCube) (Object) this,
                blockItem.getBlock().defaultBlockState()
            );
        }

        return didWork;
    }
}
//? }
