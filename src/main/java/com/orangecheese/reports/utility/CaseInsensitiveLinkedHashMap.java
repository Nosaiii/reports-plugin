package com.orangecheese.reports.utility;

import java.util.LinkedHashMap;

public class CaseInsensitiveLinkedHashMap<V> extends LinkedHashMap<String, V> {
    @Override
    public V put(String key, V value) {
        return super.put(key.toLowerCase(), value);
    }

    @Override
    public V get(Object key) {
        return super.get(((String) key).toLowerCase());
    }

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(((String) key).toLowerCase());
    }
}