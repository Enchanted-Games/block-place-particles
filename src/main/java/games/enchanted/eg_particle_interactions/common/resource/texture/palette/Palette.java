package games.enchanted.eg_particle_interactions.common.resource.texture.palette;

import games.enchanted.eg_particle_interactions.common.util.ColourUtil;
import games.enchanted.eg_particle_interactions.common.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public record Palette(Entry[] entries, boolean cacheable) {
    public static final Palette BLANK = new Palette(new Entry[]{new Entry(-1)}, false);
    public static final Palette CACHEABLE_BLANK = new Palette(new Entry[]{new Entry(-1)}, true);

    Entry getRandomEntry() {
        return this.entries()[MathHelper.randomBetween(0, entries().length - 1)];
    }

    public int[] getRandomColour(int[] tintColour) {
        return ColourUtil.multiplyColours(this.getRandomEntry().argbAsArray(), tintColour);
    }

    public String debugEntriesString() {
        StringBuilder builder = new StringBuilder("(");
        for (Entry entry : this.entries()) {
            int[] argb = entry.argbAsArray();
            builder.append("[");
            builder.append(argb[0]);
            builder.append(", ");
            builder.append(argb[1]);
            builder.append(", ");
            builder.append(argb[2]);
            builder.append(", ");
            builder.append(argb[3]);
            builder.append("], ");
        }
        builder.append(")");
        return builder.toString();
    }

    public record Entry(int argb) {
        int[] argbAsArray() {
            return ColourUtil.ARGBint_to_ARGB(argb());
        }

        public static Entry[] argbIntsToEntries(List<Integer> argbInts) {
            List<Entry> entries = new ArrayList<>();
            for (Integer argb : argbInts) {
                entries.add(new Entry(argb));
            }
            return entries.toArray(Entry[]::new);
        }
    }
}
