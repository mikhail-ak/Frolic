package com.netcracker.frolic.repository;

import com.netcracker.frolic.entity.GameInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface GameInfoRepo {

    GameInfo save(GameInfo gameInfo);
    GameInfo findByID(Long id);
}
