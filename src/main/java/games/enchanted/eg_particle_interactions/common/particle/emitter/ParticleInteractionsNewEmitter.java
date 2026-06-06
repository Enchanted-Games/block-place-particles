package games.enchanted.eg_particle_interactions.common.particle.emitter;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.component.ParticleComponentMap;
import games.enchanted.eg_particle_interactions.common.particle.definition.ParticleDefinition;
import games.enchanted.eg_particle_interactions.common.particle.definition.ParticleDefinitionManager;
import games.enchanted.eg_particle_interactions.common.particle.util.ParticleSpawner;
import org.joml.Vector3d;

public class ParticleInteractionsNewEmitter extends Emitter {
    public static final MapCodec<ParticleInteractionsNewEmitter> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            ModCodecs.COMPACT_VECTOR3D.optionalFieldOf(Emitter.VELOCITY_MULTIPLIER_NAME, Emitter.VELOCITY_MULTIPLIER_DEFAULT).forGetter(Emitter::getVelocityMultiplier),
            ParticleDefinitionManager.REFERENCE_CODEC.fieldOf("particle").forGetter(ParticleInteractionsNewEmitter::getParticleDefinition),
            ParticleComponentMap.CODEC.optionalFieldOf("components", ParticleComponentMap.EMPTY).forGetter(ParticleInteractionsNewEmitter::getCustomComponents)
        ).apply(
            instance,
            ParticleInteractionsNewEmitter::new
        )
    );

    final ParticleDefinition.Reference particleDefinition;
    final ParticleComponentMap customComponents;

    public ParticleInteractionsNewEmitter(Vector3d velocityMultiplier, ParticleDefinition.Reference particleDefinition, ParticleComponentMap customComponents) {
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

    public static ParticleInteractionsNewEmitter simple(ParticleDefinition definition) {
        return new ParticleInteractionsNewEmitter(
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
