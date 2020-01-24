package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.repository.GameInfoRepo;
import com.netcracker.frolic.validator.ValidatorImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@Service("jpaGameInfoService")
public class GameInfoServiceImpl implements GameInfoService {
    private final GameInfoRepo repository;
    private final ValidatorImpl<GameInfo> validator;

    GameInfoServiceImpl(GameInfoRepo repository,
                        @Qualifier("gameInfoJpaValidator") ValidatorImpl<GameInfo> validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Transactional(readOnly = true)
    public Optional<GameInfo> findById(long id)
    { return repository.findById(id); }

    public void deleteById(long id)
    { repository.deleteById(id); }

    @Transactional(readOnly = true)
    public Page<GameInfo> findAllByGenre(GameInfo.Genre genre, Pageable pageable)
    { return repository.findAllByGenre(genre, pageable); }

    @Transactional(readOnly = true)
    public Page<GameInfo> findByTitleContaining(String titlePiece, Pageable pageable)
    { return repository.findByTitleContaining(titlePiece, pageable); }

    @Transactional(readOnly = true)
    public Page<GameInfo> findAll(Pageable pageable)
    { return repository.findAll(pageable); }

    @Transactional(readOnly = true)
    public boolean existsById(long id)
    { return repository.existsById(id); }

    public GameInfo save(GameInfo gameInfo) {
        validator.validate(gameInfo);
        return repository.save(gameInfo);
    }
}