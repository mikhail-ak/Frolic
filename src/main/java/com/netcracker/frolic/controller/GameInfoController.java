package com.netcracker.frolic.controller;

import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.entity.Rating;
import com.netcracker.frolic.service.GameInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@RestController
@PropertySource("classpath:web.properties")
@RequestMapping(value = "/game-info", produces = "application/json")
class GameInfoController {
    private final Logger log = LoggerFactory.getLogger(GameInfoController.class);
    public final int ITEMS_PER_PAGE_DEFAULT = 10;
    @Autowired private GameInfoService gameInfoService;
    @Autowired private Environment environment;

    enum SortType { RATING, RELEASE_DATE;
        static Sort resolve(String candidate) {
            return (candidate == null) ? Sort.unsorted()
                    : candidate.equalsIgnoreCase(RATING.toString()) ? Sort.by("rating")
                    : candidate.equalsIgnoreCase(RELEASE_DATE.toString()) ? Sort.by("releaseDate")
                    : Sort.unsorted();
        }
    }

    //FIXME сортировка по рейтингу изменяет порядок элементов, но не сортирует их
    //TODO проверять все введенные пользователем данные
    @GetMapping("/all")
    public Page<GameInfo> getAll(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "sort-by", required = false) String sortParam,
            @RequestParam(name = "find-by", required = false) String searchParam) {

        int itemsPerPage = Integer.parseInt(environment.getProperty("itemsPerPage"));
        Sort sortType = SortType.resolve(sortParam);
        log.warn(sortParam + sortType);
        return gameInfoService.findAll(PageRequest.of(pageNumber, itemsPerPage, sortType));
    }

    @GetMapping(value = "/{id}")
    public GameInfo findGameInfoById(@PathVariable Long id) {
        return gameInfoService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}