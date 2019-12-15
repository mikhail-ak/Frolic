package com.netcracker.frolic.entity;

import com.netcracker.frolic.config.ApplicationConfig;
import com.netcracker.frolic.repository.GameInfoRepo;
import com.netcracker.frolic.repository.GenreRepo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class BasicORMTest {

    private GenericApplicationContext context;

    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        GameInfoRepo gameInfoRepo = context.getBean(GameInfoRepo.class);
        GenreRepo genreRepo = context.getBean(GenreRepo.class);
        assertNotNull(gameInfoRepo);
        assertNotNull(genreRepo);
    }

    @Test
    public void findByGenre() {
        GenreRepo genreRepo = context.getBean(GenreRepo.class);
        Genre genre = genreRepo.getById(1L)
                .orElse(new Genre("FPS"));
        GameInfoRepo gameInfoRepo = context.getBean(GameInfoRepo.class);
        List<GameInfo> infos = gameInfoRepo.findAllByGenre(genre, 0, 5);
        assertFalse(infos.isEmpty());
    }
}
