package games.enchanted.eg_particle_interactions.common.particle.event.action;

import com.mojang.serialization.MapCodec;
import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class SetModelOffsetAction extends EventAction {
    public static final MapCodec<SetModelOffsetAction> CODEC = ExtraCodecs.VECTOR3F.fieldOf("value").xmap(
        SetModelOffsetAction::new,
        SetModelOffsetAction::getModifier
    );

    final Vector3f modifier;

    SetModelOffsetAction(Vector3fc modifier) {
        this.modifier = new Vector3f(modifier);
    }

    protected Vector3f getModifier() {
        return this.modifier;
    }

    @Override
    public void onFire(ParticleInteractionsParticle particle) {
        particle.setModelOffset(this.modifier);
    }

    @Override
    public MapCodec<? extends EventAction> codec() {
        return CODEC;
    }
}
