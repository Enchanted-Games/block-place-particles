package games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {
    @Accessor("particleResources")
    ParticleResources eg_particle_interactions$getParticleResources();

    @Accessor("resourceManager")
    ReloadableResourceManager eg_particle_interactions$getResourceManager();
}
