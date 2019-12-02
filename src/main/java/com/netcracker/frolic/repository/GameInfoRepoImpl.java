package com.netcracker.frolic.repository;

import com.netcracker.frolic.entity.GameInfo;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("gameInfoRepo")
public class GameInfoRepoImpl implements GameInfoRepo {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public GameInfo save(GameInfo gameInfo) {
        sessionFactory.getCurrentSession().saveOrUpdate(gameInfo);
        return gameInfo;
    }

    @Transactional(readOnly = true)
    public GameInfo findByID(Long id) {
        return (GameInfo) sessionFactory.getCurrentSession()
                .getNamedQuery("GameInfo.findById")
                .setParameter("id", id).uniqueResult();
    }
}
