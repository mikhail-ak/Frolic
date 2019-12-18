package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.GameFile;
import com.netcracker.frolic.repository.GameFileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service("jpaGameFileService")
public class GameFileServiceImpl implements GameFileService {
    @Autowired private GameFileRepo gameFileRepo;

    @Transactional(readOnly = true)
    public Optional<GameFile> findById(long id)
    { return gameFileRepo.findById(id); }

    public void deleteById(long id)
    { gameFileRepo.deleteById(id); }

    public GameFile save(GameFile gameInfo)
    { return gameFileRepo.save(gameInfo); }
}
