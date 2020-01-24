package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.GameInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface GameInfoService {
    Optional<GameInfo> findById(long id);
    void deleteById(long id);
    Page<GameInfo> findAllByGenre(GameInfo.Genre genre, Pageable pageable);
    Page<GameInfo> findByTitleContaining(String titlePiece, Pageable pageable);
    Page<GameInfo> findAll(Pageable pageable);
    GameInfo save(GameInfo gameInfo);
    boolean existsById(long id);
}