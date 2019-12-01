package com.netcracker.frolic.repository;

import com.netcracker.frolic.entity.GameFile;
import org.springframework.stereotype.Repository;

@Repository
public interface GameFileRepo {
    GameFile save(GameFile gameFile);
}