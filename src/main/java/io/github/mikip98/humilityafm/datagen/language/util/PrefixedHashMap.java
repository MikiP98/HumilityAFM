package io.github.mikip98.humilityafm.datagen.language.util;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PrefixedHashMap<K, V> extends HashMap<K, V> {

    protected final String prefix;
    public PrefixedHashMap(String prefix) {
        super();
        this.prefix = prefix;
    }

    @Override
    public V put(K key,V value) {
        return super.put((K) (prefix + key), value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
}