package games.enchanted.eg_particle_interactions.common.particle.component;

import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.particle.component.type.*;
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
    public static final ParticleComponentRegistry.ComponentReference<FloatProviderComponent> BUOYANCY = register(
        ParticleInteractionsMod.id("physics/buoyancy"),
        FloatProviderComponent.CODEC,
        FloatProviderComponent.STREAM_CODEC
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
    public static final ParticleComponentRegistry.ComponentReference<FloatProviderComponent> PHYSICS_BOUNCINESS_DECAY = register(
        ParticleInteractionsMod.id("physics/bounciness_decay"),
        FloatProviderComponent.CODEC,
        FloatProviderComponent.STREAM_CODEC
    );
    public static final ParticleComponentRegistry.ComponentReference<BooleanComponent> PHYSICS_BYPASS_COLLISION_CHECK = register(
        ParticleInteractionsMod.id("physics/bypass_collision_check"),
        BooleanComponent.CODEC,
        BooleanComponent.STREAM_CODEC
    );
    public static final ParticleComponentRegistry.ComponentReference<WindConfigComponent> PHYSICS_WIND_CONFIG = register(
        ParticleInteractionsMod.id("physics/wind_config"),
        WindConfigComponent.CODEC,
        WindConfigComponent.STREAM_CODEC
    );
    public static final ParticleComponentRegistry.ComponentReference<IntangibleLayersComponent> PHYSICS_INTANGIBLE_LAYERS = register(
        ParticleInteractionsMod.id("physics/intangible_layers"),
        IntangibleLayersComponent.CODEC,
        IntangibleLayersComponent.STREAM_CODEC
    );
    public static final ParticleComponentRegistry.ComponentReference<AppearanceComponent> APPEARANCE = register(
        ParticleInteractionsMod.id("appearance"),
        AppearanceComponent.CODEC,
        AppearanceComponent.STREAM_CODEC
    );
    public static final ParticleComponentRegistry.ComponentReference<EventsComponent> EVENTS = register(
        ParticleInteractionsMod.id("lifetime_events"),
        EventsComponent.CODEC,
        EventsComponent.STREAM_CODEC
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
