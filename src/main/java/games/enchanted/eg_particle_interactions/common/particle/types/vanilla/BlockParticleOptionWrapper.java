package games.enchanted.eg_particle_interactions.common.particle.types.vanilla;

import games.enchanted.eg_particle_interactions.common.mixin.client.accessor.client.MinecraftAccessor;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.types.options.SimpleParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.types.PIParticleProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Supplier;

public class BlockParticleOptionWrapper implements PIParticleProvider<SimpleParticleOptions> {
    final Supplier<ParticleType<BlockParticleOption>> dustType;

    public BlockParticleOptionWrapper(Supplier<ParticleType<BlockParticleOption>> dustType) {
        this.dustType = dustType;
    }

    private ParticleProvider<BlockParticleOption> getProvider() {
        ParticleProvider<?> provider = ((MinecraftAccessor) Minecraft.getInstance()).eg_particle_interactions$getParticleResources()
            .getProviders().get(BuiltInRegistries.PARTICLE_TYPE.getId(this.dustType.get()));
        try {
            //noinspection unchecked
            return (ParticleProvider<BlockParticleOption>) provider;
        } catch (Exception e) {
            throw new IllegalStateException("Somehow got a particle provider of incorrect type '" + Arrays.toString(provider.getClass().getTypeParameters()) + "'. Expected ParticleProvider<BlockParticleOption>");
        }
    }

    @Override
    public @Nullable Particle createParticle(
        SimpleParticleOptions options,
        ParticleComponentMap components,
        ParticleAppearance appearance,
        ParticleContext context,
        double x,
        double z,
        double y,
        double xSpeed,
        double ySpeed,
        double zSpeed
    ) {
        ParticleContext.BlockContext blockContext = context.blockContext();
        if (blockContext == null) return null;
        BlockParticleOption option = new BlockParticleOption(this.dustType.get(), blockContext.state());
        ClientLevel level = context.level();
        ParticleProvider<BlockParticleOption> provider = this.getProvider();
        if (provider == null) return null;
        return this.getProvider().createParticle(option, level, x, y, z, xSpeed, ySpeed, zSpeed, level.getRandom());
    }
}
