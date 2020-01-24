package com.netcracker.frolic.service;

import com.google.common.io.ByteStreams;
import com.netcracker.frolic.controller.QueryParamResolver;
import com.netcracker.frolic.entity.GameFile;
import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.entity.GamePic;
import com.netcracker.frolic.entity.Rating;
import com.netcracker.frolic.validator.Validator;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.engine.jdbc.BlobProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class DbInit {

    private final GameFileService fileService;
    private final GameInfoService infoService;
    private final GamePicService picService;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    DbInit(GameFileService fileService, GameInfoService infoService, GamePicService picService) {
        this.fileService = fileService;
        this.infoService = infoService;
        this.picService = picService;
    }

    @PostConstruct
    public void initDb() throws IOException {
        String titles = "Call of Duty: Mobile, Tropico, Mini Metro, The Room: Old Sins, Hades’ Star, " +
                "Madden NFL Mobile Football, Shadowgun Legends, Stardew Valley, Oceanhorn, Dragon Ball Legends, " +
                "Real Racing 3, Horizon Chase, inbento";
        String resourceNames = "COD, Tropico, MiniMetro, TheRoom, Hade, MaddenFootball, Shadowgun, StardewValley," +
                " Ocheanhorn, DragonBall, RealRacing, HorizonChase, Inbento";
        Iterator<String> titlesIter = Arrays.asList(titles.split(", ")).iterator();
        Iterator<String> resourceNamesIter = Arrays.asList(resourceNames.split(", ")).iterator();

        while (titlesIter.hasNext() && resourceNamesIter.hasNext()) {
            putGameInDB(titlesIter.next(), resourceNamesIter.next());
        }
    }

    private void putGameInDB(String title, String resourceName) throws IOException {
        Resource logo = new ClassPathResource("images/" + resourceName + ".jpg");
        byte[] logoBytes = ByteStreams.toByteArray(logo.getInputStream());
        String logoMimeType = "image/jpeg";
        String logoName = "Logo for " + title;
        GamePic gamePic = new GamePic(logoBytes, logoName, logoMimeType);

        Resource file = new ClassPathResource("files/" + resourceName + ".apk");
        byte[] fileBytes = ByteStreams.toByteArray(file.getInputStream());
        Blob fileBlob = BlobProxy.generateProxy(fileBytes);
        GameFile gameFile = new GameFile(fileBlob);

        Resource description = new ClassPathResource("text/" + resourceName + ".txt");
        String stringDescription = asString(description);

        GameInfo gameInfo = new GameInfo();
        gameInfo.setTitle(title);
        gameInfo.setFile(gameFile);
        gameInfo.setLogo(gamePic);
        String randomPrice = String.valueOf(random.nextInt(2, 22));
        gameInfo.setPricePerDay(new BigDecimal(randomPrice));
        gameInfo.setGenre(getRandomGenre());
        gameInfo.setRating(new Rating(random.nextInt(0, 1000), random.nextInt(1, 50)));
        gameInfo.setDescription(stringDescription);
        gameInfo.setReleaseDate(LocalDate.parse("2018-04-18"));

        gameFile.setInfo(gameInfo);
        gamePic.setInfo(gameInfo);
        fileService.save(gameFile);
        picService.save(gamePic);
        infoService.save(gameInfo);
    }

    private GameInfo.Genre getRandomGenre() {
        List<GameInfo.Genre> genres = Arrays.asList(GameInfo.Genre.values());
        int randomGenreIndex = random.nextInt(genres.size());
        return  genres.get(randomGenreIndex);
    }

    private static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}