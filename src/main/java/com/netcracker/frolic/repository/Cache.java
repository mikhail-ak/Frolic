package com.netcracker.frolic.repository;

public interface Cache<K, V extends Identifiable<K>> {
    V getByID(K id);
    void saveOrUpdate(V object);
    boolean containsObjectWithID(K id);
}