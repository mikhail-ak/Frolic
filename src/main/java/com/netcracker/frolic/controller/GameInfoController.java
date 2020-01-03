package com.netcracker.frolic.controller;

import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.service.GameInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@PropertySource("classpath:web.properties")
@RequestMapping(value = "/game-info", produces = "application/json")
class GameInfoController {
    private final Logger log = LoggerFactory.getLogger(GameInfoController.class);
    @Autowired private GameInfoService gameInfoService;
    @Autowired private Environment environment;

    //FIXME сортировка по рейтингу изменяет порядок элементов, но не сортирует их
    //TODO проверять все введенные пользователем данные
    @GetMapping("/all")
    public Page<GameInfo> getAll(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "sort-by", required = false) String sortParam,
            @RequestParam(name = "find-by", required = false) String searchParam,
            @RequestBody(required = false) String searchRequest) {

        int itemsPerPage = Integer.parseInt(environment.getProperty("itemsPerPage"));
        Sort sortType = GameInfoService.SortType.resolve(sortParam);
        PageRequest pageRequest = PageRequest.of(pageNumber, itemsPerPage, sortType);

        return GameInfoService.SearchType.isPresentAndValid(searchParam) ?
                gameInfoService.findByTitleContaining(searchRequest, pageRequest)
                : gameInfoService.findAll(pageRequest);
    }

    @GetMapping(value = "/{id}")
    public GameInfo findGameInfoById(@PathVariable Long id) {
        return gameInfoService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}