package com.netcracker.frolic.repository;

import com.netcracker.frolic.entity.Genre;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Optional;

@Transactional
@Repository("genreRepository")
@PropertySource("classpath:cache.properties")
public class GenreRepo {

    @Autowired
    private SessionFactory sessionFactory;

    @Value("${capacity}")
    private int cachingLimit;

    private Cache<Long, Genre> cache;

    public void saveOrUpdate(Genre entity) {
        Objects.requireNonNull(entity, "An attempt to save null entity");
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
        cache.saveOrUpdate(entity);    // не допустить хранение в кеше устаревших версий
    }

    public Optional<Genre> getById(Long id) {    // файла с данным ID может не быть ни в кеше, ни в БД
        if (cache.containsObjectWithID(id)) return Optional.of(cache.getByID(id));
        Genre possiblyPresentFile = sessionFactory.getCurrentSession().get(Genre.class, id);
        if (possiblyPresentFile != null) cache.saveOrUpdate(possiblyPresentFile);
        return Optional.ofNullable(possiblyPresentFile);
    }

    public void remove(Genre file) {
        sessionFactory.getCurrentSession().delete(file);
    }

    @PostConstruct
    private void cacheInit() {
        cache = new LRUCache<>(cachingLimit);
    }
}
