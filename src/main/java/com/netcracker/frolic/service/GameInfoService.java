package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.entity.Rating;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static java.nio.charset.StandardCharsets.UTF_8;

public interface GameInfoService {
    ThreadLocalRandom random = ThreadLocalRandom.current();

    Optional<GameInfo> findById(long id);

    void deleteById(long id);

    Page<GameInfo> findAllByGenre(GameInfo.Genre genre, Pageable pageable);

    Page<GameInfo> findByTitleContaining(String titlePiece, Pageable pageable);

    Page<GameInfo> findAll(Pageable pageable);

    GameInfo save(GameInfo gameInfo);

    boolean existsById(long id);

    static GameInfo.Genre getRandomGenre() {
        List<GameInfo.Genre> genres = Arrays.asList(GameInfo.Genre.values());
        int randomGenreIndex = random.nextInt(genres.size());
        return genres.get(randomGenreIndex);
    }

    static Rating getRandomRating() {
        int ratingsCount = random.nextInt(50, 100);
        int ratingsSum = random.ints(0, Rating.MAX_RATING)
                .limit(ratingsCount)
                .sum();
        return new Rating(ratingsSum, ratingsCount);
    }

    static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

     static BigDecimal getRandomPrice() {
        int priceLowerBoundary = 2;
        int priceUpperBoundary = 22;
        String randomPrice = String.valueOf(random.nextInt(priceLowerBoundary, priceUpperBoundary));
        return new BigDecimal(randomPrice);
    }

    static LocalDate getRandomDate() {
        int day = random.nextInt(1, 29);
        int month = random.nextInt(1, 13);
        int year = random.nextInt(2010, 2020);
        return LocalDate.of(year, month, day);
    }
}