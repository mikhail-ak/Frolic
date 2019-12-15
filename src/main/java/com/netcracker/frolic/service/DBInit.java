package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.GameFile;
import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.entity.Genre;
import com.netcracker.frolic.entity.Rating;
import com.netcracker.frolic.repository.GameFileRepo;
import com.netcracker.frolic.repository.GameInfoRepo;
import com.netcracker.frolic.repository.GenreRepo;
import org.hibernate.engine.jdbc.BlobProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Service
public class DBInit {

    private static final Logger logger = LoggerFactory.getLogger(DBInit.class);

    @Autowired private GameFileRepo gameFileRepo;
    @Autowired private GameInfoRepo gameInfoRepo;
    @Autowired private GenreRepo genreRepo;


    @PostConstruct
    public void initDB() {
        logger.info("starting DB initialization now");

        GameFile gameFile = new GameFile(BlobProxy.generateProxy(new byte[] {}));
        GameInfo gameInfo = new GameInfo("Test game", gameFile, BigDecimal.TEN, new Rating());
        Genre genre = new Genre("FPS");
        gameFile.setInfo(gameInfo);
        genre.setInfo(gameInfo);
        genreRepo.saveOrUpdate(genre);
        gameFileRepo.saveOrUpdate(gameFile);
        gameInfoRepo.saveOrUpdate(gameInfo);
        genreRepo.saveOrUpdate(genre);

        logger.info("Database initialization finished.");
    }
}
