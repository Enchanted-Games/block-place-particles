package games.enchanted.eg_particle_interactions.common.particle_spawning;

import games.enchanted.eg_particle_interactions.common.particle.ParticleContext;
import games.enchanted.eg_particle_interactions.common.particle.options.PIParticleOptions;
import games.enchanted.eg_particle_interactions.common.particle.util.ParticleSpawner;
import games.enchanted.eg_particle_interactions.common.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SpawnParticlesUtil {
    public static boolean isParticleOutsideRenderDistance(@NotNull ParticleCategory particleCategory, BlockPos particlePos) {
        return isParticleOutsideRenderDistance(particleCategory, particlePos.getX(), particlePos.getY(), particlePos.getZ());
    }

    public static boolean isParticleOutsideRenderDistance(@NotNull ParticleCategory particleCategory, double x, double y, double z) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return false;

        double distanceFromPlayer = MathHelper.getDistanceBetweenPoints(player.getX() / 16, player.getY() / 16, player.getZ() / 16, x / 16, y / 16, z / 16);
        double maxDistance = Math.min(particleCategory.getMaxDistance(), Minecraft.getInstance().options.renderDistance().get());

        return distanceFromPlayer >= maxDistance;
    }

    @FunctionalInterface
    public interface ParticleAdder {
        void spawn(double x, double y, double z, double xSpeed, double ySpeed, double zSpeed);
    }

    /**
     * Spawns a particle option in a flat circular shape
     *
     * @param particleOptions            particle options to spawn
     * @param context                    context
     * @param center                     center of the circle
     * @param amount                     amount of particles to spawn
     * @param spread                     how far particles can deviate from the radius (in blocks)
     * @param radius                     the distance to spawn particles from the center position (in blocks)
     * @param outwardVelocityMultiplier  how quickly particles should fly out from the center
     * @param verticalVelocityBase       base vertical velocity for all particles
     * @param verticalVelocityMultiplier multiplied by particle distance from the center
     */
    public static void spawnParticleInCircle(PIParticleOptions particleOptions, ParticleContext context, Vec3 center, int amount, float spread, float radius, float outwardVelocityMultiplier, float verticalVelocityBase, float verticalVelocityMultiplier) {
        spawnParticleInCircle(
            (x, y, z, xSpeed, ySpeed, zSpeed) -> {
                ParticleSpawner.spawnWithDefaultAppearance(
                    particleOptions,
                    context,
                    x,
                    y,
                    z,
                    xSpeed,
                    ySpeed,
                    zSpeed
                );
            },
            center,
            amount,
            spread,
            radius,
            outwardVelocityMultiplier,
            verticalVelocityBase,
            verticalVelocityMultiplier
        );
    }

    /**
     * Spawns a particle option in a flat circular shape
     *
     * @param adder                      spawn a particle from position and velocity
     * @param center                     center of the circle
     * @param amount                     amount of particles to spawn
     * @param spread                     how far particles can deviate from the radius (in blocks)
     * @param radius                     the distance to spawn particles from the center position (in blocks)
     * @param outwardVelocityMultiplier  how quickly particles should fly out from the center
     * @param verticalVelocityBase       base vertical velocity for all particles
     * @param verticalVelocityMultiplier multiplied by particle distance from the center
     */
    public static void spawnParticleInCircle(ParticleAdder adder, Vec3 center, int amount, float spread, float radius, float outwardVelocityMultiplier, float verticalVelocityBase, float verticalVelocityMultiplier) {
        float randomAngleOffset = (float) Math.toRadians(MathHelper.randomBetween(0, 360f));
        radius /= 2;
        for (int i = 0; i < amount; i++) {
            float progressRadians = (float) Math.toRadians(((float) i / amount) * 360f) + randomAngleOffset;
            double distX = (radius * Math.cos(progressRadians)) + MathHelper.randomBetween(-(spread / 2), spread / 2);
            double distZ = (radius * Math.sin(progressRadians)) + MathHelper.randomBetween(-(spread / 2), spread / 2);
            double x = center.x + distX;
            double z = center.z + distZ;
            double distFromCenter = Math.max(Math.abs(distX), Math.abs(distZ));
            adder.spawn(
                x,
                center.y,
                z,
                Math.clamp(distX, -1, 1) * outwardVelocityMultiplier,
                verticalVelocityBase + (Math.abs(radius + (spread / 2) - distFromCenter) * verticalVelocityMultiplier),
                Math.clamp(distZ, -1, 1) * outwardVelocityMultiplier
            );
        }
    }

    public static void spawnMostlyUpwardsMotionParticleOption(ParticleContext context, PIParticleOptions particleOptions, double xPos, double yPos, double zPos, double velocityIntensity) {
        RandomSource random = context.level().getRandom();
        ParticleSpawner.spawnWithDefaultAppearance(
            particleOptions,
            context,
            xPos,
            yPos,
            zPos,
            (random.nextDouble() - 0.5) * velocityIntensity * 0.4,
            Math.abs((random.nextDouble() - 0.25) * velocityIntensity) + 0.25,
            (random.nextDouble() - 0.5) * velocityIntensity * 0.4
        );
    }
}
