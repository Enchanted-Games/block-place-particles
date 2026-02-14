package games.enchanted.eg_particle_interactions.common.override_system.override;

import games.enchanted.eg_particle_interactions.common.Constants;
import games.enchanted.eg_particle_interactions.common.override_system.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.ModParticleTypes;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ParticleOverrides {
    private static final Map<Identifier, ParticleOverride> OVERRIDE_BY_ID = new HashMap<>();

    public static final Identifier VANILLA = registerOverride(
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "vanilla"),
        new ParticleOverride(
            new Emitter(ModParticleTypes.SOUL_SPARK_FLASH),
            Map.of()
        )
    );

    public static final Identifier EMPTY = registerOverride(
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "empty"),
        new ParticleOverride(
            Emitter.EMPTY,
            Map.of()
        )
    );

    public static final Identifier SNOW_TEST = registerOverride(
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "snow"),
        new ParticleOverride(
            new Emitter(ModParticleTypes.SNOWFLAKE, 0.2),
            Map.of()
        )
    );

    public static final Identifier SPARK_TEST = registerOverride(
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "spark"),
        new ParticleOverride(
            new Emitter(ModParticleTypes.SPARK_FLASH, 0.1),
            Map.of()
        )
    );

    public static Identifier registerOverride(Identifier id, ParticleOverride override) {
        OVERRIDE_BY_ID.put(id, override);
        return id;
    }

    public static ParticleOverride getOverrideFromId(Identifier id) {
        ParticleOverride override = OVERRIDE_BY_ID.get(id);
        if(override == null) {
            throw new IllegalStateException("Tried to get unregistered particle override '" + id + "'");
        }
        return override;
    }

    static void clearOverrides() {
        OVERRIDE_BY_ID.clear();
    }

    public static void init() {
    }
}
