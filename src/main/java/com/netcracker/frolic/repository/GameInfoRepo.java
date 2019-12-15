package com.netcracker.frolic.repository;

import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.entity.Genre;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional
@Repository("gameInfoRepository")
@PropertySource("classpath:cache.properties")
public class GameInfoRepo {
//TODO вынести общие для репозиториев методы в абстрактный дженерик класс, от которого они будут наследовать

    private static final Logger logger = LoggerFactory.getLogger(GameInfoRepo.class);

    @Autowired private SessionFactory sessionFactory;

    @Value("${capacity}")
    private int cachingLimit;

    private Cache<Long, GameInfo> cache;

    public List<GameInfo> findAllByGenre(Genre genre, int offset, int resultsPerPage) {
        final String QUERY = "SELECT info FROM GameInfo info JOIN info.genres genre WHERE genre.id = :genreID";
        List<GameInfo> result = null;
        try (Session session = sessionFactory.openSession()) {
            Query<GameInfo> query = (Query<GameInfo>) session.createQuery(QUERY, GameInfo.class)
                    .setParameter("genreID", genre.getID())
                    .setFirstResult(offset)
                    .setMaxResults(resultsPerPage);
            result = query.list();
        } catch (HibernateException exception) {
            logger.error("Failed to retrieve GameInfos by Genre using the following HQL query: " + QUERY);
            exception.printStackTrace();
        }
        return (result == null) ? new ArrayList<>() : result;
    }

    public void saveOrUpdate(GameInfo entity) {
        Objects.requireNonNull(entity, "An attempt to save null entity");
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
        cache.saveOrUpdate(entity);    // не допустить хранение в кеше устаревших версий
    }

    public Optional<GameInfo> getById(Long id) {    // файла с данным ID может не быть ни в кеше, ни в БД
        if (cache.containsObjectWithID(id)) return Optional.of(cache.getByID(id));
        GameInfo possiblyPresentFile = sessionFactory.getCurrentSession().get(GameInfo.class, id);
        if (possiblyPresentFile != null) cache.saveOrUpdate(possiblyPresentFile);
        return Optional.ofNullable(possiblyPresentFile);
    }

    public void remove(GameInfo file) {
        sessionFactory.getCurrentSession().delete(file);
    }

    @PostConstruct
    private void cacheInit() {
        cache = new LRUCache<>(cachingLimit);
    }
}