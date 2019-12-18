package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.entity.Rating;
import com.netcracker.frolic.repository.GameInfoRepo;
import com.netcracker.frolic.validator.GameInfoValidator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
@Service("jpaGameInfoService")
public class GameInfoServiceImpl implements GameInfoService {
    @Autowired private GameInfoRepo gameInfoRepo;
    @Autowired private Logger log;

    @Transactional(readOnly = true)
    public Optional<GameInfo> findById(long id)
    { return gameInfoRepo.findById(id); }

    public void deleteById(long id)
    { gameInfoRepo.deleteById(id); }

    @Override
    public void updateGameInfo(long id, String title, BigDecimal price, GameInfo.Genre genre, Rating rating,
                               String description, Blob logo, LocalDate releaseDate) {
        gameInfoRepo.findById(id).ifPresent(info -> {
            info.setTitle(title);
            info.setPricePerDay(price);
            info.setGenre(genre);
            info.setRating(rating);
            info.setDescription(description);
            info.setLogo(logo);
            info.setReleaseDate(releaseDate);
            log.debug("Game info updated: {}", info);
        });
    }

    @Transactional(readOnly = true)
    public Page<GameInfo> findAllByGenre(GameInfo.Genre genre)
    { return gameInfoRepo.findAllByGenre(genre); }

    @Transactional(readOnly = true)
    public Page<GameInfo> findAllByReleaseDate(LocalDateTime releaseDate)
    { return gameInfoRepo.findAllByReleaseDate(releaseDate); }

    @Transactional(readOnly = true)
    public Page<GameInfo> findAll(Pageable pageable)
    { return gameInfoRepo.findAll(pageable); }

    public Optional<GameInfo> save(GameInfo gameInfo) {
        return new GameInfoValidator(errorMessage -> log
                .error("Tried to save invalid game info: {}", errorMessage))
                .getIfValid(gameInfo)
                .map(info -> gameInfoRepo.save(info));
    }
}