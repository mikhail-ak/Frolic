package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.GameFile;
import com.netcracker.frolic.entity.GameInfo;
import org.hibernate.engine.jdbc.BlobProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Service
public class DbInit {

    private static final Logger logger = LoggerFactory.getLogger(DbInit.class);

    @Autowired private GameFileService gameFileService;
    @Autowired private GameInfoService gameInfoService;

    @PostConstruct
    public void initDb() {
        logger.info("starting DB initialization now");

        GameFile gameFile = new GameFile(BlobProxy.generateProxy(new byte[] {}));
        GameInfo gameInfo = new GameInfo();
        gameInfo.setTitle("Grant Heft Auto");
        gameInfo.setFile(gameFile);
        gameInfo.setPricePerDay(new BigDecimal("4.44"));
        gameInfo.setGenre(GameInfo.Genre.FIRST_PERSON_SHOOTER);
        gameFile.setInfo(gameInfo);

        logger.info("Database initialization finished.");
    }
}