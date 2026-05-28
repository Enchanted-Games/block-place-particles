package games.enchanted.eg_particle_interactions.common.particle.component.type;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearance;
import games.enchanted.eg_particle_interactions.common.particle.appearance.ParticleAppearanceManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record AppearanceComponent(ParticleAppearance.Reference value) {
    public static final Codec<AppearanceComponent> CODEC = ParticleAppearanceManager.referenceCodec().xmap(
        AppearanceComponent::new,
        AppearanceComponent::value
    );
    public static final StreamCodec<FriendlyByteBuf, AppearanceComponent> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.fromCodec(ParticleAppearanceManager.referenceCodec()),
        AppearanceComponent::value,
        AppearanceComponent::new
    );
}
