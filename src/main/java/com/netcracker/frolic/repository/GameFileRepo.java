package com.netcracker.frolic.repository;

import com.netcracker.frolic.entity.GameFile;
import org.springframework.data.repository.CrudRepository;

public interface GameFileRepo extends CrudRepository<GameFile, Long> {
}