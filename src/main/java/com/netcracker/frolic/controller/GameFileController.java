package com.netcracker.frolic.controller;

import com.netcracker.frolic.entity.GameFile;
import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.service.GameFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping(value = "/game-file", produces = "application/json")
public class GameFileController {
    private final GameFileService service;

    GameFileController(GameFileService service)
    { this.service = service; }

    @GetMapping(value = "/{id}")
    public GameFile getFile(@PathVariable Long id) {
        return service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping
    public void putFile(
            @PathVariable Long id,
            @RequestBody GameFile fileFromJson) {
        service.save(fileFromJson);
    }
}
