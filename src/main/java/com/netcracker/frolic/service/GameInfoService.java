package com.netcracker.frolic.service;

import com.google.common.base.CaseFormat;
import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;

public interface GameInfoService {
    Optional<GameInfo> findById(long id);
    void deleteById(long id);
    void updateGameInfo(long id, String title, BigDecimal price, GameInfo.Genre genre,
                        Rating rating, String description, Blob logo, LocalDate releaseDate);
    Page<GameInfo> findAllByGenre(GameInfo.Genre genre, Pageable pageable);
    Page<GameInfo> findByTitleContaining(String titlePiece, Pageable pageable);
    Page<GameInfo> findAll(Pageable pageable);
    GameInfo save(GameInfo gameInfo);
}