package games.enchanted.eg_particle_interactions.common.override_system;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import games.enchanted.eg_particle_interactions.common.ParticleInteractionsMod;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import net.minecraft.resources.Identifier;

import java.util.function.Function;

public record ParticleOrigin(Identifier id) {
    private static final BiMap<Identifier, ParticleOrigin> ORIGIN_BY_ID = HashBiMap.create();

    public static final Codec<ParticleOrigin> CODEC = ModCodecs.IDENTIFIER.xmap(
        id -> {
            if(!ORIGIN_BY_ID.containsKey(id)) {
                throw new RuntimeException("Invalid particle origin: '" + id + "'");
            }
            return ORIGIN_BY_ID.get(id);
        },
        origin -> {
            if(!ORIGIN_BY_ID.inverse().containsKey(origin)) {
                throw new RuntimeException("Tried to serialise unregistered particle origin");
            }
            return ORIGIN_BY_ID.inverse().get(origin);
        }
    );

    public static final ParticleOrigin DEFAULT = register(ParticleInteractionsMod.id("default"), ParticleOrigin::new);

    public static final ParticleOrigin BLOCK_PLACED = register(ParticleInteractionsMod.id("block/placed"), ParticleOrigin::new);
    public static final ParticleOrigin BLOCK_BROKEN = register(ParticleInteractionsMod.id("block/broken"), ParticleOrigin::new);
    public static final ParticleOrigin BLOCK_BRUSHED = register(ParticleInteractionsMod.id("block/brushed"), ParticleOrigin::new);
    public static final ParticleOrigin BLOCK_STRIPPED = register(ParticleInteractionsMod.id("block/stripped"), ParticleOrigin::new);
    public static final ParticleOrigin BLOCK_TILLED = register(ParticleInteractionsMod.id("block/tilled"), ParticleOrigin::new);
    public static final ParticleOrigin BLOCK_FLATTENED = register(ParticleInteractionsMod.id("block/flattened"), ParticleOrigin::new);
    public static final ParticleOrigin BLOCK_CRACK = register(ParticleInteractionsMod.id("block/crack"), ParticleOrigin::new);
    public static final ParticleOrigin FALLING_BLOCK_LANDED = register(ParticleInteractionsMod.id("block/falling_block_landed"), ParticleOrigin::new);
    public static final ParticleOrigin FALLING_BLOCK_FALLING = register(ParticleInteractionsMod.id("block/falling_block_falling"), ParticleOrigin::new);
    public static final ParticleOrigin FALLING_BLOCK_UNSTABLE = register(ParticleInteractionsMod.id("block/falling_block_unstable"), ParticleOrigin::new);
    public static final ParticleOrigin AMBIENT_LEAVES = register(ParticleInteractionsMod.id("block/ambient_leaves"), ParticleOrigin::new); // TODO
    public static final ParticleOrigin BLOCK_REDSTONE_INTERACTED_WITH = register(ParticleInteractionsMod.id("block/redstone_interacted_with"), ParticleOrigin::new);
    public static final ParticleOrigin BLOCK_WALKED_THROUGH = register(ParticleInteractionsMod.id("block/walked_through"), ParticleOrigin::new);
    public static final ParticleOrigin BLOCK_PARTICLE_OVERRIDDEN = register(ParticleInteractionsMod.id("block/generic"), ParticleOrigin::new); // TODO: add new origin for sprinting particles
    public static final ParticleOrigin BLOCK_SULFUR_CUBE_CONSUMED = register(ParticleInteractionsMod.id("block/sulfur_cube_consumed"), ParticleOrigin::new);

    public static final ParticleOrigin FLUID_PLACED = register(ParticleInteractionsMod.id("fluid/placed"), ParticleOrigin::new);
    public static final ParticleOrigin FLUID_WATER_ENTITY_ENTERED = register(ParticleInteractionsMod.id("fluid/water/entity_entered"), ParticleOrigin::new);
    public static final ParticleOrigin FLUID_WATER_ENTITY_ENTERED_BUBBLES = register(ParticleInteractionsMod.id("fluid/water/entity_entered_bubbles"), ParticleOrigin::new);

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
