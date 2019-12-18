package com.netcracker.frolic.cache;

import java.io.Serializable;
import java.util.Optional;

public interface Cache<K extends Serializable, V extends Identifiable<K>> {
    Optional<V> getById(K id);
    void saveOrUpdate(V object);
    void remove(K id);
    boolean containsObjectWithId(K id);
}