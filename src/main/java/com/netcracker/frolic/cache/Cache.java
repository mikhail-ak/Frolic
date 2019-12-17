package com.netcracker.frolic.cache;

public interface Cache<K, V extends Identifiable<K>> {
    V getbyId(K id);
    void saveOrUpdate(V object);
    boolean containsObjectWithID(K id);
}