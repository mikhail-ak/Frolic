package com.netcracker.frolic.cache;

import org.apache.commons.collections4.map.LRUMap;

import java.io.Serializable;
import java.util.Optional;

public class LRUCache <K extends Serializable, V extends Identifiable<K>> implements Cache<K, V> {

    private final LRUMap<K, V> cache;

    public LRUCache(int capacity)
    { cache = new LRUMap<>(capacity); }

    public Optional<V> getById(K id)
    { return Optional.ofNullable(cache.get(id)); }

    public void saveOrUpdate(V object)
    { cache.put(object.getId(), object); }

    public void remove(K id)
    { cache.remove(id); }

    public boolean containsObjectWithId(K id) {
        return cache.containsKey(id);
    }
}
