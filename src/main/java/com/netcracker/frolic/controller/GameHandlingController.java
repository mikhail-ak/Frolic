package com.netcracker.frolic.controller;

import com.netcracker.frolic.entity.GameFile;
import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.entity.GamePic;
import com.netcracker.frolic.entity.Rating;
import com.netcracker.frolic.service.EmailService;
import com.netcracker.frolic.service.GameFileService;
import com.netcracker.frolic.service.GameInfoService;
import com.netcracker.frolic.service.GamePicService;
import com.netcracker.frolic.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Blob;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping(value = "/game-handle", produces = "application/json")
public class GameHandlingController {
    private final GameFileService fileService;
    private final GameInfoService infoService;
    private final GamePicService picService;
    private final Validator<GameInfo> validator;
    private final QueryParamResolver resolver;
    private final EmailService emailService;



    GameHandlingController(GameFileService fileService, GameInfoService infoService, GamePicService picService,
                           QueryParamResolver resolver, EmailService emailService,
                           @Qualifier("gameInfoWebValidator") Validator<GameInfo> validator) {
        this.fileService = fileService;
        this.infoService = infoService;
        this.validator = validator;
        this.picService = picService;
        this.resolver = resolver;
        this.emailService = emailService;
    }

    @PostMapping
    public void postGame(@RequestBody HashMap<String, String> gameInfoMap) {
        log.warn(gameInfoMap.get("releaseDateF"));
        String base64logoWithType = gameInfoMap.get("logo");
        String logoMimeType = StringUtils.substringBetween(base64logoWithType, ":", ";");
        String logoName = "Logo for " + gameInfoMap.get("title");
        String base64logo = StringUtils.substringAfter(base64logoWithType, ",");
        byte[] logo = Base64Utils.decodeFromString(base64logo);
        GamePic gamePic = new GamePic(logo, logoName, logoMimeType);

        String base64file = StringUtils.substringAfter(gameInfoMap.get("file"), ",");
        GameFile gameFile = new GameFile(Base64Utils.decodeFromString(base64file));
        LocalDate releaseDate = Instant.ofEpochMilli(
                Long.parseLong(gameInfoMap.get("releaseDateF")))
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        GameInfo gameInfo = new GameInfo();
        gameInfo.setTitle(gameInfoMap.get("title"));
        gameInfo.setFile(gameFile);
        gameInfo.setLogo(gamePic);
        gameInfo.setPricePerDay(new BigDecimal(gameInfoMap.get("price")));
        gameInfo.setGenre(resolver.resolve(GameInfo.Genre.class, gameInfoMap.get("genre")));
        gameInfo.setRating(new Rating(0, 1));
        gameInfo.setDescription(gameInfoMap.get("description"));
        gameInfo.setReleaseDate(releaseDate);

        gameFile.setInfo(gameInfo);
        gamePic.setInfo(gameInfo);
        fileService.save(gameFile);
        picService.save(gamePic);

        validator.validate(gameInfo);
        infoService.save(gameInfo);
    }

    @DeleteMapping(value = "/{id}")
    public void removeGame(@PathVariable Long id) {
        GameInfo gameInfo = infoService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Resource mailMessage = new ClassPathResource("text/GameRemovalMail.txt");
        String stringMessage = GameInfoService.asString(mailMessage)
                .replace("[game_title]", gameInfo.getTitle());
        gameInfo.getSubscriptions()
                .forEach(subscription -> emailService.sendMail(
                        subscription.getUser().getEmail(),
                        "Subscription cancelled",
                        stringMessage
                ));

        infoService.deleteById(id);
    }
}
