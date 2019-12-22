package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface GameInfoService {
    Optional<GameInfo> findById(long id);
    void deleteById(long id);
    void updateGameInfo(long id, String title, BigDecimal price, GameInfo.Genre genre,
                        Rating rating, String description, Blob logo, LocalDate releaseDate);
    Page<GameInfo> findAllByGenre(GameInfo.Genre genre, Pageable pageable);
    Page<GameInfo> findAllByReleaseDate(LocalDateTime releaseDate, Pageable pageable);
    Page<GameInfo> findAll(Pageable pageable);
    Optional<GameInfo> save(GameInfo gameInfo);
}