package com.netcracker.frolic.cache;

import org.apache.commons.collections4.map.LRUMap;

public class LRUCache <K, V extends Identifiable<K>> implements Cache<K, V> {

    private final LRUMap<K, V> cache;

    LRUCache(int capacity) {
        cache = new LRUMap<>(capacity);
    }

    @Override
    public V getbyId(K id) {
        return cache.get(id);
    }

    @Override
    public void saveOrUpdate(V object) {
        cache.put(object.getId(), object);
    }

    @Override
    public boolean containsObjectWithId(K id) {
        return cache.containsKey(id);
    }
}
