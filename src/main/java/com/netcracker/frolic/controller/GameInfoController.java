package com.netcracker.frolic.controller;

import com.google.common.base.CaseFormat;
import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.service.GameInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static com.netcracker.frolic.entity.GameInfo.Genre;

@Slf4j
@RestController
@PropertySource("classpath:web.properties")
@RequestMapping(value = "/game-info", produces = "application/json")
class GameInfoController {
    final int DEFAULT_ITEMS_PER_PAGE = 10;
    final int ITEMS_PER_PAGE;

    private final GameInfoService gameInfoService;
    private final QueryParamResolver paramResolver;
    private final Validator<GameInfo> validator;

    enum SortType { RATING, RELEASE_DATE }
    enum SearchType { GENRE, TITLE }

    GameInfoController(Environment environment, GameInfoService service, QueryParamResolver resolver,
                       @Qualifier("gameInfoValidator") Validator<GameInfo> validator) {
        String itemsPerPageString = environment.getProperty("itemsPerPage");
        ITEMS_PER_PAGE = (itemsPerPageString == null) ? DEFAULT_ITEMS_PER_PAGE
                : Integer.parseInt(itemsPerPageString);
        gameInfoService = service;
        paramResolver = resolver;
        this.validator = validator;
    }

    //FIXME сортировка по рейтингу изменяет порядок элементов, но не сортирует их. Hibernate генерит
    //некорректный запрос из-за того, что рейтинг -- это embedded entity.
    @GetMapping("/all")
    public Page<GameInfo> getAll(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "sort-by", required = false) String sortParam,
            @RequestParam(name = "find-by", required = false) String searchParam,
            @RequestParam(name = "query",  required = false) String queryParam) {

        SortType sortType = paramResolver.resolve(SortType.class, sortParam);
        SearchType searchType = paramResolver.resolve(SearchType.class, searchParam);
        Genre genre = paramResolver.resolve(Genre.class, queryParam);

        Sort sort = (sortType == SortType.RELEASE_DATE) ? Sort.by("releaseDate")
                : (sortType == SortType.RATING) ? Sort.by("rating")
                : Sort.unsorted();
        PageRequest pageRequest = PageRequest.of(pageNumber, ITEMS_PER_PAGE, sort);
        boolean searchByTitle = Objects.equals(searchType, SearchType.TITLE)  &&  queryParam != null;
        boolean searchByGenre = Objects.equals(searchType, SearchType.GENRE)  &&  genre != null;

        return searchByTitle ? gameInfoService.findByTitleContaining(queryParam, pageRequest)
                : searchByGenre ? gameInfoService.findAllByGenre(genre, pageRequest)
                : gameInfoService.findAll(pageRequest);
    }

    @GetMapping(value = "/{id}")
    public GameInfo findGameInfoById(@PathVariable Long id) {
        return gameInfoService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(value = "/{id}")
    public void patchGameInfo(
            @PathVariable Long id,
            @RequestBody GameInfo infoFromJson) {
        GameInfo infoFromRepository = findGameInfoById(id);
        if (!infoFromRepository.getId().equals(infoFromJson.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "IDs in request and in JSON do not match");

        if (validator.approves(infoFromJson)) {
            gameInfoService.save(infoFromJson);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, validator.getErrorMessage());
        }
    }
}