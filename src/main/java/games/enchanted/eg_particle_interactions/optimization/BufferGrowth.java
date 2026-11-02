package games.enchanted.eg_particle_interactions.optimization;

/** Reduces repeated array copying without increasing the initial buffer allocation. */
public final class BufferGrowth {
    private BufferGrowth() {}

    public static int nextCapacity(int oldCapacity) {
        long candidate = Math.max((long) oldCapacity + 2048L, oldCapacity + (long) oldCapacity / 2L);
        candidate = (candidate + 3L) & ~3L;
        // At VM array-size limits, use the original increment and failure behavior.
        if (candidate * 5L > Integer.MAX_VALUE - 8L) return oldCapacity + 2048;
        return (int) candidate;
    }
}
