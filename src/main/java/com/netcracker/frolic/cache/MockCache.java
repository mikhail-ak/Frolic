package com.netcracker.frolic.cache;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Optional;

/**
 * Мок кеша на случай если кеширование отключено
 */
public class MockCache<K extends Serializable, V extends Identifiable<K>> implements Cache<K, V> {

    @Autowired
    SessionFactory sessionFactory;

    final Class<V> class_;

    public MockCache(Class<V> class_, int capacity)
    { this.class_ = class_; }

    public Optional<V> getById(K id) {
        return Optional.ofNullable(
                sessionFactory.getCurrentSession().get(class_, id));
    }

    public void saveOrUpdate(V object) {
        sessionFactory.getCurrentSession().saveOrUpdate(object);
    }

    public void remove(K id) { }

    public boolean containsObjectWithId(K id) {
        return this.getById(id) != null;
    }
}