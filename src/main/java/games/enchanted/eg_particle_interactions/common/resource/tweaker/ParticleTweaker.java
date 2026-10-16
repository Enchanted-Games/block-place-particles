package games.enchanted.eg_particle_interactions.common.resource.tweaker;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.util.ObjectReference;
import net.minecraft.resources.Identifier;

public record ParticleTweaker(boolean disableEffect) {
    public static Codec<ParticleTweaker> codec() {
        return RecordCodecBuilder.create(i -> i
            .group(
                Codec.BOOL.fieldOf("disable").forGetter(ParticleTweaker::disableEffect)
            ).apply(
                i,
                ParticleTweaker::new
            )
        );
    }

    public static class Reference extends ObjectReference<ParticleTweaker> {
        public Reference(Identifier id) {
            super(id);
        }

        @Override
        protected ParticleTweaker lookupObject() {
            return ParticleTweakerManager.get(this.id());
        }
    }
}
