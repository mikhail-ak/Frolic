package com.netcracker.frolic.entity;

import com.netcracker.frolic.config.DataJpaConfig;
import com.netcracker.frolic.repository.GameInfoRepo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class BasicORMTest {

    private GenericApplicationContext context;
    private GameInfoRepo gameInfoRepo;

    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext(DataJpaConfig.class);
        gameInfoRepo = context.getBean(GameInfoRepo.class);
        assertNotNull(gameInfoRepo);
    }

    @Test
    public void findByGenre() {

    }
}
