package com.netcracker.frolic.controller;

import com.google.common.base.CaseFormat;
import com.netcracker.frolic.entity.GameFile;
import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.service.GameFileService;
import com.netcracker.frolic.service.GameInfoService;
import com.netcracker.frolic.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Objects;

import static com.netcracker.frolic.entity.GameInfo.Genre;

@Slf4j
@RestController
@RequestMapping(value = "/game-shop", produces = "application/json")
@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
class GameShopController {

    private final GameInfoService infoService;
    private final GameFileService fileService;
    private final QueryParamResolver resolver;
    private final Validator<GameInfo> validator;

    enum SortBy { RATING, RELEASE_DATE }
    enum FindBy { GENRE, TITLE }

    GameShopController(Environment environment, GameInfoService infoService, GameFileService fileService,
                       QueryParamResolver resolver, @Qualifier("gameInfoWebValidator") Validator<GameInfo> validator) {
        this.infoService = infoService;
        this.fileService = fileService;
        this.resolver = resolver;
        this.validator = validator;
    }

    @GetMapping("/all")
    public Page<GameInfo> getAll(@RequestParam(name = "page-number", defaultValue = "0") int pageNumber,
                                 @RequestParam(name = "items-per-page", defaultValue = "10") int itemsPerPage,
                                 @RequestParam(name = "sort-by", required = false) String sortParam,
                                 @RequestParam(name = "find-by", required = false) String searchParam,
                                 @RequestParam(name = "query",  required = false) String queryParam) {

        //будет ли сортировка по рейтингу или дате выхода; сформировать универсальный pageRequest
        SortBy sortBy = resolver.resolve(SortBy.class, sortParam);
        Sort sort = (sortBy == null) ? Sort.unsorted()
                : Sort.by(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, sortBy.toString()))
                .ascending();
        PageRequest pageRequest = PageRequest.of(pageNumber, itemsPerPage, sort);

        //будет ли поиск по жанру или по названию
        FindBy findBy = resolver.resolve(FindBy.class, searchParam);
        boolean searchIsByTitle = Objects.equals(findBy, FindBy.TITLE)  &&  queryParam != null;
        Genre genre = searchIsByTitle ? null : resolver.resolve(Genre.class, queryParam);
        boolean searchIsByGenre = Objects.equals(findBy, FindBy.GENRE)  &&  genre != null;

        return searchIsByTitle ? infoService.findByTitleContaining(queryParam, pageRequest)
                : searchIsByGenre ? infoService.findAllByGenre(genre, pageRequest)
                : infoService.findAll(pageRequest);
    }

    @GetMapping("game/{id}")
    public GameInfo findGameInfoById(@PathVariable Long id) {
        return infoService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public GameFile getFile(@PathVariable Long id) throws SQLException, IOException {
        return fileService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/{rating}")
    public void rate(@PathVariable long id, @PathVariable int rating) {
        if (rating > 100 || rating < 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The rating is out of boundaries");
        GameInfo gameInfo = infoService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no game" +
                        "with id " + id));
        gameInfo.getRating().add(rating);
        infoService.save(gameInfo);
    }
}