package com.netcracker.frolic.repository;

import com.netcracker.frolic.entity.GameFile;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

@Transactional
@Repository("gameFileRepository")
@PropertySource("classpath:cache.properties")
public class GameFileRepo {

    @Autowired
    private SessionFactory sessionFactory;

    @Value("${capacity}")
    private int cachingLimit;

    private Cache<Long, GameFile> cache;

    public void saveOrUpdate(GameFile entity) {
        Objects.requireNonNull(entity, "An attempt to save null entity");
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
        cache.saveOrUpdate(entity);    // не допустить хранение в кеше устаревших версий
    }

    public Optional<GameFile> getById(Long id) {    // файла с данным ID может не быть ни в кеше, ни в БД
        if (cache.containsObjectWithID(id)) return Optional.of(cache.getByID(id));
        GameFile possiblyPresentFile = sessionFactory.getCurrentSession().get(GameFile.class, id);
        if (possiblyPresentFile != null) cache.saveOrUpdate(possiblyPresentFile);
        return Optional.ofNullable(possiblyPresentFile);
    }

    public void remove(GameFile file) {
        sessionFactory.getCurrentSession().delete(file);
    }

    @PostConstruct
    private void cacheInit() {
        cache = new LRUCache<>(cachingLimit);
    }
}