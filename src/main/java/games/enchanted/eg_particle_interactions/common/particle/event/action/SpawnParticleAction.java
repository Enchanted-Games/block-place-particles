package games.enchanted.eg_particle_interactions.common.particle.event.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.value.VelocityProvider;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitter;
import games.enchanted.eg_particle_interactions.common.particle.emitter.Emitters;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;
import org.joml.Vector3d;

public class SpawnParticleAction extends EventAction {
    public static final MapCodec<SpawnParticleAction> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            Emitters.CODEC.fieldOf("emitter").forGetter(SpawnParticleAction::getEmitter),
            VelocityProvider.CODEC.fieldOf("velocity_provider").forGetter(SpawnParticleAction::getVelocityProvider),
            ModCodecs.VECTOR3D.optionalFieldOf("pos_offset", new Vector3d(0)).forGetter(SpawnParticleAction::getPositionOffset)
        ).apply(
            i,
            SpawnParticleAction::new
        )
    );

    final Emitter emitter;
    final VelocityProvider velocityProvider;
    final Vector3d positionOffset;

    SpawnParticleAction(Emitter emitter, VelocityProvider velocityProvider, Vector3d positionOffset) {
        this.emitter = emitter;
        this.velocityProvider = velocityProvider;
        this.positionOffset = positionOffset;
    }

    protected Emitter getEmitter() {
        return this.emitter;
    }

    protected VelocityProvider getVelocityProvider() {
        return this.velocityProvider;
    }

    protected Vector3d getPositionOffset() {
        return this.positionOffset;
    }

    @Override
    public void onFire(ParticleInteractionsParticle particle) {
        particle.emit(this.emitter, this.positionOffset, this.velocityProvider);
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return CODEC;
    }
}
