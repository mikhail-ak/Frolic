package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.GameFile;
import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.entity.Rating;
import com.netcracker.frolic.repository.GameFileRepo;
import com.netcracker.frolic.repository.GameInfoRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;

@Service
public class DbInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DbInitializer.class);

    @Autowired
    GameFileRepo gameFileRepo;
    @Autowired
    GameInfoRepo gameInfoRepo;


    @PostConstruct
    public void initDB() {
        logger.info("starting DB initialization now");

        Blob giantUglyThing = new Blob() {
            @Override
            public long length() throws SQLException {
                return 0;
            }

            @Override
            public byte[] getBytes(long l, int i) throws SQLException {
                return new byte[0];
            }

            @Override
            public InputStream getBinaryStream() throws SQLException {
                return null;
            }

            @Override
            public long position(byte[] bytes, long l) throws SQLException {
                return 0;
            }

            @Override
            public long position(Blob blob, long l) throws SQLException {
                return 0;
            }

            @Override
            public int setBytes(long l, byte[] bytes) throws SQLException {
                return 0;
            }

            @Override
            public int setBytes(long l, byte[] bytes, int i, int i1) throws SQLException {
                return 0;
            }

            @Override
            public OutputStream setBinaryStream(long l) throws SQLException {
                return null;
            }

            @Override
            public void truncate(long l) throws SQLException {

            }

            @Override
            public void free() throws SQLException {

            }

            @Override
            public InputStream getBinaryStream(long l, long l1) throws SQLException {
                return null;
            }
        };
        GameFile gameFile = new GameFile(giantUglyThing);
        GameInfo gameInfo = new GameInfo("Test game", BigDecimal.TEN);
        gameInfo.setFile(gameFile);
        gameInfo.setRating(new Rating());
        gameFile.setInfo(gameInfo);
        gameFileRepo.save(gameFile);
        gameInfoRepo.save(gameInfo);

        logger.info("Database initilization finished.");

    }
}
