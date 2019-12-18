package com.netcracker.frolic.cache;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

public class FakeCache <K extends Serializable, V extends Identifiable<K>> implements Cache<K, V> {

    @Autowired
    SessionFactory sessionFactory;

    final Class<V> class_;

    FakeCache(Class<V> class_) {
        this.class_ = class_;
    }

    @Override
    public V getbyId(K id) {
        return sessionFactory.getCurrentSession().get(class_, id);
    }

    @Override
    public void saveOrUpdate(V object) {
        sessionFactory.getCurrentSession().saveOrUpdate(object);
    }

    @Override
    public boolean containsObjectWithId(K id) {
        return this.getbyId(id) != null;
    }
}