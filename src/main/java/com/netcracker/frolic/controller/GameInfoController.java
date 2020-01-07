package com.netcracker.frolic.controller;

import com.google.common.base.CaseFormat;
import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.service.GameInfoService;
import com.netcracker.frolic.validator.Validator;
import com.netcracker.frolic.validator.ValidatorImpl;
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

//TODO передача id в запрос избыточна если он уже есть в json
@Slf4j
@RestController
@PropertySource("classpath:web.properties")
@RequestMapping(value = "/game-info", produces = "application/json")
class GameInfoController {
    final int DEFAULT_ITEMS_PER_PAGE = 10;
    final int ITEMS_PER_PAGE;

    private final GameInfoService service;
    private final QueryParamResolver resolver;
    private final Validator<GameInfo> validator;

    enum SortBy { RATING, RELEASE_DATE }
    enum FindBy { GENRE, TITLE }

    GameInfoController(Environment environment, GameInfoService service, QueryParamResolver resolver,
                       @Qualifier("gameInfoWebValidator") Validator<GameInfo> validator) {
        String itemsPerPageString = environment.getProperty("itemsPerPage");
        ITEMS_PER_PAGE = (itemsPerPageString == null) ? DEFAULT_ITEMS_PER_PAGE
                : Integer.parseInt(itemsPerPageString);
        this.service = service;
        this.resolver = resolver;
        this.validator = validator;
    }

    @GetMapping("/all")
    public Page<GameInfo> getAll(@RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                 @RequestParam(name = "sort-by", required = false) String sortParam,
                                 @RequestParam(name = "find-by", required = false) String searchParam,
                                 @RequestParam(name = "query",  required = false) String queryParam) {

        //будет ли сортировка по рейтингу или дате выхода; сформировать универсальный pageRequest
        SortBy sortBy = resolver.resolve(SortBy.class, sortParam);
        Sort sort = (sortBy == null) ? Sort.unsorted()
                : Sort.by(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, sortBy.toString()));
        PageRequest pageRequest = PageRequest.of(pageNumber, ITEMS_PER_PAGE, sort);

        //будет ли поиск по жанру или по названию
        FindBy findBy = resolver.resolve(FindBy.class, searchParam);
        boolean searchIsByTitle = Objects.equals(findBy, FindBy.TITLE)  &&  queryParam != null;
        Genre genre = searchIsByTitle ? null : resolver.resolve(Genre.class, queryParam);
        boolean searchIsByGenre = Objects.equals(findBy, FindBy.GENRE)  &&  genre != null;

        return searchIsByTitle ? service.findByTitleContaining(queryParam, pageRequest)
                : searchIsByGenre ? service.findAllByGenre(genre, pageRequest)
                : service.findAll(pageRequest);
    }

    @PatchMapping(value = "/{id}")
    public void patchGameInfo(@PathVariable Long id,
                              @RequestBody GameInfo infoFromJson) {
        if(!service.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        GameInfo infoFromRepository = findGameInfoById(id);
        if (!infoFromRepository.getId().equals(infoFromJson.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "IDs in HTTP request and in JSON do not match");

        validator.validate(infoFromJson);
        service.save(infoFromJson);
    }

    @GetMapping(value = "/{id}")
    public GameInfo findGameInfoById(@PathVariable Long id) {
        return service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}