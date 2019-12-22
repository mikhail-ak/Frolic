package com.netcracker.frolic.repository;

import com.netcracker.frolic.entity.GameInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface GameInfoRepo extends JpaRepository<GameInfo, Long> {
    public Page<GameInfo> findAllByGenre(GameInfo.Genre genre, Pageable pageable);
    public Page<GameInfo> findAllByReleaseDate(LocalDateTime releaseDate, Pageable pageable);
}
