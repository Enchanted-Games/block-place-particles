package games.enchanted.blockplaceparticles.particle_spawning;

import games.enchanted.blockplaceparticles.util.MathHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SpawnParticlesUtil {
    public static boolean isParticleOutsideRenderDistance(@NotNull ParticleCategory particleCategory, BlockPos particlePos) {
        return isParticleOutsideRenderDistance(particleCategory, particlePos.getX(), particlePos.getY(), particlePos.getZ());
    }

    public static boolean isParticleOutsideRenderDistance(@NotNull ParticleCategory particleCategory, double x, double y, double z) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return false;

        double distanceFromPlayer = MathHelpers.getDistanceBetweenPoints(player.getX() / 16, player.getY() / 16, player.getZ() / 16, x / 16, y / 16, z / 16);
        double maxDistance = Math.min(particleCategory.getMaxDistance(), Minecraft.getInstance().options.renderDistance().get());

        return distanceFromPlayer >= maxDistance;
    }

    /**
     * Spawns a particle option in a flat circular shape
     *
     * @param particleOptions            particle options to spawn
     * @param level                      level
     * @param center                     center of the circle
     * @param amount                     amount of particles to spawn
     * @param spread                     how far particles can deviate from the radius (in blocks)
     * @param radius                     the distance to spawn particles from the center position (in blocks)
     * @param outwardVelocityMultiplier  how quickly particles should fly out from the center
     * @param verticalVelocityBase       base vertical velocity for all particles
     * @param verticalVelocityMultiplier multiplied by particle distance from the center
     */
    public static void spawnParticleInCircle(ParticleOptions particleOptions, ClientLevel level, Vec3 center, int amount, float spread, float radius, float outwardVelocityMultiplier, float verticalVelocityBase, float verticalVelocityMultiplier) {
        float randomAngleOffset = (float) Math.toRadians(MathHelpers.randomBetween(0, 360f));
        radius /= 2;
        for (int i = 0; i < amount; i++) {
            float progressRadians = (float) Math.toRadians(((float) i / amount) * 360f) + randomAngleOffset;
            double distX = (radius * Math.cos(progressRadians)) + MathHelpers.randomBetween(-(spread / 2), spread / 2);
            double distZ = (radius * Math.sin(progressRadians)) + MathHelpers.randomBetween(-(spread / 2), spread / 2);
            double x = center.x + distX;
            double z = center.z + distZ;
            double distFromCenter = Math.max(Math.abs(distX), Math.abs(distZ));
            level.addParticle(particleOptions, x, center.y, z, Math.clamp(distX, -1, 1) * outwardVelocityMultiplier, verticalVelocityBase + (Math.abs(radius + (spread / 2) - distFromCenter) * verticalVelocityMultiplier), Math.clamp(distZ, -1, 1) * outwardVelocityMultiplier);
        }
    }

    public static void spawnMostlyUpwardsMotionParticleOption(Level level, ParticleOptions particleOptions, double xPos, double yPos, double zPos, double velocityIntensity) {
        level.addParticle(
            particleOptions,
            xPos,
            yPos,
            zPos,
            (level.random.nextDouble() - 0.5) * velocityIntensity * 0.4,
            Math.abs((level.random.nextDouble() - 0.25) * velocityIntensity) + 0.25,
            (level.random.nextDouble() - 0.5) * velocityIntensity * 0.4
        );
    }
}
