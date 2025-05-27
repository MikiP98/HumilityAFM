package io.github.mikip98.humilityafm.datagen.language.util;

import java.util.HashMap;
import java.util.Map;

public class PrefixedHashMap extends HashMap<String, String> {

    protected final String prefix;
    public PrefixedHashMap(String prefix) {
        super();
        this.prefix = prefix;
    }

    @Override
    public String put(String key, String value) {
        return super.put(prefix + key, value);
    }
    public String putNoPrefix(String key, String value) {
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        for (Entry<? extends String, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
}