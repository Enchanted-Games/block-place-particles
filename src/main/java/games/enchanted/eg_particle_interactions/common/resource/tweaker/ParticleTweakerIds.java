package games.enchanted.eg_particle_interactions.common.resource.tweaker;

import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public class ParticleTweakerIds {
    public static final Supplier<ParticleTweaker> SPECTRAL_ARROW_EFFECT = ref(Identifier.withDefaultNamespace("spectral_arrow_effect"));

    private static Supplier<ParticleTweaker> ref(Identifier id) {
        return () -> ParticleTweakerManager.get(id);
    }
}
