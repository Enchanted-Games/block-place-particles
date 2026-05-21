package games.enchanted.eg_particle_interactions.common.particle.event.action.lifetime;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import games.enchanted.eg_particle_interactions.common.codecs.ModCodecs;
import games.enchanted.eg_particle_interactions.common.particle.event.action.EventAction;
import games.enchanted.eg_particle_interactions.common.particle.types.ParticleInteractionsParticle;
import org.joml.Vector3d;

public class ModifyVelocityAction extends EventAction {
    private static final Vector3d ONE = new Vector3d(1);

    public static final MapCodec<ModifyVelocityAction> CODEC = RecordCodecBuilder.mapCodec(i ->
        i.group(
            ModCodecs.VECTOR3D.optionalFieldOf("divide", ONE).forGetter(ModifyVelocityAction::getDivision),
            ModCodecs.VECTOR3D.optionalFieldOf("multiplication", ONE).forGetter(ModifyVelocityAction::getMultiplication),
            ModCodecs.VECTOR3D.optionalFieldOf("addition", ONE).forGetter(ModifyVelocityAction::getAddition),
            ModCodecs.VECTOR3D.optionalFieldOf("subtraction", ONE).forGetter(ModifyVelocityAction::getSubtraction)
        ).apply(
            i,
            ModifyVelocityAction::new
        )
    );

    final Vector3d division;
    final Vector3d multiplication;
    final Vector3d addition;
    final Vector3d subtraction;

    ModifyVelocityAction(Vector3d division, Vector3d multiplication, Vector3d addition, Vector3d subtraction) {
        this.division = division;
        this.multiplication = multiplication;
        this.addition = addition;
        this.subtraction = subtraction;
    }

    protected Vector3d getSubtraction() {
        return subtraction;
    }

    protected Vector3d getAddition() {
        return addition;
    }

    protected Vector3d getMultiplication() {
        return multiplication;
    }

    protected Vector3d getDivision() {
        return division;
    }

    @Override
    public void onFire(ParticleInteractionsParticle particle) {
        particle.modifyVelocity(this.division, this.multiplication, this.addition, this.subtraction);
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return CODEC;
    }
}
