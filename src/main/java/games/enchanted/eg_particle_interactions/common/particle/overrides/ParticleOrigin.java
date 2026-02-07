package games.enchanted.eg_particle_interactions.common.particle.overrides;

public record ParticleOrigin() {
    public static final ParticleOrigin BLOCK_PLACED = new ParticleOrigin();
    public static final ParticleOrigin BLOCK_BROKEN = new ParticleOrigin();
    public static final ParticleOrigin BLOCK_PARTICLE_OVERRIDDEN = new ParticleOrigin();
    public static final ParticleOrigin ITEM_PARTICLE_OVERRIDDEN = new ParticleOrigin();
    public static final ParticleOrigin BLOCK_BRUSHED = new ParticleOrigin();
    public static final ParticleOrigin BLOCK_CRACK = new ParticleOrigin();
    public static final ParticleOrigin FALLING_BLOCK_LANDED = new ParticleOrigin();
    public static final ParticleOrigin FALLING_BLOCK_FALLING = new ParticleOrigin();
    public static final ParticleOrigin BLOCK_INTERACTED_WITH = new ParticleOrigin();
    public static final ParticleOrigin BLOCK_WALKED_THROUGH = new ParticleOrigin();
}
