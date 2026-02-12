package games.enchanted.eg_particle_interactions.common.particle_overrides;

import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.particle.ModParticleTypes;
import games.enchanted.eg_particle_interactions.common.particle_overrides.manager.ParticleOverrideManager;
import net.minecraft.resources.Identifier;

import java.util.Map;

public class ParticleOverrides {
    public static final Identifier VANILLA = ParticleOverrideManager.registerOverride(
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "vanilla"),
        new ParticleOverride(
            new Emitter(ModParticleTypes.SOUL_SPARK_FLASH),
            Map.of()
        )
    );

    public static final Identifier SNOW_TEST = ParticleOverrideManager.registerOverride(
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "snow"),
        new ParticleOverride(
            new Emitter(ModParticleTypes.SNOWFLAKE),
            Map.of()
        )
    );

    public static final Identifier SPARK_TEST = ParticleOverrideManager.registerOverride(
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "spark"),
        new ParticleOverride(
            new Emitter(ModParticleTypes.SPARK_FLASH),
            Map.of()
        )
    );

    public static void init() {
    }
}
