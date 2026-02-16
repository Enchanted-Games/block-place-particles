package games.enchanted.eg_particle_interactions.common.particle.overrides;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import net.minecraft.resources.Identifier;

import java.util.Objects;
import java.util.function.Function;

public record ParticleOrigin(Identifier id) {
    private static final BiMap<Identifier, ParticleOrigin> ORIGIN_BY_ID = HashBiMap.create();

    public static final Codec<ParticleOrigin> CODEC = Identifier.CODEC.xmap(
        id -> Objects.requireNonNull(ORIGIN_BY_ID.get(id), "Invalid particle origin: '" + id + "'"),
        origin -> Objects.requireNonNull(ORIGIN_BY_ID.inverse().get(origin), "Tried to serialise unregistered particle origin")
    );

    public static final ParticleOrigin BLOCK_PLACED = register(ParticleInteractionsMod.id("block/placed"), ParticleOrigin::new);
    public static final ParticleOrigin BLOCK_BROKEN = register(ParticleInteractionsMod.id("block/broken"), ParticleOrigin::new);
    public static final ParticleOrigin BLOCK_BRUSHED = register(ParticleInteractionsMod.id("block/brushed"), ParticleOrigin::new);
    public static final ParticleOrigin BLOCK_CRACK = register(ParticleInteractionsMod.id("block/crack"), ParticleOrigin::new);
    public static final ParticleOrigin FALLING_BLOCK_LANDED = register(ParticleInteractionsMod.id("block/falling_block_landed"), ParticleOrigin::new);
    public static final ParticleOrigin FALLING_BLOCK_FALLING = register(ParticleInteractionsMod.id("block/falling_block_falling"), ParticleOrigin::new);
    public static final ParticleOrigin BLOCK_INTERACTED_WITH = register(ParticleInteractionsMod.id("block/interacted_with"), ParticleOrigin::new);
    public static final ParticleOrigin BLOCK_WALKED_THROUGH = register(ParticleInteractionsMod.id("block/walked_through"), ParticleOrigin::new);
    public static final ParticleOrigin BLOCK_PARTICLE_OVERRIDDEN = register(ParticleInteractionsMod.id("block/generic"), ParticleOrigin::new);

    public static final ParticleOrigin ITEM_PARTICLE_OVERRIDDEN = register(ParticleInteractionsMod.id("item/generic"), ParticleOrigin::new);

    private static ParticleOrigin register(Identifier id, Function<Identifier, ParticleOrigin> originCreator) {
        if(ORIGIN_BY_ID.containsKey(id)) {
            throw new IllegalStateException("Tried to register particle origin '" + id + "', but a particle origin with that id already exists");
        }
        ParticleOrigin origin = originCreator.apply(id);
        ORIGIN_BY_ID.put(id, origin);
        return origin;
    }
}
