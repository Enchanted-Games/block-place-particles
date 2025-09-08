package games.enchanted.eg_particle_interactions.common.duck;

public interface ParticleAccess {
    void eg_particle_interactions$setBypassMovementCollisionCheck(boolean newValue);
    boolean eg_particle_interactions$getBypassMovementCollisionCheck();
    boolean eg_particle_interactions$isStoppedByCollision();
    void eg_particle_interactions$setHasStoppedByCollision(boolean newValue);
    void eg_particle_interactions$moveUpBecauseParticleLanded();
}
