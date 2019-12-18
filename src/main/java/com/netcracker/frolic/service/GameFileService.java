package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.GameFile;

import java.util.Optional;

/**
 * id GameInfo всегда совпадает с id соответствующего GameFile за счет @MapsId,
 * поэтому удобно найти соответствующей игре файл по id. Поиск файлов по другим критериям
 * не представляется нужным.
 */
public interface GameFileService {
    Optional<GameFile> findById(long id);
    void deleteById(long id);
    GameFile save(GameFile gameInfo);
}
