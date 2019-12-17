package com.netcracker.frolic.repository;

import com.netcracker.frolic.entity.GameInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameInfoRepo extends JpaRepository<GameInfo, Long> {
}
