package games.enchanted.eg_particle_interactions.common.particle.emitter;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.definition.ParticleDefinition;
import games.enchanted.eg_particle_interactions.common.particle.definition.ParticleDefinitionManager;
import games.enchanted.eg_particle_interactions.common.particle.ParticleSpawner;
import org.joml.Vector3d;

public class ParticleInteractionsEmitter extends Emitter {
    public static final MapCodec<ParticleInteractionsEmitter> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            ModCodecs.COMPACT_VECTOR3D.optionalFieldOf(Emitter.VELOCITY_MULTIPLIER_NAME, Emitter.VELOCITY_MULTIPLIER_DEFAULT).forGetter(Emitter::getVelocityMultiplier),
            ParticleDefinitionManager.REFERENCE_CODEC.fieldOf("particle").forGetter(ParticleInteractionsEmitter::getParticleDefinition),
            ParticleComponentMap.CODEC.optionalFieldOf("components", ParticleComponentMap.EMPTY).forGetter(ParticleInteractionsEmitter::getCustomComponents)
        ).apply(
            instance,
            ParticleInteractionsEmitter::new
        )
    );

    final ParticleDefinition.Reference particleDefinition;
    final ParticleComponentMap customComponents;

    public ParticleInteractionsEmitter(Vector3d velocityMultiplier, ParticleDefinition.Reference particleDefinition, ParticleComponentMap customComponents) {
        super(velocityMultiplier);
        this.particleDefinition = particleDefinition;
        this.customComponents = customComponents;
    }

    @Override
    public void spawnParticle(ParticleContext context, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        ParticleSpawner.spawn(
            this.particleDefinition.get(),
            this.customComponents,
            context,
            x,
            y,
            z,
            xSpeed * this.getVelocityMultiplier().x(),
            ySpeed * this.getVelocityMultiplier().y(),
            zSpeed * this.getVelocityMultiplier().z()
        );
    }

    public static ParticleInteractionsEmitter simple(ParticleDefinition definition) {
        return new ParticleInteractionsEmitter(
            VELOCITY_MULTIPLIER_DEFAULT,
            new ParticleDefinition.InlineRef(definition),
            ParticleComponentMap.EMPTY
        );
    }

    @Override
    public MapCodec<? extends Emitter> codec() {
        return CODEC;
    }

    protected ParticleDefinition.Reference getParticleDefinition() {
        return this.particleDefinition;
    }

    protected ParticleComponentMap getCustomComponents() {
        return this.customComponents;
    }
}
