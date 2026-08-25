package games.enchanted.eg_particle_interactions.common.particle.variable.field;

import games.enchanted.eg_particle_interactions.common.particle.ParticleInteractionsParticle;

public class ParticleFields {
    public static final ParticleField<Float> SCALE = new ParticleField<>() {
        @Override
        public Float getInitial(ParticleInteractionsParticle particle) {
            return particle.getInitialAppearanceScale();
        }

        @Override
        public Float getCurrent(ParticleInteractionsParticle particle) {
            return particle.getScale();
        }

        @Override
        public void set(ParticleInteractionsParticle particle, Float value) {
            particle.setScale(value, true);
        }
    };

    public static final ParticleField<Float> ALPHA = new ParticleField<>() {
        @Override
        public Float getInitial(ParticleInteractionsParticle particle) {
            return particle.getInitialAppearanceAlpha();
        }

        @Override
        public Float getCurrent(ParticleInteractionsParticle particle) {
            return particle.getAlpha();
        }

        @Override
        public void set(ParticleInteractionsParticle particle, Float value) {
            particle.setAlpha(value, true);
        }
    };

    public static final ParticleField<Integer> LIGHT_EMISSION = new ParticleField<>() {
        @Override
        public Integer getInitial(ParticleInteractionsParticle particle) {
            return particle.getInitialAppearanceLightEmission();
        }

        @Override
        public Integer getCurrent(ParticleInteractionsParticle particle) {
            return particle.getLightEmission();
        }

        @Override
        public void set(ParticleInteractionsParticle particle, Integer value) {
            particle.setLightEmission(value);
        }
    };
}
