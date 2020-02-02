package com.netcracker.frolic.service;

import com.google.common.io.ByteStreams;
import com.netcracker.frolic.entity.GameFile;
import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.entity.GamePic;
import com.netcracker.frolic.entity.User;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.Blob;
import java.util.*;

@Service
public class DbInit {

    private final GameFileService fileService;
    private final GameInfoService infoService;
    private final GamePicService picService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /*
    Поставить в соответствие полному названию игры простое название. Полное название может содержать
    символы, недопустимые для имён файлов, отсюда и необходимость в простых названиях.
     */
    public final Map<String, String> GAME_TITLE_TO_RESOURCE_FILE_NAME;

    DbInit(GameFileService fileService, GameInfoService infoService, GamePicService picService,
           UserService userService, PasswordEncoder passwordEncoder) {
        this.fileService = fileService;
        this.infoService = infoService;
        this.picService = picService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

        Map<String, String> proxy = new HashMap<>();
        proxy.put("Call of Duty: Mobile", "COD");
        proxy.put("Tropico", "Tropico");
        proxy.put("Mini Metro", "MiniMetro");
        proxy.put("The Room: Old Sins", "TheRoom");
        proxy.put("Hades’ Star", "Hade");
        proxy.put("Madden NFL Mobile Football", "MaddenFootball");
        proxy.put("Shadowgun Legends", "Shadowgun");
        proxy.put("Stardew Valley", "StardewValley");
        proxy.put("Ocheanhorn", "Ocheanhorn");
        proxy.put("Dragon Ball Legends", "DragonBall");
        proxy.put("Real Racing 3", "RealRacing");
        proxy.put("Horizon Chase", "HorizonChase");
        proxy.put("inbento", "Inbento");
        GAME_TITLE_TO_RESOURCE_FILE_NAME = Collections.unmodifiableMap(proxy);
    }

    @PostConstruct
    public void initDb() {
        GAME_TITLE_TO_RESOURCE_FILE_NAME.entrySet()
                .forEach(this::getGameFromFilesAndPutItInDB);

        User admin = new User();
        admin.setName("FBombChampion");
        admin.setEmail("example@gamil.com");
        admin.setPassword(passwordEncoder.encode("Ab123456"));
        admin.setRoles(Arrays.asList("ROLE_ADMIN"));
        userService.save(admin);

        User employee = new User();
        employee.setName("CptButtPirate");
        employee.setEmail("ample@gamil.com");
        employee.setPassword(passwordEncoder.encode("Ab123456"));
        employee.setRoles(Arrays.asList("ROLE_EMPLOYEE"));
        userService.save(employee);

        User client = new User();
        client.setName("BigNickDigger");
        client.setEmail("ple@gamil.com");
        client.setPassword(passwordEncoder.encode("Ab123456"));
        client.setRoles(Arrays.asList("ROLE_CLIENT"));
        userService.save(client);
    }

    private void getGameFromFilesAndPutItInDB(Map.Entry<String, String> titleAndFileName) {
        String title = titleAndFileName.getKey();
        String resourceName = titleAndFileName.getValue();
        Resource logo = new ClassPathResource("images/" + resourceName + ".jpg");
        byte[] logoBytes;
        try {
            logoBytes = ByteStreams.toByteArray(logo.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String logoMimeType = "image/jpeg";
        String logoName = "Logo for " + title;
        GamePic gamePic = new GamePic(logoBytes, logoName, logoMimeType);

        Resource file = new ClassPathResource("files/" + resourceName + ".zip");
        byte[] fileBytes;
        try {
            fileBytes = ByteStreams.toByteArray(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GameFile gameFile = new GameFile(fileBytes);

        Resource description = new ClassPathResource("text/" + resourceName + ".txt");
        String stringDescription = GameInfoService.asString(description);

        GameInfo gameInfo = new GameInfo();
        gameInfo.setTitle(title);
        gameInfo.setFile(gameFile);
        gameInfo.setLogo(gamePic);
        gameInfo.setPricePerDay(GameInfoService.getRandomPrice());
        gameInfo.setGenre(GameInfoService.getRandomGenre());
        gameInfo.setRating(GameInfoService.getRandomRating());
        gameInfo.setDescription(stringDescription);
        gameInfo.setReleaseDate(GameInfoService.getRandomDate());

        gameFile.setInfo(gameInfo);
        gamePic.setInfo(gameInfo);
        fileService.save(gameFile);
        picService.save(gamePic);
        infoService.save(gameInfo);
    }

}