package games.enchanted.eg_particle_interactions.common.debug;

import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ParticleDebugShapes {
    public static final int PARTICLE_TICK_POSITION = 0xff00ffa2;
    public static final int PARTICLE_RENDER_POSITION = 0xffff6f00;
    public static final int PARTICLE_PREV_RENDER_POSITION = 0xff823800;

    public static final int EMITTER_BOUNDING_BOX = 0xfff382ff;

    public static final int PARTICLE_BOUNDING_BOX = 0xff44ff3b;
    public static final int PARTICLE_BOUNDING_BOX_STOPPED = 0xff217a1c;
    public static final int PARTICLE_CULLING_BOX = 0xffff3b3b;

    public static void particlePosition(double x, double y, double z, int argb) {
        Gizmos.point(new Vec3(x, y, z), argb, 5);
    }

    public static void box(AABB aabb, int argb) {
        Gizmos.cuboid(aabb, GizmoStyle.stroke(argb, 2));
    }

    public static void onGroundMarker(AABB aabb, boolean onGround) {
        Gizmos.point(aabb.getCenter().add(new Vec3(0, aabb.getYsize(), 0)), onGround ? 0xffbb00bb : 0xffffccff, 8);
    }
}
