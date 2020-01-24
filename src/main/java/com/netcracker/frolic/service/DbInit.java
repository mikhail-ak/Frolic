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
        gameInfo1.setTitle("Call of Duty: Mobile");
        gameInfo1.setFile(gameFile1);
        gameInfo1.setPricePerDay(new BigDecimal("4.44"));
        gameInfo1.setGenre(GameInfo.Genre.FIRST_PERSON_SHOOTER);
        gameFile1.setInfo(gameInfo1);
        gameInfo1.setRating(new Rating(1100, 22));
        gameInfo1.setDescription("One of the biggest FPS franchises around comes to mobile with a game deliberately " +
                "designed for touchscreen firefights. If you love your shooter action, then this is a must on your " +
                "Android phone. It’s free, features a bunch of multiplayer modes, including Battle Royale, and you’ll " +
                "find some familiar classic maps and characters from other Call of Duty games. You can customize " +
                "loadouts, get ranked, win prizes, and more. But more important than all of that is the fast, frenetic, " +
                "satisfying gameplay and the great graphics which combine to make this the best mobile shooter around " +
                "right now. Just be sure to check out our Call of Duty: Mobile tips before you dive into the fray.");
        gameInfo1.setReleaseDate(LocalDate.of(1993, 11, 22));
        gameFileService.save(gameFile1);
        gameInfoService.save(gameInfo1);

        GameFile gameFile2 = new GameFile(BlobProxy.generateProxy(new byte[] {}));
        GameInfo gameInfo2 = new GameInfo();
        gameInfo2.setTitle("Tropico");
        gameInfo2.setFile(gameFile2);
        gameInfo2.setPricePerDay(new BigDecimal("4.11"));
        gameInfo2.setGenre(GameInfo.Genre.FIRST_PERSON_SHOOTER);
        gameFile2.setInfo(gameInfo2);
        gameInfo2.setRating(new Rating(1800, 19));
        gameInfo2.setDescription("There aren’t enough in-depth strategy games in the Play Store, so the arrival of a " +
                "redesigned Tropico title is worth celebrating. Cast in the role of El Presidente, you must modernize " +
                "your Caribbean island and see if you can line your own pockets along the way. You decide on an " +
                "approach, from ruling with an iron fist as a military dictator to creating a tourist haven beyond " +
                "compare to leading your people through a glorious industrial revolution that raises living standards " +
                "for all. ");
        gameInfo2.setReleaseDate(LocalDate.of(1984, 2, 18));
        gameFileService.save(gameFile2);
        gameInfoService.save(gameInfo2);

        GameFile gameFile3 = new GameFile(BlobProxy.generateProxy(new byte[] {}));
        GameInfo gameInfo3 = new GameInfo();
        gameInfo3.setTitle("Mini Metro");
        gameInfo3.setFile(gameFile3);
        gameInfo3.setPricePerDay(new BigDecimal("2.3"));
        gameInfo3.setGenre(GameInfo.Genre.FIRST_PERSON_SHOOTER);
        gameFile3.setInfo(gameInfo3);
        gameInfo3.setRating(new Rating(6300, 99));
        gameInfo3.setDescription("Can you create a working subway system for a city as it expands? This stylish," +
                "strangely soothing puzzle game seems simple at first, charging you with drawing lines between" +
                "stations to keep everything connected. But random city growth soon throws up obstacles and you’ll" +
                "unlock new trains, carriages, track, tunnels, and other upgrades to help you build out a great" +
                "metro network. There are 20 different cities to play, each with a slightly different feel and unique" +
                "challenge presented by the local river system.");
        gameInfo3.setReleaseDate(LocalDate.of(2006, 6, 1));
        gameFileService.save(gameFile3);
        gameInfoService.save(gameInfo3);

        logger.info("Database initialization finished.");
    }
}