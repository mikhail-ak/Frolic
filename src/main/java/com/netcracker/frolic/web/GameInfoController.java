package com.netcracker.frolic.web;

import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.service.GameInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/game-info")
@PropertySource("classpath:web.properties")
class GameInfoController {
    private final Logger log = LoggerFactory.getLogger(GameInfoController.class);
    @Autowired private GameInfoService gameInfoService;

    @Value("${itemsPerPage}")
    private int itemsPerPage;

    @GetMapping
    public Page<GameInfo> getAll() {
        return gameInfoService.findAll(PageRequest.of(0, itemsPerPage));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public GameInfo findGameInfoById(@PathVariable Long id) {
        return gameInfoService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}