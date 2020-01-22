package com.netcracker.frolic.controller;

import com.netcracker.frolic.entity.GameFile;
import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.service.GameFileService;
import com.netcracker.frolic.service.GameInfoService;
import com.netcracker.frolic.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping(value = "/game", produces = "application/json")
public class GameHandlingController {
    private final GameFileService fileService;
    private final GameInfoService infoService;
    private final Validator<GameInfo> validator;

    GameHandlingController(GameFileService fileService, GameInfoService infoService,
                           @Qualifier("gameInfoWebValidator") Validator<GameInfo> validator) {
        this.fileService = fileService;
        this.infoService = infoService;
        this.validator = validator;
    }

    @GetMapping(value = "/{id}")
    public GameFile getFile(@PathVariable Long id) {
        return fileService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public void postGame(@RequestBody GameInfo gameInfo) {
        validator.validate(gameInfo);
        fileService.save(gameInfo.getFile());
        infoService.save(gameInfo);
    }
}
