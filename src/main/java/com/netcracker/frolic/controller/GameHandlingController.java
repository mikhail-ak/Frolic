package com.netcracker.frolic.controller;

import com.netcracker.frolic.entity.GameFile;
import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.entity.GamePic;
import com.netcracker.frolic.entity.Rating;
import com.netcracker.frolic.service.GameFileService;
import com.netcracker.frolic.service.GameInfoService;
import com.netcracker.frolic.service.GamePicService;
import com.netcracker.frolic.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping(value = "/game-resources", produces = "application/json")
public class GameHandlingController {
    private final GameFileService fileService;
    private final GameInfoService infoService;
    private final GamePicService picService;
    private final Validator<GameInfo> validator;
    private final QueryParamResolver resolver;


    GameHandlingController(GameFileService fileService, GameInfoService infoService, GamePicService picService,
                           QueryParamResolver resolver,
                           @Qualifier("gameInfoWebValidator") Validator<GameInfo> validator) {
        this.fileService = fileService;
        this.infoService = infoService;
        this.validator = validator;
        this.picService = picService;
        this.resolver = resolver;
    }

    @GetMapping(value = "/{id}")
    public GameFile getFile(@PathVariable Long id) {
        return fileService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public void postGame(@RequestBody HashMap<String, String> gameInfoMap) {
        String base64logoWithType = gameInfoMap.get("logo");
        String logoMimeType = StringUtils.substringBetween(base64logoWithType, ":", ";");
        String logoName = "Logo for " + gameInfoMap.get("title");
        String base64logo = StringUtils.substringAfter(base64logoWithType, ",");
        byte[] logo = Base64Utils.decodeFromString(base64logo);
        GamePic gamePic = new GamePic(logo, logoName, logoMimeType);
        //log.warn(gamePic.toString());

        String base64file = StringUtils.substringAfter(gameInfoMap.get("file"), ",");
        Blob file = BlobProxy.generateProxy(Base64Utils.decodeFromString(base64file));
        GameFile gameFile = new GameFile(file);

        GameInfo gameInfo = new GameInfo();
        gameInfo.setTitle(gameInfoMap.get("title"));
        gameInfo.setFile(gameFile);
        gameInfo.setLogo(gamePic);
        gameInfo.setPricePerDay(new BigDecimal(gameInfoMap.get("price")));
        gameInfo.setGenre(resolver.resolve(GameInfo.Genre.class, gameInfoMap.get("genre")));
        gameInfo.setRating(new Rating(0, 1));
        gameInfo.setDescription(gameInfoMap.get("description"));
        gameInfo.setReleaseDate(LocalDate.parse(gameInfoMap.get("date")));

        gameFile.setInfo(gameInfo);
        gamePic.setInfo(gameInfo);
        fileService.save(gameFile);
        picService.save(gamePic);
        infoService.save(gameInfo);
    }
}
