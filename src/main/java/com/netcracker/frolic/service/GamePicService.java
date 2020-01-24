package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.GamePic;

import java.util.Optional;

public interface GamePicService {
    Optional<GamePic> findById(long id);
    void deleteById(long id);
    GamePic save(GamePic gameInfo);
}
