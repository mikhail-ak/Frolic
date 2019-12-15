package com.netcracker.frolic.repository;

import org.apache.commons.collections4.map.LRUMap;

public class LRUCache<K, V extends Identifiable<K>> implements Cache<K, V> {

    private final LRUMap<K, V> cache;

    LRUCache(int capacity) {
        cache = new LRUMap<>(capacity);
    }

    @Override
    public V getByID(K id) {
        return cache.get(id);
    }

    @Override
    public void saveOrUpdate(V object) {
        cache.put(object.getID(), object);
    }

    @Override
    public boolean containsObjectWithID(K id) {
        return cache.containsKey(id);
    }
}
