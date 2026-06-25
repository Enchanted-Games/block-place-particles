package games.enchanted.eg_particle_interactions.common.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {
    public static <K, V> Map<V, K> inverseMapEntries(Map<K, V> map) {
        Map<V, K> newMap = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            newMap.put(entry.getValue(), entry.getKey());
        }
        return newMap;
    }
}
