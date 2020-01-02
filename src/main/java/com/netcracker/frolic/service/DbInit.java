package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.GameFile;
import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.entity.Rating;
import org.hibernate.engine.jdbc.BlobProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class DbInit {

    private static final Logger logger = LoggerFactory.getLogger(DbInit.class);

    @Autowired private GameFileService gameFileService;
    @Autowired private GameInfoService gameInfoService;

    @PostConstruct
    public void initDb() {
        logger.info("starting DB initialization now");

        GameFile gameFile1 = new GameFile(BlobProxy.generateProxy(new byte[] {}));
        GameInfo gameInfo1 = new GameInfo();
        gameInfo1.setTitle("Grant Heft Auto");
        gameInfo1.setFile(gameFile1);
        gameInfo1.setPricePerDay(new BigDecimal("4.44"));
        gameInfo1.setGenre(GameInfo.Genre.FIRST_PERSON_SHOOTER);
        gameFile1.setInfo(gameInfo1);
        gameInfo1.setRating(new Rating(1100, 22));
        gameInfo1.setDescription("Some description");
        gameInfo1.setReleaseDate(LocalDate.of(1993, 11, 22));
        gameFileService.save(gameFile1);
        gameInfoService.save(gameInfo1);

        GameFile gameFile2 = new GameFile(BlobProxy.generateProxy(new byte[] {}));
        GameInfo gameInfo2 = new GameInfo();
        gameInfo2.setTitle("Grant Heft Auto 2 ");
        gameInfo2.setFile(gameFile2);
        gameInfo2.setPricePerDay(new BigDecimal("4.11"));
        gameInfo2.setGenre(GameInfo.Genre.FIRST_PERSON_SHOOTER);
        gameFile2.setInfo(gameInfo2);
        gameInfo2.setRating(new Rating(1800, 19));
        gameInfo2.setDescription("Some description");
        gameInfo2.setReleaseDate(LocalDate.of(1984, 2, 18));
        gameFileService.save(gameFile2);
        gameInfoService.save(gameInfo2);

        GameFile gameFile3 = new GameFile(BlobProxy.generateProxy(new byte[] {}));
        GameInfo gameInfo3 = new GameInfo();
        gameInfo3.setTitle("Grant Heft Auto 3");
        gameInfo3.setFile(gameFile3);
        gameInfo3.setPricePerDay(new BigDecimal("2.3"));
        gameInfo3.setGenre(GameInfo.Genre.FIRST_PERSON_SHOOTER);
        gameFile3.setInfo(gameInfo3);
        gameInfo3.setRating(new Rating(6300, 99));
        gameInfo3.setDescription("Some description");
        gameInfo3.setReleaseDate(LocalDate.of(2006, 6, 1));
        gameFileService.save(gameFile3);
        gameInfoService.save(gameInfo3);

        logger.info("Database initialization finished.");
    }
}