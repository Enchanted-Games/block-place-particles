package games.enchanted.eg_particle_interactions.common.particle.component;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.particle.component.type.FloatProviderComponent;
import games.enchanted.eg_particle_interactions.common.particle.component.type.IntProviderComponent;
import games.enchanted.eg_particle_interactions.common.particle.component.type.Vec3Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public class ParticleComponents {
    public static final ParticleComponentRegistry.ComponentReference<FloatProviderComponent> GRAVITY_INITIAL = register(
        ParticleInteractionsMod.id("gravity/initial"),
        FloatProviderComponent.CODEC,
        FloatProviderComponent.STREAM_CODEC
    );
    public static final ParticleComponentRegistry.ComponentReference<FloatProviderComponent> GRAVITY_DECAY = register(
        ParticleInteractionsMod.id("gravity/decay"),
        FloatProviderComponent.CODEC,
        FloatProviderComponent.STREAM_CODEC
    );
    public static final ParticleComponentRegistry.ComponentReference<IntProviderComponent> LIFETIME = register(
        ParticleInteractionsMod.id("lifetime"),
        IntProviderComponent.CODEC,
        IntProviderComponent.STREAM_CODEC
    );
    public static final ParticleComponentRegistry.ComponentReference<Vec3Component> VELOCITY_INITIAL_RANDOMNESS = register(
        ParticleInteractionsMod.id("velocity/initial_randomness"),
        Vec3Component.CODEC,
        Vec3Component.STREAM_CODEC
    );
    public static final ParticleComponentRegistry.ComponentReference<Vec3Component> VELOCITY_DECAY = register(
        ParticleInteractionsMod.id("velocity/decay"),
        Vec3Component.CODEC,
        Vec3Component.STREAM_CODEC
    );
    public static final ParticleComponentRegistry.ComponentReference<FloatProviderComponent> PHYSICS_COLLISION_SIZE = register(
        ParticleInteractionsMod.id("physics/collision_size"),
        FloatProviderComponent.CODEC,
        FloatProviderComponent.STREAM_CODEC
    );
    public static final ParticleComponentRegistry.ComponentReference<FloatProviderComponent> PHYSICS_FRICTION = register(
        ParticleInteractionsMod.id("physics/friction"),
        FloatProviderComponent.CODEC,
        FloatProviderComponent.STREAM_CODEC
    );
    public static final ParticleComponentRegistry.ComponentReference<FloatProviderComponent> PHYSICS_BOUNCINESS = register(
        ParticleInteractionsMod.id("physics/bounciness"),
        FloatProviderComponent.CODEC,
        FloatProviderComponent.STREAM_CODEC
    );
    public static final ParticleComponentRegistry.ComponentReference<FloatProviderComponent> PHYSICS_FLUID_DAMPEN = register(
        ParticleInteractionsMod.id("physics/fluid_dampen"),
        FloatProviderComponent.CODEC,
        FloatProviderComponent.STREAM_CODEC
    );

    public static <T> ParticleComponentRegistry.ComponentReference<T> register(
        Identifier id,
        Codec<T> codec,
        StreamCodec<? extends FriendlyByteBuf, T> streamCodec
    ) {
        return ParticleComponentRegistry.register(id, ParticleComponent.create(codec, streamCodec));
    }

    public static void init() {
    }
}
