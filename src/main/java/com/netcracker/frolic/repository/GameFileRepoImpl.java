package com.netcracker.frolic.repository;

import com.netcracker.frolic.entity.GameFile;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("gameFileRepo")
public class GameFileRepoImpl implements GameFileRepo {

    private SessionFactory sessionFactory;

    @Override
    public GameFile save(GameFile gameFile) {
        sessionFactory.getCurrentSession().saveOrUpdate(gameFile);
        return gameFile;
    }
}
